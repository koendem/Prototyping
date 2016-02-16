package com.silvermoongroup.opaintegration;

import com.oracle.opa.AssessRequest;
import com.oracle.opa.AssessResponse;
import com.oracle.opa.AttributeType;
import com.oracle.opa.ObjectFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import javax.xml.bind.JAXBElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by koen on 11.02.16.
 *
 *
 */
public class OpaAssessmentIntegration {

    public static String contextRoot = "/lunos_rules/determinations-server/assess/soap/generic/12.2.1/";

    private String  opaServer;
    private int     opaServerPort;
    private String  ruleBase;


    public OpaAssessmentIntegration(String opaServer, int opaServerPort, String ruleBase) {
        this.opaServer = opaServer;
        this.opaServerPort = opaServerPort;
        this.ruleBase = ruleBase;
    }



    public AttributeType runOpaRules(OpaAttribute unknown, List<OpaAttribute> inputValues) {

        //---- construct payload.
        AssessRequest request = new OpaAssessRequestBuilder().constructOpaAssessRequest(unknown, inputValues);
        JAXBElement<AssessRequest> jaxbRequest = new ObjectFactory().createAssessRequest(request);

        //---- call the opa webservice.
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.oracle.opa");

        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.afterPropertiesSet();
        WebServiceTemplate template = new WebServiceTemplate(messageFactory);
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        template.setMessageSender(messageSender());

        WebServiceMessageCallback credentialsWSMessageCallBack = new CredentialsWSMessageCallBack("admin","password");

        template.setDefaultUri(getEndpointURL());
        JAXBElement<AssessResponse> jaxbReponse = (JAXBElement<AssessResponse>) template.marshalSendAndReceive(jaxbRequest, credentialsWSMessageCallBack);
        AssessResponse assessResponse = jaxbReponse.getValue();

        AttributeType result=null;

        List<AttributeType> attributes = assessResponse.getGlobalInstance().getAttribute();
        for(AttributeType attribute : attributes){
            if (attribute.getId().equals(unknown.getName())){
                return attribute;
            }
        }
        return result;
    }

    private static class ContentLengthHeaderRemover implements HttpRequestInterceptor{

        @Override
        public void process(HttpRequest request, HttpContext context)
                throws HttpException, IOException {

            // ProtocolException("Content-Length header already present");
            request.removeHeaders(HTTP.CONTENT_LEN);
        }
    }

    public HttpComponentsMessageSender messageSender() {

        HttpClient httpClient = HttpClients.custom()
                .addInterceptorFirst(new ContentLengthHeaderRemover())
                .build();

        return new HttpComponentsMessageSender(httpClient);
    }

    private String getEndpointURL() {
        return "http://"+getOpaServer() + ":" + getOpaServerPort() + getContextRoot() + getRuleBase();
    }

    public static String getContextRoot() {
        return contextRoot;
    }

    public String getOpaServer() {
        return opaServer;
    }

    public int getOpaServerPort() {
        return opaServerPort;
    }

    public String getRuleBase() {
        return ruleBase;
    }
}
