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

}