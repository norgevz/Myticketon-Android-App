package com.application.norgevz.myticketon.models;

/**
 * Created by norgevz on 1/15/2017.
 */

public class User {
    public String Email;
    public String Roles;

    public String[] getRoles(){
        return Roles.split(" ");
    }
}
