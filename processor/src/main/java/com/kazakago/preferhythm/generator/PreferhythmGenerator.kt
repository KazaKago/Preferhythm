package com.kazakago.preferhythm.generator

import com.kazakago.preferhythm.PrefField
import com.kazakago.preferhythm.PrefKeyName
import com.kazakago.preferhythm.constants.Annotations
import com.kazakago.preferhythm.constants.Types
import com.kazakago.preferhythm.utils.AnnotationUtils
import com.squareup.javapoet.*
import org.apache.commons.lang3.StringUtils
import java.io.IOException
import java.util.*
import javax.annotation.processing.Filer
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.util.Elements

/**
 * Preferences management class generator.
 *
 * Created by KazaKago on 2017/03/11.
 */
class PreferhythmGenerator(filer: Filer, elements: Elements) : CodeGenerator(filer, elements) {

    @Throws(IOException::class)
    override fun execute(element: Element) {
        val packageName = elements.getPackageOf(element).qualifiedName.toString()
        val modelClassName = ClassName.get(packageName, element.simpleName.toString())
        val generatedClassName = ClassName.get(packageName, element.simpleName.toString() + "Manager")

        val modelField = createModelField(modelClassName)
        val constructor = createConstructor()
        val getPrefNameMethod = createGetPrefNameMethod(modelClassName)
        val getMethods = createGetMethods(element)
        val getIsNullMethods = createGetIsNullMethods(element)
        val setMethods = createSetMethods(element, generatedClassName)
        val setIsNullMethods = createSetIsNullMethods(element, generatedClassName)
        val removeMethods = createRemoveMethods(element, generatedClassName)

        val generatedClass = TypeSpec.classBuilder(generatedClassName)
                .superclass(Types.BasePreferhythm)
                .addModifiers(Modifier.PUBLIC)
                .addField(modelField)
                .addMethod(constructor)
                .addMethod(getPrefNameMethod)
                .addMethods(getMethods)
                .addMethods(getIsNullMethods)
                .addMethods(setMethods)
                .addMethods(setIsNullMethods)
                .addMethods(removeMethods)
                .build()

        JavaFile.builder(packageName, generatedClass)
                .build()
                .writeTo(filer)
    }

    private fun createModelField(modelClassName: ClassName): FieldSpec {
        return FieldSpec.builder(modelClassName, "modelInstance")
                .addModifiers(Modifier.PRIVATE)
                .initializer("new \$T()", modelClassName)
                .build()
    }

