package com.kazakago.preferhythm;

import android.content.Context;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by tamura_k on 2017/03/08.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.kazakago.preferhythm.PrefClass", "com.kazakago.preferhythm.PrefField", "com.kazakago.preferhythm.PrefKeyName"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class QuickPrefProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Class<PrefClass> prefClass = PrefClass.class;
        for (Element element : roundEnv.getElementsAnnotatedWith(prefClass)) {
            ElementKind kind = element.getKind();
            if (kind == ElementKind.CLASS) {
                try {
                    generateLogger(element);
                } catch (IOException e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "IO error");
                }
            } else {
                messager.printMessage(Diagnostic.Kind.ERROR, "Type error");
            }
        }
        return true;
    }

    /**
     * エレメントからLoggerのソースを出力する
     */
    private void generateLogger(Element element) throws IOException {
        String packageName = elements.getPackageOf(element).getQualifiedName().toString();
        ClassName modelClassName = ClassName.get(packageName, element.getSimpleName().toString());
        ClassName generatedClassName = ClassName.get(packageName, element.getSimpleName().toString() + "Manager");

        // モデルクラスフィールドの定義
        FieldSpec modelField = FieldSpec.builder(modelClassName, "modelInstance")
                .addModifiers(Modifier.PRIVATE)
                .initializer("new $T()", modelClassName)
                .build();

        // コンストラクタの定義
        ParameterSpec contextParameter = ParameterSpec.builder(Context.class, "context")
                .addAnnotation(Nonnull.class)
                .build();
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextParameter)
                .addStatement("super(context)")
                .build();

        // getSharedPreferencesNameメソッドの定義
        MethodSpec getPrefNameMethod = MethodSpec.methodBuilder("getSharedPreferencesName")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Nonnull.class)
                .addAnnotation(Override.class)
                .addStatement("return $S", modelClassName.simpleName())
                .returns(String.class)
                .build();

        // getメソッドの定義
        List<MethodSpec> getMethods = new ArrayList<>();
        for (Element el : element.getEnclosedElements()) {
            if (el.getAnnotation(PrefField.class) != null) {
                TypeName fieldType = ClassName.get(el.asType());
                String methodNameParam;
                String defaultValue;
                if (fieldType.equals(TypeName.INT) || fieldType.equals(TypeName.INT.box())) {
                    methodNameParam = "Int";
                    defaultValue = "0";
                } else if (fieldType.equals(TypeName.LONG) || fieldType.equals(TypeName.INT.box())) {
                    methodNameParam = "Long";
                    defaultValue = "0";
                } else if (fieldType.equals(TypeName.FLOAT) || fieldType.equals(TypeName.INT.box())) {
                    methodNameParam = "Float";
                    defaultValue = "0";
                } else if (fieldType.equals(TypeName.BOOLEAN) || fieldType.equals(TypeName.INT.box())) {
                    methodNameParam = "Boolean";
                    defaultValue = "false";
                } else if (fieldType.equals(TypeName.get(String.class))) {
                    methodNameParam = "String";
                    defaultValue = "null";
                } else if (fieldType.equals(ParameterizedTypeName.get(Set.class, String.class))) {
                    methodNameParam = "StringSet";
                    defaultValue = "null";
                } else {
                    continue;
                }
                String fieldName = el.getSimpleName().toString();
                PrefKeyName prefKeyNameAnnotation = el.getAnnotation(PrefKeyName.class);
                String prefKeyName = (prefKeyNameAnnotation != null) ? prefKeyNameAnnotation.value() : fieldName;
                MethodSpec.Builder getMethodBuilder = MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName));
                if (fieldType.isPrimitive()) {
                    getMethodBuilder.addStatement("$T value = getSharedPreferences().get$L($S, modelInstance.$L)", fieldType, methodNameParam, prefKeyName, fieldName)
                            .addStatement("return value");
                } else {
                    getMethodBuilder.beginControlFlow("if (getSharedPreferences().contains($S))", prefKeyName)
                            .addStatement("return getSharedPreferences().get$L($S, $L)", methodNameParam, prefKeyName, defaultValue)
                            .nextControlFlow("else")
                            .beginControlFlow("if (get$LIsNull())", StringUtils.capitalize(fieldName))
                            .addStatement("return null")
                            .nextControlFlow("else")
                            .addStatement("return modelInstance.$L", fieldName)
                            .endControlFlow()
                            .endControlFlow();
                    if (el.getAnnotation(Nonnull.class) != null) {
                        getMethodBuilder.addAnnotation(Nonnull.class);
                    } else {
                        getMethodBuilder.addAnnotation(Nullable.class);
                    }
                }
                MethodSpec getMethod = getMethodBuilder
                        .addModifiers(Modifier.PUBLIC)
                        .returns(fieldType)
                        .build();
                getMethods.add(getMethod);
            }
        }

        //getIsNullメソッドの定義
        List<MethodSpec> getIsNullMethods = new ArrayList<>();
        for (Element el : element.getEnclosedElements()) {
            if (el.getAnnotation(PrefField.class) != null) {
                String fieldName = el.getSimpleName().toString();
                TypeName fieldType = ClassName.get(el.asType());
                PrefKeyName prefKeyNameAnnotation = el.getAnnotation(PrefKeyName.class);
                String prefKeyName = (prefKeyNameAnnotation != null) ? prefKeyNameAnnotation.value() : fieldName;
                MethodSpec.Builder getIsNullMethodBuilder = MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName) + "IsNull");
                if (fieldType.equals(TypeName.INT.box()) ||
                        fieldType.equals(TypeName.LONG.box()) ||
                        fieldType.equals(TypeName.FLOAT.box()) ||
                        fieldType.equals(TypeName.BOOLEAN.box()) ||
                        fieldType.equals(TypeName.get(String.class)) ||
                        fieldType.equals(ParameterizedTypeName.get(Set.class, String.class))) {
                    getIsNullMethodBuilder.addStatement("return getSharedPreferences().getBoolean($S, $L)", prefKeyName + "IsNull", false);
                } else {
                    continue;
                }
                MethodSpec getIsNullMethod = getIsNullMethodBuilder
                        .addModifiers(Modifier.PRIVATE)
                        .returns(boolean.class)
                        .build();
                getIsNullMethods.add(getIsNullMethod);
            }
        }

        // putメソッドの定義
        List<MethodSpec> putMethods = new ArrayList<>();
        for (Element el : element.getEnclosedElements()) {
            if (el.getAnnotation(PrefField.class) != null) {
                TypeName fieldType = ClassName.get(el.asType());
                String methodNameParam;
                if (fieldType.equals(TypeName.INT) || fieldType.equals(TypeName.INT.box())) {
                    methodNameParam = "Int";
                } else if (fieldType.equals(TypeName.LONG) || fieldType.equals(TypeName.INT.box())) {
                    methodNameParam = "Long";
                } else if (fieldType.equals(TypeName.FLOAT) || fieldType.equals(TypeName.INT.box())) {
                    methodNameParam = "Float";
                } else if (fieldType.equals(TypeName.BOOLEAN) || fieldType.equals(TypeName.INT.box())) {
                    methodNameParam = "Boolean";
                } else if (fieldType.equals(TypeName.get(String.class))) {
                    methodNameParam = "String";
                } else if (fieldType.equals(ParameterizedTypeName.get(Set.class, String.class))) {
                    methodNameParam = "StringSet";
                } else {
                    continue;
                }
                String fieldName = el.getSimpleName().toString();
                PrefKeyName prefKeyNameAnnotation = el.getAnnotation(PrefKeyName.class);
                String prefKeyName = (prefKeyNameAnnotation != null) ? prefKeyNameAnnotation.value() : fieldName;
                MethodSpec.Builder putMethodBuilder = MethodSpec.methodBuilder("put" + StringUtils.capitalize(fieldName));
                ParameterSpec.Builder valueParameterBuilder = ParameterSpec.builder(fieldType, "value");
                if (fieldType.isPrimitive()) {
                    putMethodBuilder.addStatement("getSharedPreferencesEditor().put$L($S, value)", methodNameParam, prefKeyName);
                } else {
                    putMethodBuilder.beginControlFlow("if (value != null)")
                            .addStatement("getSharedPreferencesEditor().put$L($S, value)", methodNameParam, prefKeyName)
                            .addStatement("put$LIsNull(false)", StringUtils.capitalize(fieldName))
                            .nextControlFlow("else")
                            .addStatement("getSharedPreferencesEditor().remove($S)", prefKeyName)
                            .addStatement("put$LIsNull(true)", StringUtils.capitalize(fieldName))
                            .endControlFlow();
                    if (el.getAnnotation(Nonnull.class) != null) {
                        valueParameterBuilder.addAnnotation(Nonnull.class);
                    } else {
                        valueParameterBuilder.addAnnotation(Nullable.class);
                    }
                }
                ParameterSpec parameter = valueParameterBuilder.build();
                MethodSpec putMethod = putMethodBuilder
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Nonnull.class)
                        .addParameter(parameter)
                        .addStatement("return this")
                        .returns(generatedClassName)
                        .build();
                putMethods.add(putMethod);
            }
        }

        // putIsNullメソッドの定義
        List<MethodSpec> putIsNullMethods = new ArrayList<>();
        for (Element el : element.getEnclosedElements()) {
            if (el.getAnnotation(PrefField.class) != null) {
                String fieldName = el.getSimpleName().toString();
                TypeName fieldType = ClassName.get(el.asType());
                PrefKeyName prefKeyNameAnnotation = el.getAnnotation(PrefKeyName.class);
                String prefKeyName = (prefKeyNameAnnotation != null) ? prefKeyNameAnnotation.value() : fieldName;
                MethodSpec.Builder putMethodBuilder = MethodSpec.methodBuilder("put" + StringUtils.capitalize(fieldName) + "IsNull");
                if (fieldType.equals(TypeName.INT.box()) ||
                        fieldType.equals(TypeName.LONG.box()) ||
                        fieldType.equals(TypeName.FLOAT.box()) ||
                        fieldType.equals(TypeName.BOOLEAN.box()) ||
                        fieldType.equals(TypeName.get(String.class)) ||
                        fieldType.equals(ParameterizedTypeName.get(Set.class, String.class))) {
                    putMethodBuilder.addStatement("getSharedPreferencesEditor().putBoolean($S, value)", prefKeyName + "IsNull");
                } else {
                    continue;
                }
                MethodSpec putIsNullMethod = putMethodBuilder
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(Nonnull.class)
                        .addParameter(boolean.class, "value")
                        .addStatement("return this")
                        .returns(generatedClassName)
                        .build();
                putIsNullMethods.add(putIsNullMethod);
            }
        }

        // removeメソッドの定義
        List<MethodSpec> removeMethods = new ArrayList<>();
        for (Element el : element.getEnclosedElements()) {
            if (el.getAnnotation(PrefField.class) != null) {
                String fieldName = el.getSimpleName().toString();
                TypeName fieldType = ClassName.get(el.asType());
                PrefKeyName prefKeyNameAnnotation = el.getAnnotation(PrefKeyName.class);
                String prefKeyName = (prefKeyNameAnnotation != null) ? prefKeyNameAnnotation.value() : fieldName;
                MethodSpec.Builder removeMethodBuilder = MethodSpec.methodBuilder("remove" + StringUtils.capitalize(fieldName));
                if (fieldType.equals(TypeName.INT) ||
                        fieldType.equals(TypeName.LONG) ||
                        fieldType.equals(TypeName.FLOAT) ||
                        fieldType.equals(TypeName.BOOLEAN) ||
                        fieldType.equals(TypeName.INT.box()) ||
                        fieldType.equals(TypeName.LONG.box()) ||
                        fieldType.equals(TypeName.FLOAT.box()) ||
                        fieldType.equals(TypeName.BOOLEAN.box()) ||
                        fieldType.equals(TypeName.get(String.class)) ||
                        fieldType.equals(ParameterizedTypeName.get(Set.class, String.class))) {
                    removeMethodBuilder.addStatement("getSharedPreferencesEditor().remove($S)", prefKeyName);
                    removeMethodBuilder.addStatement("getSharedPreferencesEditor().remove($S)", prefKeyName + "IsNull");
                } else {
                    continue;
                }
                MethodSpec removeMethod = removeMethodBuilder.addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Nonnull.class)
                        .addStatement("return this")
                        .returns(generatedClassName)
                        .build();
                removeMethods.add(removeMethod);
            }
        }

        // クラスの定義
        TypeSpec generatedClass = TypeSpec.classBuilder(generatedClassName)
                .superclass(BasePreferhythm.class)
                .addModifiers(Modifier.PUBLIC)
                .addField(modelField)
                .addMethod(constructor)
                .addMethod(getPrefNameMethod)
                .addMethods(getMethods)
                .addMethods(getIsNullMethods)
                .addMethods(putMethods)
                .addMethods(putIsNullMethods)
                .addMethods(removeMethods)
                .build();

        // ファイルへ出力
        JavaFile.builder(packageName, generatedClass)
                .build()
                .writeTo(filer);
    }

}