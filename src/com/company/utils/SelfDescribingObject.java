package com.company.utils;

/**
 * Created by Yevgen on 06.01.2016.
 */
public class SelfDescribingObject {
    public String[] getPublicMethodNameList(String methodPrefix) {
        return ObjectService.getPublicMethodNameList(this, methodPrefix);
    }

    public String[] getPublicFieldNameList() {
        return ObjectService.getPublicFieldNameList(this);
    }

    public String[] getGettersNameList() {
        return ObjectService.getGettersNameList(this);
    }

    public String[] getSettersNameList() {
        return ObjectService.getSettersNameList(this);
    }

    public String checkWhetherFieldIsPresented(String fieldName, boolean onlyPublic) {
        return ObjectService.checkWhetherFieldIsPresented(this, fieldName, onlyPublic);
    }

    public String checkWhetherPublicFieldIsPresented(String fieldName) {
        return ObjectService.checkWhetherFieldIsPresented(this, fieldName, true);
    }

    private String checkWhetherMethodWithoutArgumentsIsPresented(String methodName, boolean onlyPublic) {
        return ObjectService.checkWhetherMethodWithoutArgumentsIsPresented(this, methodName, onlyPublic);
    }

    public String checkWhetherPublicMethodWithoutArgumentsIsPresented(String methodName) {
        return checkWhetherMethodWithoutArgumentsIsPresented(methodName, true);
    }

    public ObjectProperty checkProperty(String propertyName) {
        return ObjectService.checkProperty(this, propertyName);
    }
}
