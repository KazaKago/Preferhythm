package com.kazakago.quickpref;

import android.content.Context;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
@SupportedAnnotationTypes({"com.kazakago.quickpref.PrefName", "com.kazakago.quickpref.PrefKey"})
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
                .addStatement("prefName = $S", modelClassName.simpleName())
                .build();

        // putメソッドの定義
        List<MethodSpec> putMethods = new ArrayList<>();
        for (Element el : element.getEnclosedElements()) {
            if (el.getAnnotation(PrefKey.class) != null) {
                String fieldName = el.getSimpleName().toString();
                TypeName fieldType = ClassName.get(el.asType());
                MethodSpec.Builder putMethodBuilder = MethodSpec.methodBuilder("put" + StringUtils.capitalize(fieldName))
                        .addParameter(fieldType, "value")
                        .addModifiers(Modifier.PUBLIC);
                if (fieldType.equals(TypeName.INT)) {
                    putMethodBuilder.addStatement("editor.putInt($S, value)", fieldName);
                } else if (fieldType.equals(TypeName.get(Integer.class))) {
                    putMethodBuilder.addStatement("$T.putInteger(getEditor(), $S, value)", SharedPreferencesWrapper.class, fieldName);
                } else if (fieldType.equals(TypeName.get(String.class))) {
                    putMethodBuilder.addStatement("$T.putString(getEditor(), $S, value)", SharedPreferencesWrapper.class, fieldName);
                }
                MethodSpec putMethod = putMethodBuilder
                        .addStatement("return this")
                        .returns(generatedClassName)
                        .build();
                putMethods.add(putMethod);
            }
        }

        // クラスの定義
        TypeSpec generatedClass = TypeSpec.classBuilder(generatedClassName)
                .superclass(BasePrefManager.class)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructor)
                .addMethods(putMethods)
                .build();

        // ファイルへ出力
        JavaFile.builder(packageName, generatedClass)
                .build()
                .writeTo(filer);
    }

}