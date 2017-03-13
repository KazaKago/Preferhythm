package com.kazakago.preferhythm.constants;

import com.squareup.javapoet.ClassName;

/**
 * Type Constants
 *
 * Created by Kensuke on 2017/03/14.
 */
public class Types {

    public static final ClassName Context = ClassName.get("android.content", "Context");

    public static final ClassName BasePreferhythm = ClassName.get("com.kazakago.preferhythm", "BasePreferhythm");

    public static final ClassName NonNull = ClassName.get("android.support.annotation", "NonNull");

    public static final ClassName Nullable = ClassName.get("android.support.annotation", "Nullable");

}
