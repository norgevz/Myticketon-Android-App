package com.application.norgevz.myticketon.settings;

import com.application.norgevz.myticketon.global.GlobalState;

/**
 * Created by norgevz on 12/8/2016.
 */

public final class Settings {

    private static GlobalState state = ((GlobalState) GlobalState.getCurrentApplication());

    public static String getEndpoint() {
        return state.getSharedPref().getString("entry_point", "");
    }

    public static void setEndpoint(String _endpoint) {
        state.getEditor().putString("entry_point" , _endpoint);
        state.getEditor().commit();
    }

    public static String getKey(){
        return state.getSharedPref().getString("app_key", "");
    }

    public static void setKey(String _key) {
        state.getEditor().putString("app_key" , _key);
        state.getEditor().commit();
    }
}
