package com.application.norgevz.myticketon.global;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.application.norgevz.myticketon.R;
import com.application.norgevz.myticketon.models.User;

/**
 * Created by norgevz on 12/9/2016.
 */

public class MyApplication extends Application {

    SharedPreferences sharedPref;

    SharedPreferences.Editor editor;

    private static MyApplication CurrentApplication;

    public User loggedUser;

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public SharedPreferences getSharedPref() {
        return sharedPref;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        editor = sharedPref.edit();

        CurrentApplication = this;
    }

    public static MyApplication getInstance(){
        return CurrentApplication;
    }

    public static Context getAppContext(){
        return CurrentApplication.getApplicationContext();
    }





}
