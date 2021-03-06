package com.company.util.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Yevgen on 10.01.2016.
 */
public class ObjectService {
    private static final String METHOD_IS_NOT_PRESENTED_IN_CLASS_PATTERN = "Method <{0}> is not presented in class <{1}>";
    private static final String FIELD_IS_NOT_PRESENTED_IN_CLASS_PATTERN = "Field <{0}> is not presented in class <{1}>";
    private static final String ACCESS_DENIED_TO_THE_FIELD_IN_CLASS_PATTERN = "Access denied to the field <{0}> in class <{1}>";
    private static final String ACCESS_DENIED_TO_THE_METHOD_IN_CLASS_PATTERN = "Access denied to the method <{0}> in class <{1}>";
    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";

    public static String[] getPublicMethodNameList(Object object, String methodPrefix) {
        Method[] methods = object.getClass().getMethods();
        ArrayList<String> nameArrayList = new ArrayList<>();

        boolean notPrefixCheck = (methodPrefix == null) || methodPrefix.isEmpty();

        for (Method method: methods) {
            String name = method.getName();
            if (notPrefixCheck || name.indexOf(methodPrefix) == 0) {
                nameArrayList.add(name);
            }
        }

        String[] methodNameList = new String[nameArrayList.size()];
        nameArrayList.toArray(methodNameList);

        return methodNameList;
    }

    public static String[] getPublicFieldNameList(Object object) {
        Field[] fields = object.getClass().getFields();
        ArrayList<String> nameArrayList = new ArrayList<>();

        for (Field field: fields) {
            nameArrayList.add(field.getName());
        }

        String[] methodNameList = new String[nameArrayList.size()];
        nameArrayList.toArray(methodNameList);

        return methodNameList;
    }

    public static String[] getGettersNameList(Object object) {
        return getPublicMethodNameList(object, GET_PREFIX);
    }

    public static String[] getSettersNameList(Object object) {
        return getPublicMethodNameList(object, SET_PREFIX);
    }

    public static String checkWhetherFieldIsPresented(Object object, String fieldName, boolean onlyPublic) {
        Class cls = object.getClass();

        String errorMessage = "";
        try {
            // Just check if field "fieldName" presents
            if (onlyPublic) {
                cls.getField(fieldName);
            } else {
                cls.getDeclaredField(fieldName);
            }
        } catch (SecurityException e) {
            errorMessage = MessageFormat.format(ACCESS_DENIED_TO_THE_FIELD_IN_CLASS_PATTERN, fieldName, cls.getName());
        } catch (NoSuchFieldException e) {
            errorMessage = MessageFormat.format(FIELD_IS_NOT_PRESENTED_IN_CLASS_PATTERN, fieldName, cls.getName());
        }

        return errorMessage;
    }

    private static String checkWhetherPublicFieldIsPresented(Object object, String fieldName) {
        return checkWhetherFieldIsPresented(object, fieldName, true);
    }

    public static String checkWhetherMethodWithoutArgumentsIsPresented(Object object, String methodName, boolean onlyPublic) {
        Class<?> cls = object.getClass();

        String errorMessage = "";
        try {
            // Just check if field "methodName" presents
            if (onlyPublic) {
                cls.getMethod(methodName);
            } else {
                cls.getDeclaredMethod(methodName);
            }
        } catch (SecurityException e) {
            errorMessage = MessageFormat.format(ACCESS_DENIED_TO_THE_METHOD_IN_CLASS_PATTERN, methodName, cls.getName());
        } catch (NoSuchMethodException e) {
            errorMessage = MessageFormat.format(METHOD_IS_NOT_PRESENTED_IN_CLASS_PATTERN, methodName, cls.getName());
        }

        return errorMessage;
    }

    private static String checkWhetherPublicMethodWithoutArgumentsIsPresented(Object object, String methodName) {
        return checkWhetherMethodWithoutArgumentsIsPresented(object,methodName, true);
    }

