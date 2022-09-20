package com.test.userapp.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PropertyCopyHelper {
    public static <T> void copyNonNullProperties(T from, T to) {
        final List<Field> fields = getAllClassFields(from.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                final Object fieldValue = field.get(from);
                if (fieldValue != null) {
                    field.set(to, fieldValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage());
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
