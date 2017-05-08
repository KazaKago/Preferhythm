package com.kazakago.preferhythm.generator;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

/**
 * Created by tamura_k on 2017/05/08.
 */
abstract class CodeGenerator {

    Filer filer;
    Elements elements;

    CodeGenerator(Filer filer, Elements elements) {
        this.filer = filer;
        this.elements = elements;
    }

    public abstract void execute(Element element) throws IOException;

}
