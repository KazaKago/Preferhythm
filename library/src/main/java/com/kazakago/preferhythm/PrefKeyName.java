package com.kazakago.preferhythm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by tamura_k on 2017/03/08.
 */
@Target(ElementType.FIELD)
public @interface PrefKeyName {
    String value();
}