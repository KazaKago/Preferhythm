package com.kazakago.preferhythm;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Created by tamura_k on 2017/03/08.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.kazakago.preferhythm.PrefClass", "com.kazakago.preferhythm.PrefField", "com.kazakago.preferhythm.PrefKeyName"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class PreferhythmProcessor extends AbstractProcessor {

    private Messager messager;
    private PreferhythmGenerator generator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        generator = new PreferhythmGenerator(processingEnv.getFiler(), processingEnv.getElementUtils());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Class<PrefClass> prefClass = PrefClass.class;
        for (Element element : roundEnv.getElementsAnnotatedWith(prefClass)) {
            ElementKind kind = element.getKind();
            if (kind == ElementKind.CLASS) {
                try {
                    generator.execute(element);
                } catch (IOException e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "IO error");
                }
            } else {
                messager.printMessage(Diagnostic.Kind.ERROR, "Type error");
            }
        }
        return true;
    }

}