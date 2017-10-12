package com.kazakago.preferhythm.generator

import java.io.IOException

import javax.annotation.processing.Filer
import javax.lang.model.element.Element
import javax.lang.model.util.Elements

/**
 * Code Generator Abstract Class
 *
 * Created by tamura_k on 2017/05/08.
 */
abstract class CodeGenerator(var filer: Filer, var elements: Elements) {

    @Throws(IOException::class)
    abstract fun execute(element: Element)

}
