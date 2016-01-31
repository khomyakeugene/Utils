package com.company.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by Yevgen on 10.01.2016.
 */
public class SelfDescribingObjectService {
    public static final String METHOD_IS_NOT_PRESENTED_IN_CLASS_PATTERN = "Method <{0}> is not presented in class <{1}>";
    public static final String FIELD_IS_NOT_PRESENTED_IN_CLASS_PATTERN = "Field <{0}> is not presented in class <{1}>";
    public static final String ACCESS_DENIED_TO_THE_FIELD_IN_CLASS_PATTERN = "Access denied to the field <{0}> in class <{1}>";
    public static final String ACCESS_DENIED_TO_THE_METHOD_IN_CLASS_PATTERN = "Access denied to the method <{0}> in class <{1}>";

    public static final String GET_PREFIX = "get";
    public static final String SET_PREFIX = "set";

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

    public static String checkWhetherPublicFieldIsPresented(Object object, String fieldName) {
        return checkWhetherFieldIsPresented(object, fieldName, true);
    }

    public static String checkWhetherMethodWithoutArgumentsIsPresented(Object object, String methodName, boolean onlyPublic) {
        Class cls = object.getClass();

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

    public static String checkWhetherPublicMethodWithoutArgumentsIsPresented(Object object, String methodName) {
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
            int index = Utils.getIndexInStringArray(fieldNameList, propertyName, true);
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
                    index = Utils.getIndexInStringArray(gettersNameList, propertyName, true);
                    if (index == -1) {
                        // Construct "synthetic" getter name
                        propertyName = GET_PREFIX + propertyName;
                        // Find "synthetic" getter
                        index = Utils.getIndexInStringArray(gettersNameList, propertyName, true);
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

    public static Method searchOneDoubleArgumentMethod(String className, String methodName, boolean onlyPublic) {
        Method method = null;

        try {
            Class cls = Class.forName(className);
            Class[] parameterTypes = new Class[1];
            parameterTypes[0] = double.class;

            try {
                method = onlyPublic ? cls.getMethod(methodName, parameterTypes) :
                        cls.getDeclaredMethod(methodName, parameterTypes);
            } catch (NullPointerException | SecurityException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return method;
    }

    public static Method searchOneDoubleArgumentPublicMethod(String className, String methodName) {
        return searchOneDoubleArgumentMethod(className, methodName, true);
    }


    public static double invokeOneDoubleArgumentStaticMethod(String className, String methodName, double argument) {
        return invokeOneDoubleArgumentStaticMethod(searchOneDoubleArgumentPublicMethod(className, methodName), argument);
    }

    public static double invokeOneDoubleArgumentStaticMethod(Method method, double argument) {
        double result = Double.NaN;

        try {
            result = Double.parseDouble(method.invoke(null, argument).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
