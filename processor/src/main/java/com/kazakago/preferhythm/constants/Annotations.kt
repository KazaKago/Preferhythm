package com.kazakago.preferhythm.constants

import com.squareup.javapoet.AnnotationSpec

/**
 * Annotation Constants
 *
 * Created by KazaKago on 2017/03/14.
 */
class Annotations {

    companion object {
        @JvmStatic
        val NonNull = AnnotationSpec.builder(Types.NonNull).build()!!

        @JvmStatic
        val Nullable = AnnotationSpec.builder(Types.Nullable).build()!!
    }

}
