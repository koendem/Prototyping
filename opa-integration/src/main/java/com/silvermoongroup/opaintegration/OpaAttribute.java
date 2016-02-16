package com.silvermoongroup.opaintegration;

/**
 * Created by koen on 16.02.16.
 */
public class OpaAttribute {

    String name;
    String value;
    OpaAttributeType valueType;

    public OpaAttribute(String name) {
        this.name = name;
    }

    public OpaAttribute(String name, String value, OpaAttributeType valueType) {
        this.name = name;
        this.value = value;
        this.valueType = valueType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public OpaAttributeType getValueType() {
        return valueType;
    }

    public void setValueType(OpaAttributeType valueType) {
        this.valueType = valueType;
    }
}
