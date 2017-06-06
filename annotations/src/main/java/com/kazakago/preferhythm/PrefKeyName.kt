package com.kazakago.preferhythm

/**
 * Annotation of Preferences ModelField.
 * this can change key name.

 * Created by KazaKago on 2017/03/08.
 */
@Target(AnnotationTarget.FIELD)
annotation class PrefKeyName(val value: String)