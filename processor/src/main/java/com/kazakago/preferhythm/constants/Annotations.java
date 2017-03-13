package com.kazakago.preferhythm.constants;

import com.squareup.javapoet.AnnotationSpec;

/**
 * Annotation Constants
 *
 * Created by KazaKago on 2017/03/14.
 */
public class Annotations {

    public static final AnnotationSpec NonNull = AnnotationSpec.builder(Types.NonNull).build();

    public static final AnnotationSpec Nullable = AnnotationSpec.builder(Types.Nullable).build();

}