    private fun createConstructor(): MethodSpec {
        val contextParameter = ParameterSpec.builder(Types.Context, "context")
                .addAnnotation(Annotations.NonNull)
                .build()
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextParameter)
                .addStatement("super(context)")
                .build()
    }

    private fun createGetPrefNameMethod(modelClassName: ClassName): MethodSpec {
        return MethodSpec.methodBuilder("getSharedPreferencesName")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Annotations.NonNull)
                .addAnnotation(Override::class.java)
                .addStatement("return \$S", modelClassName.simpleName())
                .returns(String::class.java)
                .build()
    }

    private fun createGetMethods(element: Element): List<MethodSpec> {
        val getMethods = ArrayList<MethodSpec>()
        for (el in element.enclosedElements) {
            if (el.getAnnotation(PrefField::class.java) != null) {
                var fieldType = ClassName.get(el.asType())
                val methodNameParam: String
                val defaultValue: String
                if (fieldType == TypeName.INT) {
                    fieldType = TypeName.INT
                    methodNameParam = "Int"
                    defaultValue = "0"
                } else if (fieldType == TypeName.INT.box()) {
                    fieldType = TypeName.INT.box()
                    methodNameParam = "Int"
                    defaultValue = "0"
                } else if (fieldType == TypeName.LONG) {
                    fieldType = TypeName.LONG
                    methodNameParam = "Long"
                    defaultValue = "0"
                } else if (fieldType == TypeName.LONG.box()) {
                    fieldType = TypeName.LONG.box()
                    methodNameParam = "Long"
                    defaultValue = "0"
                } else if (fieldType == TypeName.FLOAT) {
                    fieldType = TypeName.FLOAT
                    methodNameParam = "Float"
                    defaultValue = "0"
                } else if (fieldType == TypeName.FLOAT.box()) {
                    fieldType = TypeName.FLOAT.box()
                    methodNameParam = "Float"
                    defaultValue = "0"
                } else if (fieldType == TypeName.BOOLEAN) {
                    fieldType = TypeName.BOOLEAN
                    methodNameParam = "Boolean"
                    defaultValue = "false"
                } else if (fieldType == TypeName.BOOLEAN.box()) {
                    fieldType = TypeName.BOOLEAN.box()
                    methodNameParam = "Boolean"
                    defaultValue = "false"
                } else if (fieldType == TypeName.get(String::class.java)) {
                    fieldType = TypeName.get(String::class.java)
                    methodNameParam = "String"
                    defaultValue = "null"
                } else if (fieldType.javaClass.isInstance(ParameterizedTypeName.get(Set::class.java, String::class.java))) {
                    fieldType = ParameterizedTypeName.get(Set::class.java, String::class.java)
                    methodNameParam = "StringSet"
                    defaultValue = "null"
                } else {
                    continue
                }
                val fieldName = el.simpleName.toString()
                val prefKeyNameAnnotation = el.getAnnotation(PrefKeyName::class.java)
                val prefKeyName = prefKeyNameAnnotation?.value ?: fieldName
                val fieldAccessor: String
                if (el.modifiers.contains(Modifier.PRIVATE)) {
                    fieldAccessor = String.format("get%s()", StringUtils.capitalize(fieldName))
                } else {
                    fieldAccessor = fieldName
                }
                val getMethodBuilder = MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName))
                if (fieldType.isPrimitive) {
                    getMethodBuilder.addStatement("\$T value = getSharedPreferences().get\$L(\$S, modelInstance.\$L)", fieldType, methodNameParam, prefKeyName, fieldAccessor)
                            .addStatement("return value")
                } else {
                    getMethodBuilder.beginControlFlow("if (getSharedPreferences().contains(\$S))", prefKeyName)
                            .addStatement("return getSharedPreferences().get\$L(\$S, \$L)", methodNameParam, prefKeyName, defaultValue)
                            .nextControlFlow("else")
                            .beginControlFlow("if (get\$LIsNull())", StringUtils.capitalize(fieldName))
                            .addStatement("return null")
                            .nextControlFlow("else")
                            .addStatement("return modelInstance.\$L", fieldAccessor)
                            .endControlFlow()
                            .endControlFlow()
                    if (AnnotationUtils.hasNonNullAnnotation(el)) {
                        getMethodBuilder.addAnnotation(Annotations.NonNull)
                    } else {
                        getMethodBuilder.addAnnotation(Annotations.Nullable)
                    }
                }
                val getMethod = getMethodBuilder
                        .addModifiers(Modifier.PUBLIC)
                        .returns(fieldType)
                        .build()
                getMethods.add(getMethod)
            }
        }
        return getMethods
    }

    private fun createGetIsNullMethods(element: Element): List<MethodSpec> {
        val getIsNullMethods = ArrayList<MethodSpec>()
        for (el in element.enclosedElements) {
            if (el.getAnnotation(PrefField::class.java) != null) {
                val fieldName = el.simpleName.toString()
                val fieldType = ClassName.get(el.asType())
                val prefKeyNameAnnotation = el.getAnnotation(PrefKeyName::class.java)
                val prefKeyName = prefKeyNameAnnotation?.value ?: fieldName
                val getIsNullMethodBuilder = MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName) + "IsNull")
                if ((fieldType == TypeName.INT.box() ||
                        fieldType == TypeName.LONG.box() ||
                        fieldType == TypeName.FLOAT.box() ||
                        fieldType == TypeName.BOOLEAN.box() ||
                        fieldType == TypeName.get(String::class.java) ||
                        fieldType.javaClass.isInstance(ParameterizedTypeName.get(Set::class.java, String::class.java))) && !fieldType.isPrimitive) {
                    getIsNullMethodBuilder.addStatement("return getSharedPreferences().getBoolean(\$S, \$L)", prefKeyName + "IsNull", false)
                } else {
                    continue
                }
                val getIsNullMethod = getIsNullMethodBuilder
                        .addModifiers(Modifier.PRIVATE)
                        .returns(Boolean::class.javaPrimitiveType!!)
                        .build()
                getIsNullMethods.add(getIsNullMethod)
            }
        }
        return getIsNullMethods
    }

    private fun createSetMethods(element: Element, generatedClassName: ClassName): List<MethodSpec> {
        val setMethods = ArrayList<MethodSpec>()
        for (el in element.enclosedElements) {
            if (el.getAnnotation(PrefField::class.java) != null) {
                var fieldType = ClassName.get(el.asType())
                val methodNameParam: String
                if (fieldType == TypeName.INT) {
                    fieldType = TypeName.INT
                    methodNameParam = "Int"
                } else if (fieldType == TypeName.INT.box()) {
                    fieldType = TypeName.INT.box()
                    methodNameParam = "Int"
                } else if (fieldType == TypeName.LONG) {
                    fieldType = TypeName.LONG
                    methodNameParam = "Long"
                } else if (fieldType == TypeName.LONG.box()) {
                    fieldType = TypeName.LONG.box()
                    methodNameParam = "Long"
                } else if (fieldType == TypeName.FLOAT) {
                    fieldType = TypeName.FLOAT
                    methodNameParam = "Float"
                } else if (fieldType == TypeName.FLOAT.box()) {
                    fieldType = TypeName.FLOAT.box()
                    methodNameParam = "Float"
                } else if (fieldType == TypeName.BOOLEAN) {
                    fieldType = TypeName.BOOLEAN
                    methodNameParam = "Boolean"
                } else if (fieldType == TypeName.BOOLEAN.box()) {
                    fieldType = TypeName.BOOLEAN.box()
                    methodNameParam = "Boolean"
                } else if (fieldType == TypeName.get(String::class.java)) {
                    fieldType = TypeName.get(String::class.java)
                    methodNameParam = "String"
                } else if (fieldType.javaClass.isInstance(ParameterizedTypeName.get(Set::class.java, String::class.java))) {
                    fieldType = ParameterizedTypeName.get(Set::class.java, String::class.java)
                    methodNameParam = "StringSet"
                } else {
                    continue
                }
                val fieldName = el.simpleName.toString()
                val prefKeyNameAnnotation = el.getAnnotation(PrefKeyName::class.java)
                val prefKeyName = prefKeyNameAnnotation?.value ?: fieldName
                val setMethodBuilder = MethodSpec.methodBuilder("set" + StringUtils.capitalize(fieldName))
                val valueParameterBuilder = ParameterSpec.builder(fieldType, "value")
                if (fieldType.isPrimitive) {
                    setMethodBuilder.addStatement("getSharedPreferencesEditor().put\$L(\$S, value)", methodNameParam, prefKeyName)
                } else {
                    setMethodBuilder.beginControlFlow("if (value != null)")
                            .addStatement("getSharedPreferencesEditor().put\$L(\$S, value)", methodNameParam, prefKeyName)
                            .addStatement("set\$LIsNull(false)", StringUtils.capitalize(fieldName))
                            .nextControlFlow("else")
                            .addStatement("getSharedPreferencesEditor().remove(\$S)", prefKeyName)
                            .addStatement("set\$LIsNull(true)", StringUtils.capitalize(fieldName))
                            .endControlFlow()
                    if (AnnotationUtils.hasNonNullAnnotation(el)) {
                        valueParameterBuilder.addAnnotation(Annotations.NonNull)
                    } else {
                        valueParameterBuilder.addAnnotation(Annotations.Nullable)
                    }
                }
                val parameter = valueParameterBuilder.build()
                val setMethod = setMethodBuilder
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Annotations.NonNull)
                        .addParameter(parameter)
                        .addStatement("return this")
                        .returns(generatedClassName)
                        .build()
                setMethods.add(setMethod)
            }
        }
        return setMethods
    }

    private fun createSetIsNullMethods(element: Element, generatedClassName: ClassName): List<MethodSpec> {
        val setIsNullMethods = ArrayList<MethodSpec>()
        for (el in element.enclosedElements) {
            if (el.getAnnotation(PrefField::class.java) != null) {
                val fieldName = el.simpleName.toString()
                val fieldType = ClassName.get(el.asType())
                val prefKeyNameAnnotation = el.getAnnotation(PrefKeyName::class.java)
                val prefKeyName = prefKeyNameAnnotation?.value ?: fieldName
                val setMethodBuilder = MethodSpec.methodBuilder("set" + StringUtils.capitalize(fieldName) + "IsNull")
                if ((fieldType == TypeName.INT.box() ||
                        fieldType == TypeName.LONG.box() ||
                        fieldType == TypeName.FLOAT.box() ||
                        fieldType == TypeName.BOOLEAN.box() ||
                        fieldType == TypeName.get(String::class.java) ||
                        fieldType.javaClass.isInstance(ParameterizedTypeName.get(Set::class.java, String::class.java))) && !fieldType.isPrimitive) {
                    setMethodBuilder.addStatement("getSharedPreferencesEditor().putBoolean(\$S, value)", prefKeyName + "IsNull")
                } else {
                    continue
                }
                val setIsNullMethod = setMethodBuilder
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(Annotations.NonNull)
                        .addParameter(Boolean::class.javaPrimitiveType, "value")
                        .addStatement("return this")
                        .returns(generatedClassName)
                        .build()
                setIsNullMethods.add(setIsNullMethod)
            }
        }
        return setIsNullMethods
    }

    private fun createRemoveMethods(element: Element, generatedClassName: ClassName): List<MethodSpec> {
        val removeMethods = ArrayList<MethodSpec>()
        for (el in element.enclosedElements) {
            if (el.getAnnotation(PrefField::class.java) != null) {
                val fieldName = el.simpleName.toString()
                val fieldType = ClassName.get(el.asType())
                val prefKeyNameAnnotation = el.getAnnotation(PrefKeyName::class.java)
                val prefKeyName = prefKeyNameAnnotation?.value ?: fieldName
                val removeMethodBuilder = MethodSpec.methodBuilder("remove" + StringUtils.capitalize(fieldName))
                if (fieldType == TypeName.INT ||
                        fieldType == TypeName.LONG ||
                        fieldType == TypeName.FLOAT ||
                        fieldType == TypeName.BOOLEAN ||
                        fieldType == TypeName.INT.box() ||
                        fieldType == TypeName.LONG.box() ||
                        fieldType == TypeName.FLOAT.box() ||
                        fieldType == TypeName.BOOLEAN.box() ||
                        fieldType == TypeName.get(String::class.java) ||
                        fieldType.javaClass.isInstance(ParameterizedTypeName.get(Set::class.java, String::class.java))) {
                    removeMethodBuilder.addStatement("getSharedPreferencesEditor().remove(\$S)", prefKeyName)
                    removeMethodBuilder.addStatement("getSharedPreferencesEditor().remove(\$S)", prefKeyName + "IsNull")
                } else {
                    continue
                }
                val removeMethod = removeMethodBuilder.addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Annotations.NonNull)
                        .addStatement("return this")
                        .returns(generatedClassName)
                        .build()
                removeMethods.add(removeMethod)
            }
        }
        return removeMethods
    }

}
