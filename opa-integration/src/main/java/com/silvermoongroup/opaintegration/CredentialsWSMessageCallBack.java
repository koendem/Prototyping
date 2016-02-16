package com.silvermoongroup.opaintegration;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by koen on 16.02.16.
 */
public class CredentialsWSMessageCallBack implements WebServiceMessageCallback {

    private final String username;
    private final String password;

    public CredentialsWSMessageCallBack(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
        SOAPMessage soapMessage = ((SaajSoapMessage)message).getSaajMessage();

        SOAPHeader header = null;
        try {
            header = soapMessage.getSOAPHeader();
            SOAPHeaderElement security = header.addHeaderElement(new QName("http://schemas.xmlsoap.org/ws/2003/06/secext", "Security", "wsse"));
            SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
            SOAPElement username = usernameToken.addChildElement("Username", "wsse");
            SOAPElement password = usernameToken.addChildElement("Password", "wsse");

            //password.addAttribute(type, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
            username.setTextContent(getUsername());
            password.setTextContent(getPassword());
        } catch (SOAPException e) {
            throw new TransformerException(e.getMessage());
        }

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
