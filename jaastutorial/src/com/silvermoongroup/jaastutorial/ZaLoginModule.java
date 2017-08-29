package com.silvermoongroup.jaastutorial;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 * Created by koen on 19.03.17.
 */
public class ZaLoginModule implements LoginModule {


    public static final String TEST_USER = "user";
    public static final String TEST_PASSWORD = "password";

    private CallbackHandler callbackHandler;
    private boolean authenticationSuccessFlag = false;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
            Map<String, ?> options) {

        this.callbackHandler = callbackHandler;

    }

    @Override
    public boolean login() throws LoginException {
        Callback callbackArray[] = new Callback[2];
        callbackArray[0] = new NameCallback("username:");
        callbackArray[1] = new PasswordCallback("password:",false);

        try {
            callbackHandler.handle(callbackArray);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedCallbackException e) {
            e.printStackTrace();
        }
        String name = ((NameCallback)callbackArray[0]).getName();
        String password = new String(((PasswordCallback)callbackArray[1]).getPassword());

        if ((TEST_USER.equals(name)) && (TEST_PASSWORD.equals(password))){
            System.out.println("Authentication successful !");
            authenticationSuccessFlag = true;
        } else {
            authenticationSuccessFlag = false;
            throw new FailedLoginException("Authentication failed");
        }

        return authenticationSuccessFlag;
    }

    @Override
    public boolean commit() throws LoginException {
        return false;
    }

    @Override
    public boolean abort() throws LoginException {
        return false;
    }

    @Override
    public boolean logout() throws LoginException {
        return false;
    }
}
