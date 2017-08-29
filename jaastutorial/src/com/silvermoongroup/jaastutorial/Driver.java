package com.silvermoongroup.jaastutorial;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * Created by koen on 19.03.17.
 */
public class Driver {

    public static void main(String[] args) throws LoginException {

        System.setProperty("java.security.auth.login.config", "jaastutorial.config");
        LoginContext loginContext = new LoginContext("ZaJaasTutorial", new ZaCallbackHandler());

        while (true){
            loginContext.login();
            System.out.println("You logged in successfully !!!!!");
            System.exit(0);
        }

    }
}
