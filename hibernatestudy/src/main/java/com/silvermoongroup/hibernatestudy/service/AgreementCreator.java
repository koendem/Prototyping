package com.silvermoongroup.hibernatestudy.service;

import java.util.Date;

import org.hibernate.Session;

import com.silvermoongroup.hibernatestudy.domain.Agreement;
import com.silvermoongroup.hibernatestudy.domain.Kind;
import com.silvermoongroup.hibernatestudy.domain.Property;
import com.silvermoongroup.hibernatestudy.domain.Request;

/**
 * Created by koen on 25.08.17.
 */
public class AgreementCreator {

    private final Session hibernateSession;

    public AgreementCreator(Session hibernateSession) {
        this.hibernateSession = hibernateSession;
    }

    public Agreement createAgreement(){

        Agreement agmt = new Agreement();
        agmt.setName("Created at " + new Date());

        for(long i=-3; i<0; i++){
            Property property = createProperty(i);
            property.setValue(property.getKind().getKindName()+"-value");
            agmt.addProperty(property);
        }

        return agmt;
    }

    public Property createProperty(Long kindId){
        Kind propertyKind = (Kind) hibernateSession.get(Kind.class, kindId);
        System.out.println(propertyKind);
        Property property = new Property();
        property.setKind(propertyKind);
        return property;
    }

    public Request createRequest() {
        Request req = new Request();
        req.setName("Request created at : " + new Date());

        Property property = createProperty(-4l);
        property.setValue("value for property on request");
        req.addProperty(property);
        return req;
    }
}
