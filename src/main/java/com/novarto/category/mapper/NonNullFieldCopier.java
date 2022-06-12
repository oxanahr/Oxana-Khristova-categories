package com.novarto.category.mapper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NonNullFieldCopier {
    public static <T> void copyNonNull(T to, T from) throws IllegalAccessException {
        if (!to.getClass().equals(from.getClass())) {
            throw new IllegalArgumentException(to.getClass() + " is of a different type than " + from.getClass());
        }
        final List<Field> fields = getAllModelFields(from.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            final Object fieldValue = field.get(from);
            if (fieldValue != null) {
                field.set(to, fieldValue);
            }
        }
    }

    private static List<Field> getAllModelFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        do {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return fields;
    }
}