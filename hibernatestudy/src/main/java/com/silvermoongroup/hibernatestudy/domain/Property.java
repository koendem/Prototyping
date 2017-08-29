package com.silvermoongroup.hibernatestudy.domain;

/**
 * Created by koen on 25.08.17.
 */
public class Property {

    private Long id;

    private String value;

    private Kind kind;

    private Agreement agreement;

    private Request request;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", kind=" + kind +
                '}';
    }
}
