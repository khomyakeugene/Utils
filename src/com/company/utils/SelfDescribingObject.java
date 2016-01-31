package com.company.utils;

/**
 * Created by Yevgen on 06.01.2016.
 */
public class SelfDescribingObject {
    public String[] getPublicMethodNameList(String methodPrefix) {
        return SelfDescribingObjectService.getPublicMethodNameList(this, methodPrefix);
    }

    public String[] getPublicFieldNameList() {
        return SelfDescribingObjectService.getPublicFieldNameList(this);
    }

    public String[] getGettersNameList() {
        return SelfDescribingObjectService.getGettersNameList(this);
    }

    public String[] getSettersNameList() {
        return SelfDescribingObjectService.getSettersNameList(this);
    }

    public String checkWhetherFieldIsPresented(String fieldName, boolean onlyPublic) {
        return SelfDescribingObjectService.checkWhetherFieldIsPresented(this, fieldName, onlyPublic);
    }

    public String checkWhetherPublicFieldIsPresented(String fieldName) {
        return SelfDescribingObjectService.checkWhetherFieldIsPresented(this, fieldName, true);
    }

    public String checkWhetherMethodWithoutArgumentsIsPresented(String methodName, boolean onlyPublic) {
        return SelfDescribingObjectService.checkWhetherMethodWithoutArgumentsIsPresented(this, methodName, onlyPublic);
    }

    public String checkWhetherPublicMethodWithoutArgumentsIsPresented(String methodName) {
        return checkWhetherMethodWithoutArgumentsIsPresented(methodName, true);
    }

    public ObjectProperty checkProperty(String propertyName) {
        return SelfDescribingObjectService.checkProperty(this, propertyName);
    }
}
