package com.silvermoongroup.opaintegration;

import com.oracle.opa.AssessRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.xml.transform.StringResult;

/**
 * Created by koen on 13.02.16.
 * This is based on: http://docs.spring.io/spring-ws/site/reference/html/client.html
 */
public class SpringOpaAssessmentIntegration {

    public static String contextRoot = "/lunos_rules/determinations-server/assess/soap/generic/12.2.1/computerloss_rules";

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        WebServiceTemplate wsTemplate = (WebServiceTemplate) context.getBean("webServiceTemplate");
        wsTemplate.setDefaultUri("http://192.168.10.103:7003" + contextRoot);


        Wss4jSecurityInterceptor securityInterceptor = context.getBean(Wss4jSecurityInterceptor.class);
        securityInterceptor.setSecurementUsername("system");
        securityInterceptor.setSecurementPassword("123456");
        securityInterceptor.setSecurementActions("UsernameToken");


        System.out.println("wsTemplate : " + wsTemplate);

        AssessRequest request = new OpaAssessRequestBuilder().constructOpaAssessRequest();

        StringResult result = (StringResult) wsTemplate.marshalSendAndReceive(request);
        System.out.println(result);


    }



}
