package com.kazakago.preferhythm.samplekotlin

import com.kazakago.preferhythm.PrefClass
import com.kazakago.preferhythm.PrefField

import java.util.Arrays
import java.util.HashSet

/**
 * Created by tamura_k on 2017/03/08.
 */
@PrefClass
class MyPreferences {

    @PrefField
    @JvmField
    val intObject: Int? = null
    @PrefField
    @JvmField
    val intObjectWithInit: Int? = 9
    @PrefField
    @JvmField
    val intObjectNonNull: Int = 12

    @PrefField
    @JvmField
    val longObject: Long? = null
    @PrefField
    @JvmField
    val longObjectWithInit: Long? = 15L
    @PrefField
    @JvmField
    val longObjectNonNull: Long = 12L

    @PrefField
    @JvmField
    val floatObject: Float? = null
    @PrefField
    @JvmField
    val floatObjectWithInit: Float? = 15.0f
    @PrefField
    @JvmField
    val floatObjectNonNull: Float = 12.0f

    @PrefField
    @JvmField
    val booleanObject: Boolean? = null
    @PrefField
    @JvmField
    val booleanObjectWithInit: Boolean? = true
    @PrefField
    @JvmField
    val booleanObjectNonNull: Boolean = true

    @PrefField
    @JvmField
    val stringObject: String? = null
    @PrefField
    @JvmField
    val stringObjectWithInit: String? = "foo"
    @PrefField
    @JvmField
    val stringObjectNonNull: String = "bar"

    @PrefField
    @JvmField
    val stringSetObject: Set<String>? = null
    @PrefField
    @JvmField
    val stringSetObjectWithInit: Set<String>? = HashSet(Arrays.asList("foo", "bar"))
    @PrefField
    @JvmField
    val stringSetObjectNonNull: Set<String> = HashSet(Arrays.asList("foo", "bar"))

}