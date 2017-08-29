package com.silvermoongroup.hibernatestudy.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by koen on 28.08.17.
 */
public class Request {

    private Long id;
    private String name;

    private Set<Property> properties = new HashSet<Property>();

    private Agreement resultActual;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

    public Agreement getResultActual() {
        return resultActual;
    }

    public void setResultActual(Agreement resultActual) {
        this.resultActual = resultActual;
    }

    public void addProperty(Property property) {
        property.setRequest(this);
        getProperties().add(property);
    }
}
