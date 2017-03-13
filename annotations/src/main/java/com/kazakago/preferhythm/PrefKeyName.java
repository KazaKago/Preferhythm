package com.kazakago.preferhythm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation of Preferences ModelField.
 * this can change key name.
 *
 * Created by KazaKago on 2017/03/08.
 */
@Target(ElementType.FIELD)
public @interface PrefKeyName {
    String value();
}