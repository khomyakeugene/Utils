package com.company.utils;

/**
 * Created by Yevgen on 07.01.2016.
 */
public class ObjectProperty {
    enum PropertyType {field, method}

    private String sourcePropertyName;
    PropertyType propertyType = PropertyType.field;
    private String realPropertyName;
    private String errorMessage = "";

    public ObjectProperty(String sourcePropertyName, PropertyType propertyType, String realPropertyName) {
        super();

        this.sourcePropertyName = sourcePropertyName;
        this.propertyType = propertyType;
        this.realPropertyName = realPropertyName;
    }

    public ObjectProperty(String sourcePropertyName, PropertyType propertyType) {
        this(sourcePropertyName,propertyType, null);
    }

    public ObjectProperty(String sourcePropertyName) {
        this(sourcePropertyName, PropertyType.field);
    }

    public String getSourcePropertyName() {
        return sourcePropertyName;
    }

    public void setSourcePropertyName(String sourcePropertyName) {
        this.sourcePropertyName = sourcePropertyName;
    }

    public String getRealPropertyName() {
        return realPropertyName;
    }

    public void setRealPropertyName(String realPropertyName) {
        this.realPropertyName = realPropertyName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }
}
