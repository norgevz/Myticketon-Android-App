package com.application.norgevz.myticketon.global;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.application.norgevz.myticketon.R;

/**
 * Created by norgevz on 12/9/2016.
 */

public class GlobalState extends Application {

    SharedPreferences sharedPref;

    SharedPreferences.Editor editor;

    public static Application CurrentApplication;

    public static Application getCurrentApplication() {
        return CurrentApplication;
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


}
