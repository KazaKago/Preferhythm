package com.kazakago.preferhythm.samplekotlin

import com.kazakago.preferhythm.PrefClass
import com.kazakago.preferhythm.PrefField
import java.util.*

@PrefClass
class MyPreferences {

    @PrefField
    val intObject: Int? = null
    @PrefField
    val intObjectWithInit: Int? = 9
    @PrefField
    val intObjectNonNull: Int = 12

    @PrefField
    val longObject: Long? = null
    @PrefField
    val longObjectWithInit: Long? = 15L
    @PrefField
    val longObjectNonNull: Long = 12L

    @PrefField
    val floatObject: Float? = null
    @PrefField
    val floatObjectWithInit: Float? = 15.0f
    @PrefField
    val floatObjectNonNull: Float = 12.0f

    @PrefField
    val booleanObject: Boolean? = null
    @PrefField
    val booleanObjectWithInit: Boolean? = true
    @PrefField
    val booleanObjectNonNull: Boolean = true

    @PrefField
    val stringObject: String? = null
    @PrefField
    val stringObjectWithInit: String? = "foo"
    @PrefField
    val stringObjectNonNull: String = "bar"

    @PrefField
    val stringSetObject: Set<String>? = null
    @PrefField
    val stringSetObjectWithInit: Set<String>? = HashSet(Arrays.asList("foo", "bar"))
    @PrefField
    val stringSetObjectNonNull: Set<String> = HashSet(Arrays.asList("foo", "bar"))

}