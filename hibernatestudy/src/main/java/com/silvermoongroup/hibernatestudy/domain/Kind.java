package com.silvermoongroup.hibernatestudy.domain;

/**
 * Created by koen on 25.08.17.
 */
public class Kind {

    private Long id;
    private String kindName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    @Override
    public String toString() {
        return "Kind{" +
                "id=" + id +
                ", kindName='" + kindName + '\'' +
                '}';
    }
}
