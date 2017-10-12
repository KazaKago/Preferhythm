package com.kazakago.preferhythm.constants

import com.squareup.javapoet.ClassName

/**
 * Type Constants
 *
 * Created by Kensuke on 2017/03/14.
 */
class Types {

    companion object {
        @JvmStatic
        val Context = ClassName.get("android.content", "Context")!!

        @JvmStatic
        val NonNull = ClassName.get("android.support.annotation", "NonNull")!!

        @JvmStatic
        val Nullable = ClassName.get("android.support.annotation", "Nullable")!!

        @JvmStatic
        val BasePreferhythm = ClassName.get("com.kazakago.preferhythm", "BasePreferhythm")!!
    }

}