    public static ObjectProperty checkProperty(Object object, String propertyName) {
        // Firstly think that a property is a field
        ObjectProperty objectProperty = new ObjectProperty(propertyName, ObjectProperty.PropertyType.field, propertyName);

        // Firstly - check if this "propertyName" is presented as a pubic field
        String errorMessage = checkWhetherPublicFieldIsPresented(object, propertyName);
        if (!errorMessage.isEmpty()) {
            //  Try to find a field without case sensitive
            String[] fieldNameList = getPublicFieldNameList(object);
            int index = Util.getIndexInStringArray(fieldNameList, propertyName, true);
            if (index != -1) {
                objectProperty.setRealPropertyName(fieldNameList[index]);
                errorMessage = "";
            }

            // Need to find method?
            if (!errorMessage.isEmpty()) {
                objectProperty.setPropertyType(ObjectProperty.PropertyType.method);
                String nextErrorMessage = checkWhetherPublicMethodWithoutArgumentsIsPresented(object, propertyName);
                // Need to find Getter?
                if (nextErrorMessage.isEmpty()) {
                    errorMessage = "";
                } else {
                    // Let's try to find suitable getter
                    String[] gettersNameList = getGettersNameList(object);
                    index = Util.getIndexInStringArray(gettersNameList, propertyName, true);
                    if (index == -1) {
                        // Construct "synthetic" getter name
                        propertyName = GET_PREFIX + propertyName;
                        // Find "synthetic" getter
                        index = Util.getIndexInStringArray(gettersNameList, propertyName, true);
                    }
                    if (index != -1) {
                        objectProperty.setRealPropertyName(gettersNameList[index]);
                        errorMessage = "";
                    }
                }

                if (!errorMessage.isEmpty()) {
                    errorMessage = errorMessage + "\n" + nextErrorMessage;
                }
            }
        }

        // Fix "final" error message
        objectProperty.setErrorMessage(errorMessage);

        return objectProperty;
    }

