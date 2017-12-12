package com.kazakago.preferhythm

/**
 * Annotation of Preferences ModelClass.
 *
 * Created by KazaKago on 2017/03/08.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class PrefClass(val value: String = "")