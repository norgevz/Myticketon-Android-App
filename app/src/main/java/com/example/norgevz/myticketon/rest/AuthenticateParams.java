package com.example.norgevz.myticketon.rest;

import android.content.Context;

/**
 * Created by norgevz on 12/8/2016.
 */

public class AuthenticateParams{

    public Context context;

    public Credentials credentials;

    public AuthenticateParams(Context context, Credentials credentials) {
        this.context = context;
        this.credentials = credentials;
    }
}