    private static Method searchMethod(String className, String methodName, Class<?>[] parameterTypes, boolean onlyPublic) {
        Method method = null;
        if (methodName != null) {
            try {
                Class<?> cls = Class.forName(className);

                try {
                    method = onlyPublic ? cls.getMethod(methodName, parameterTypes) :
                            cls.getDeclaredMethod(methodName, parameterTypes);
                    if (method != null) {
                        method.setAccessible(true);
                    }
                } catch (NullPointerException | SecurityException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return method;
    }

    private static Method searchMethod(Object object, String methodName, Class[] parameterTypes, boolean onlyPublic) {
        return searchMethod(object.getClass().getName(), methodName, parameterTypes, onlyPublic);
    }

    public static Method searchPublicMethod(Object object, String methodName, Class[] parameterTypes) {
        return searchMethod(object, methodName, parameterTypes, true);
    }

    public static Object invokeMethod(Object object, Method method, Object... args) {
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Method searchOneDoubleArgumentMethod(String className, String methodName, boolean onlyPublic) {
        return searchMethod(className, methodName, new Class[]{double.class}, onlyPublic);
    }

    private static Method searchOneDoubleArgumentPublicMethod(String className, String methodName) {
        return searchOneDoubleArgumentMethod(className, methodName, true);
    }

    public static Method searchOneDoubleArgumentAnyMethod(String className, String methodName) {
        return searchOneDoubleArgumentMethod(className, methodName, false);
    }

    private static Method searchEmptyArgumentMethod(Object object, String methodName, boolean onlyPublic) {
        return searchMethod(object, methodName, new Class[]{}, onlyPublic);
    }

    public static Method searchEmptyArgumentPublicMethod(Object object, String methodName) {
        return searchEmptyArgumentMethod(object, methodName, true);
    }

    public static Method searchEmptyArgumentAnyMethod(Object object, String methodName) {
        return searchEmptyArgumentMethod(object, methodName, false);
    }

    private static Method searchOneObjectArgumentMethod(Object object, String methodName, boolean onlyPublic) {
        return searchMethod(object, methodName, new Class[]{Object.class}, onlyPublic);
    }

    public static Method searchOneObjectArgumentPublicMethod(Object object, String methodName) {
        return searchOneObjectArgumentMethod(object, methodName, true);
    }

    public static Method searchOneObjectArgumentAnyMethod(Object object, String methodName) {
        return searchOneObjectArgumentMethod(object, methodName, false);
    }

    private static Method searchOneIntArgumentMethod(Object object, String methodName, boolean onlyPublic) {
        return searchMethod(object, methodName, new Class[]{int.class}, onlyPublic);
    }

    public static Method searchOneIntArgumentPublicMethod(Object object, String methodName) {
        return searchOneIntArgumentMethod(object, methodName, true);
    }

    public static Method searchOneIntArgumentAnyMethod(Object object, String methodName) {
        return searchOneIntArgumentMethod(object, methodName, false);
    }

    private static Method searchOneIntAndOneObjectArgumentMethod(Object object, String methodName, boolean onlyPublic) {
        return searchMethod(object, methodName, new Class[]{int.class, Object.class}, onlyPublic);
    }

    public static Method searchOneIntAndOneObjectArgumentPublicMethod(Object object, String methodName) {
        return searchOneIntAndOneObjectArgumentMethod(object, methodName, true);
    }

    public static Method searchOneIntAndOneObjectArgumentAnyMethod(Object object, String methodName) {
        return searchOneIntAndOneObjectArgumentMethod(object, methodName, false);
    }

    private static double invokeOneDoubleArgumentStaticMethod(Method method, double argument) {
        double result = Double.NaN;

        try {
            result = Double.parseDouble(method.invoke(null, argument).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static double invokeOneDoubleArgumentStaticMethod(String className, String methodName, double argument) {
        return invokeOneDoubleArgumentStaticMethod(searchOneDoubleArgumentPublicMethod(className, methodName), argument);
    }

    private static Method searchPublicMethodWithoutParameters(Object object, String methodName) {
        return searchPublicMethod(object, methodName, new Class[]{});
    }

    private static Object invokePublicEmptyArgumentMethod(Object object, String methodName) {
        return invokeMethod(object, searchPublicMethodWithoutParameters(object, methodName));
    }

    public static boolean isEqualByGetterValuesStringRepresentation(Object object1, Object object2) {
        boolean result = object1.getClass().equals(object2.getClass());

        if (result) {
            for (String s : getGettersNameList(object1)) {
                Object methodResult1 = invokePublicEmptyArgumentMethod(object1, s);
                Object methodResult2 = invokePublicEmptyArgumentMethod(object2, s);

                result = ((methodResult1 == null) ? "" : methodResult1.toString()).
                        equals((methodResult1 == null) ? "" : methodResult2.toString());
                if (!result) {
                    break;
                }
            }
        }

        return result;
    }

    public static Object copyObjectByAccessors(Object source, Object target) {
        String[] sourceGetters = getGettersNameList(source);
        String[] targetSetters = getSettersNameList(target);

        Arrays.stream(sourceGetters).forEach(getter -> {
            String setterName = SET_PREFIX + getter.substring(GET_PREFIX.length());
            if (Arrays.stream(targetSetters).filter(s -> s.equals(setterName)).findFirst().isPresent()) {
                // Data type control
                try {
                    Method getterMethod = source.getClass().getMethod(getter);
                    Class returnType = getterMethod.getReturnType();
                    try {
                        Method setterMethod = target.getClass().getMethod(setterName, returnType);
                        if (setterMethod != null) {
                            try {
                                setterMethod.invoke(target, getterMethod.invoke(source));
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                // Theoretically, such exception should not be raised here
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        // Not all "analogs" of source.getters should be presented as target.setters

                    }
                } catch (NoSuchMethodException e) {
                    // Theoretically, such exception should not be raised here
                    throw new RuntimeException(e);
                }
            }
        });

        return target;
    }

    public static Field getDeclaredField(Class objectClass, String fieldName) {
        Field result = null;
        while((result == null) && (objectClass != null)) {
            try {
                result = objectClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                objectClass = objectClass.getSuperclass();
            }
        }

        return result;
    }
}
