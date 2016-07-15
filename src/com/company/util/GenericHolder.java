package com.company.util;

import java.lang.reflect.ParameterizedType;

/**
 * Created by Yevhen on 13.07.2016.
 */
public class GenericHolder<T> {
    private Class<T> entityClass;

    private Class<T> getGenericClass() {
        return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).
                getActualTypeArguments()[0]);
    }

    private Class<T> getEntityClass() {
        if (entityClass == null) {
            entityClass = getGenericClass();
        }

        return entityClass;
    }

    protected String getEntityName() {
        return getEntityClass().getName();
    }

    protected T newObject() {
        Class<? extends T> entityClass = getEntityClass();

        T object;
        try {
            object = (T) Class.forName(entityClass.getName()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return object;
    }
}
