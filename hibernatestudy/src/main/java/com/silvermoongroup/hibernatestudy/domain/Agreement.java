package com.silvermoongroup.hibernatestudy.domain;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by koen on 25.08.17.
 */
public class Agreement {

    private Long id;

    private String name;

    private Set<Property> properties = new HashSet<Property>();

    private Request originRequest;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

    public Request getOriginRequest() {
        return originRequest;
    }

    public void setOriginRequest(Request originRequest) {
        this.originRequest = originRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agreement)) return false;

        Agreement agreement = (Agreement) o;

        return getName() != null ? getName().equals(agreement.getName()) : agreement.getName() == null;

    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Agreement{ id=");
        sb.append( id );
        sb.append( ", name='");
        sb.append( name);
        sb.append("', \n\tproperties= [");
        for(Property property : getProperties()){
            sb.append("\n\t\t");
            sb.append(property.toString());
        }
        sb.append( "\n\t\t] \n}");

        return sb.toString();
    }

    public void addProperty(Property property) {
        property.setAgreement(this);
        getProperties().add(property);
    }
}
