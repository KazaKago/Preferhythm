package com.kazakago.preferhythm.utils

import javax.lang.model.element.Element

/**
 * Annotation related utility methods.

 * Created by KazaKago on 2017/03/14.
 */
class AnnotationUtils {

    companion object {
        @JvmStatic
        fun hasNullableAnnotation(element: Element): Boolean {
            return element.annotationMirrors.any {
                it.annotationType.asElement().simpleName.contentEquals("Nullable")
            }
        }

        @JvmStatic
        fun hasNonNullAnnotation(element: Element): Boolean {
            return element.annotationMirrors.any {
                it.annotationType.asElement().simpleName.contentEquals("NonNull") ||
                        it.annotationType.asElement().simpleName.contentEquals("Nonnull") ||
                        it.annotationType.asElement().simpleName.contentEquals("NotNull") ||
                        it.annotationType.asElement().simpleName.contentEquals("Notnull")
            }
        }
    }

}
