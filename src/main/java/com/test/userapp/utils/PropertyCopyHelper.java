package com.test.userapp.utils;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PropertyCopyHelper {

    @SneakyThrows
    public static <T> void copyNonNullProperties(T from, T to) {
        if (!from.getClass().equals(to.getClass())) {
            throw new IllegalAccessException("Can't copy properties for objects "
                    + "with different classes: from " + from.getClass()
                    + " to " + to.getClass());
        }
        final List<Field> fields = getAllClassFields(from.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            final Object fieldValue = field.get(from);
            if (fieldValue != null) {
                field.set(to, fieldValue);
            }
        }
    }

    private static List<Field> getAllClassFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        do {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return fields;
    }
}
