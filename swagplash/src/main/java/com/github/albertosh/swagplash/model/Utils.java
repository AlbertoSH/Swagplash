package com.github.albertosh.swagplash.model;

import com.github.albertosh.swagplash.annotations.ApiModel;
import scala.annotation.meta.field;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

public class Utils {

    private static ProcessingEnvironment processingEnv;

    public static void warning(String message) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.WARNING,
                message);
    }

    public static void warning(String message, Element element) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.WARNING,
                message,
                element);
    }

    public static void init(ProcessingEnvironment processingEnv) {
        Utils.processingEnv = processingEnv;
    }

    public static String getType(VariableElement field) {
        String declaredType = field.asType().toString();
        if (declaredType.endsWith("[]"))
            return "array";

        return getType(declaredType, field);
    }

    public static String getTypeOfArrayItems(VariableElement element) {
        String declaredType = element.asType().toString().replaceAll("\\[\\]","");
        return getType(declaredType, element);
    }

    public static String getType(String type, Element field) {
        String transformed = type.replaceAll(".+\\.", "").toLowerCase();
        System.out.println("getType: " + type);
        System.out.println("transformed: " + transformed);
        switch (transformed) {
            case "int":
            case "long":
                return "integer";
            case "string":
                return "string";
            default:
                warning("Unsupported type", field);
                return null;
        }
    }

    public static String getFormat(VariableElement field) {
        try {
            processingEnv.getTypeUtils().boxedClass((PrimitiveType) field.asType());
            return getFormat(field.asType().toString());
        } catch (Exception e) {
        }

        try {
            processingEnv.getTypeUtils().unboxedType(field.asType());
            return getFormat(field.asType().toString().toLowerCase());
        } catch (Exception e) {
        }

        return null;
    }

    public static String getFormat(String type) {
        switch (type) {
            case "int":
                return "int32";
            case "long":
                return "int64";
            default:
                return null;
        }
    }

    public static boolean isPrimitive(VariableElement field) {
        try {
            processingEnv.getTypeUtils().boxedClass((PrimitiveType) field.asType());
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean typeMirrorIsNotApiModel(TypeMirror tm) {
        boolean result = false;
        Element classElement = processingEnv.getTypeUtils().asElement(tm);
        if (classElementIsNotVoid(classElement)) {
            result = (classElement.getAnnotation(ApiModel.class) == null);
        }
        return result;
    }

    public static boolean typeMirrorIsNotVoid(TypeMirror tm) {
        Element classElement = processingEnv.getTypeUtils().asElement(tm);
        return (classElementIsNotVoid(classElement));
    }

    public static boolean classElementIsNotVoid(Element classElement) {
        return !classElement.asType().toString().equals(Void.class.getCanonicalName());
    }

    public static Object defaultValueWithTypeFormat(String defaultValue, String type, String format) {
        switch (type) {
            case "integer":
                switch (format) {
                    case "int32":
                        return Integer.valueOf(defaultValue);
                    case "int64":
                        return Long.valueOf(defaultValue);
                    default:
                        warning("Unkown combination of type integer with format: " + format);
                        return null;
                }
            case "string":
                return defaultValue;
            default:
                warning("Unkown combination of type-format: " + type + "-" + format);
                return null;
        }
    }
}
