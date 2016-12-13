package com.application.norgevz.myticketon.network;

/**
 * Created by norgevz on 12/8/2016.
 */
public class Credentials{

    public String email;
    public String providerId;
    public String password;

    public Credentials( String providerId, String email, String password) {
        this.email = email;
        this.password = password;
        this.providerId = providerId;
    }

    public static boolean isValidCredentialsFormat(String line, String password){
        if(!line.contains("\\"))return false;

        int divider = line.indexOf("\\");

        String providerId = line.substring(0 , divider);
        String email = line.substring(divider + 1);

        if(providerId.length() == 0 || email.length() == 0 || password.length() == 0){
            return false;
        }

        System.out.println(providerId);
        System.out.println(email);
        return true;
    }

    public static Credentials getCredentials(String line, String password){
        int divider = line.indexOf("\\");
        String providerId = line.substring(0 , divider);
        String email = line.substring(divider + 1);
        return new Credentials(providerId, email, password);
    }

}