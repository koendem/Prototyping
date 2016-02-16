package com.silvermoongroup.opaintegration;

import com.oracle.opa.AssessRequest;
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
import org.springframework.xml.transform.StringResult;

import java.io.IOException;


/**
 * Created by koen on 11.02.16.
 *
 *
 */
public class OpaAssessmentIntegration {

    public static String contextRoot = "/lunos_rules/determinations-server/assess/soap/generic/12.2.1/computerloss_rules";
    public static String endpointURL = "http://192.168.10.25:7003/lunos_rules/determinations-server/assess/soap/generic/12.2.1/computerloss_rules";
    public static String host;
    public static int port;

    public static void main(String[] args){
        System.out.println("args.length = " + args.length);
        if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);

            populateEndpointURL(host, port);
            System.out.println("Using the following endpointUrl :\n"+endpointURL);
        } else {
            System.out.println("Provide the ip address and port as the first and the second argument respectively. \n" +
                    "Example:\n" +
                    OpaAssessmentIntegration.class.getSimpleName() + " 192.168.10.25 7003");
            return;
        }

        AssessRequest request = new OpaAssessRequestBuilder().constructOpaAssessRequest();

        //--------now call the webservice.
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.oracle.opa");

        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.afterPropertiesSet();
        WebServiceTemplate template = new WebServiceTemplate(messageFactory);
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        template.setMessageSender(messageSender());

        WebServiceMessageCallback credentialsWSMessageCallBack = new CredentialsWSMessageCallBack("username","password");

        template.setDefaultUri(endpointURL);
        StringResult result = (StringResult) template.marshalSendAndReceive(request, credentialsWSMessageCallBack);
        System.out.println(result);


    }

    private static class ContentLengthHeaderRemover implements HttpRequestInterceptor{

        @Override
        public void process(HttpRequest request, HttpContext context)
                throws HttpException, IOException {

            // fighting org.apache.http.protocol.RequestContent's
            // ProtocolException("Content-Length header already present");
            request.removeHeaders(HTTP.CONTENT_LEN);
        }
    }

    public static HttpComponentsMessageSender messageSender() {

        RequestConfig requestConfig = RequestConfig.custom()
                .setAuthenticationEnabled(true)
                .build();

        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        HttpClient httpClient = httpClientBuilder
                .addInterceptorFirst(new ContentLengthHeaderRemover())
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCredentialsProvider(credentialsProvider())
                .build();

        HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(httpClient);
        return messageSender;
    }

    public static CredentialsProvider credentialsProvider() {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin","password");
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(host, port), credentials);  //, AuthScope.ANY_REALM, "basic")
        return credentialsProvider;
    }

    private static void populateEndpointURL(String ipAddress, int port) {
        endpointURL = "http://"+ipAddress+":"+port+contextRoot;
    }
}
