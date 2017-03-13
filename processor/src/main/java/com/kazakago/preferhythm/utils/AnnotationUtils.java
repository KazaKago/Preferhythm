package com.kazakago.preferhythm.utils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

/**
 * Annotation related utility methods.
 *
 * Created by KazaKago on 2017/03/14.
 */
public class AnnotationUtils {

    public static boolean hasNullableAnnotation(Element element) {
        for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
            // allow anything named "Nullable"
            if (annotation.getAnnotationType().asElement().getSimpleName().contentEquals("Nullable")) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasNonNullAnnotation(Element element) {
        for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
            // allow anything named "NonNull"
            if (annotation.getAnnotationType().asElement().getSimpleName().contentEquals("NonNull") ||
                    annotation.getAnnotationType().asElement().getSimpleName().contentEquals("Nonnull") ||
                    annotation.getAnnotationType().asElement().getSimpleName().contentEquals("NotNull") ||
                    annotation.getAnnotationType().asElement().getSimpleName().contentEquals("Notnull")) {
                return true;
            }
        }
        return false;
    }

}
