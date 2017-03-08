package com.kazakago.preferhythm;

import android.content.Context;

import com.google.auto.service.AutoService;
import com.kazakago.quickpref.BasePrefManager;
import com.kazakago.quickpref.PrefKey;
import com.kazakago.quickpref.PrefName;
import com.kazakago.quickpref.SharedPreferencesWrapper;
import com.squareup.javapoet.ClassName;
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
@SupportedAnnotationTypes({"com.kazakago.preferhythm.PrefName", "com.kazakago.preferhythm.PrefKey"})
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
        Class<PrefName> prefClass = PrefName.class;
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

        // コンストラクタの定義
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Context.class, "context")
                .addStatement("super(context)")
                .build();

        // getPrefNameメソッドの定義
        MethodSpec getPrefNameMethod = MethodSpec.methodBuilder("getPrefName")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .addStatement("return $S", modelClassName.simpleName())
                .returns(String.class)
                .build();

        // getメソッドの定義
        List<MethodSpec> getMethods = new ArrayList<>();
        for (Element el : element.getEnclosedElements()) {
            if (el.getAnnotation(PrefKey.class) != null) {
                String fieldName = el.getSimpleName().toString();
                TypeName fieldType = ClassName.get(el.asType());
                MethodSpec.Builder getMethodBuilder = MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName));
                if (!fieldType.isPrimitive()) {
                    getMethodBuilder.addAnnotation(Nullable.class);
                }
                if (fieldType.equals(TypeName.INT)) {
                    getMethodBuilder.addStatement("return getSharedPreferences().getInt($S, $L)", fieldName, 0);
                } else if (fieldType.equals(TypeName.LONG)) {
                    getMethodBuilder.addStatement("return getSharedPreferences().getLong($S, $L)", fieldName, 0);
                } else if (fieldType.equals(TypeName.FLOAT)) {
                    getMethodBuilder.addStatement("return getSharedPreferences().getFloat($S, $L)", fieldName, 0);
                } else if (fieldType.equals(TypeName.BOOLEAN)) {
                    getMethodBuilder.addStatement("return getSharedPreferences().getBoolean($S, $L)", fieldName, false);
                } else if (fieldType.equals(TypeName.INT.box())) {
                    getMethodBuilder.addStatement("return $T.getInteger(getSharedPreferences(), $S)", SharedPreferencesWrapper.class, fieldName);
                } else if (fieldType.equals(TypeName.LONG.box())) {
                    getMethodBuilder.addStatement("return $T.getLong(getSharedPreferences(), $S)", SharedPreferencesWrapper.class, fieldName);
                } else if (fieldType.equals(TypeName.FLOAT.box())) {
                    getMethodBuilder.addStatement("return $T.getFloat(getSharedPreferences(), $S)", SharedPreferencesWrapper.class, fieldName);
                } else if (fieldType.equals(TypeName.BOOLEAN.box())) {
                    getMethodBuilder.addStatement("return $T.getBoolean(getSharedPreferences(), $S)", SharedPreferencesWrapper.class, fieldName);
                } else if (fieldType.equals(TypeName.get(String.class))) {
                    getMethodBuilder.addStatement("return $T.getString(getSharedPreferences(), $S)", SharedPreferencesWrapper.class, fieldName);
                } else if (fieldType.equals(ParameterizedTypeName.get(Set.class, String.class))) {
                    getMethodBuilder.addStatement("return $T.getStringSet(getSharedPreferences(), $S)", SharedPreferencesWrapper.class, fieldName);
                } else {
                    continue;
                }
                MethodSpec getMethod = getMethodBuilder
                        .addModifiers(Modifier.PUBLIC)
                        .returns(fieldType)
                        .build();
                getMethods.add(getMethod);
            }
        }

        // putメソッドの定義
        List<MethodSpec> putMethods = new ArrayList<>();
        for (Element el : element.getEnclosedElements()) {
            if (el.getAnnotation(PrefKey.class) != null) {
                String fieldName = el.getSimpleName().toString();
                TypeName fieldType = ClassName.get(el.asType());
                MethodSpec.Builder putMethodBuilder = MethodSpec.methodBuilder("put" + StringUtils.capitalize(fieldName));
                ParameterSpec.Builder parameterBuilder = ParameterSpec.builder(fieldType, "value");
                if (!fieldType.isPrimitive()) {
                    parameterBuilder.addAnnotation(Nullable.class);
                }
                ParameterSpec parameter = parameterBuilder.build();
                if (fieldType.equals(TypeName.INT)) {
                    putMethodBuilder.addStatement("getEditor().putInt($S, value)", fieldName);
                } else if (fieldType.equals(TypeName.LONG)) {
                    putMethodBuilder.addStatement("getEditor().putLong($S, value)", fieldName);
                } else if (fieldType.equals(TypeName.FLOAT)) {
                    putMethodBuilder.addStatement("getEditor().putFloat($S, value)", fieldName);
                } else if (fieldType.equals(TypeName.BOOLEAN)) {
                    putMethodBuilder.addStatement("getEditor().putBoolean($S, value)", fieldName);
                } else if (fieldType.equals(TypeName.INT.box())) {
                    putMethodBuilder.addStatement("$T.putInteger(getEditor(), $S, value)", SharedPreferencesWrapper.class, fieldName);
                } else if (fieldType.equals(TypeName.LONG.box())) {
                    putMethodBuilder.addStatement("$T.putLong(getEditor(), $S, value)", SharedPreferencesWrapper.class, fieldName);
                } else if (fieldType.equals(TypeName.FLOAT.box())) {
                    putMethodBuilder.addStatement("$T.putFloat(getEditor(), $S, value)", SharedPreferencesWrapper.class, fieldName);
                } else if (fieldType.equals(TypeName.BOOLEAN.box())) {
                    putMethodBuilder.addStatement("$T.putBoolean(getEditor(), $S, value)", SharedPreferencesWrapper.class, fieldName);
                } else if (fieldType.equals(TypeName.get(String.class))) {
                    putMethodBuilder.addStatement("$T.putString(getEditor(), $S, value)", SharedPreferencesWrapper.class, fieldName);
                } else if (fieldType.equals(ParameterizedTypeName.get(Set.class, String.class))) {
                    putMethodBuilder.addStatement("$T.putStringSet(getEditor(), $S, value)", SharedPreferencesWrapper.class, fieldName);
                } else {
                    continue;
                }
                MethodSpec putMethod = putMethodBuilder
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(parameter)
                        .addStatement("return this")
                        .returns(generatedClassName)
                        .build();
                putMethods.add(putMethod);
            }
        }

        // removeメソッドの定義
        List<MethodSpec> removeMethods = new ArrayList<>();
        for (Element el : element.getEnclosedElements()) {
            if (el.getAnnotation(PrefKey.class) != null) {
                TypeName fieldType = ClassName.get(el.asType());
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
                    String fieldName = el.getSimpleName().toString();
                    MethodSpec removeMethod = MethodSpec.methodBuilder("remove" + StringUtils.capitalize(fieldName))
                            .addModifiers(Modifier.PUBLIC)
                            .addStatement("getEditor().remove($S)", fieldName)
                            .addStatement("return this")
                            .returns(generatedClassName)
                            .build();
                    removeMethods.add(removeMethod);
                }
            }
        }

        // クラスの定義
        TypeSpec generatedClass = TypeSpec.classBuilder(generatedClassName)
                .superclass(BasePrefManager.class)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructor)
                .addMethod(getPrefNameMethod)
                .addMethods(getMethods)
                .addMethods(putMethods)
                .addMethods(removeMethods)
                .build();

        // ファイルへ出力
        JavaFile.builder(packageName, generatedClass)
                .build()
                .writeTo(filer);
    }

}