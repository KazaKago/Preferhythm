package com.kazakago.preferhythm

/**
 * Annotation of Preferences ModelField.
 *
 * Created by KazaKago on 2017/03/08.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class PrefField(val value: String = "")