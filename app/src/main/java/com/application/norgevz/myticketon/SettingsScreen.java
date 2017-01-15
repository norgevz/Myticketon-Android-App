package com.application.norgevz.myticketon;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.application.norgevz.myticketon.settings.Settings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by norgevz on 12/9/2016.
 */


@EActivity(R.layout.settings_layout)
public class SettingsScreen extends AppCompatActivity {

    @ViewById(R.id.entry_point_edit_view)
    EditText entryPointTextView;

    @ViewById(R.id.app_key_edit_view)
    EditText appKeyEditText;

    @AfterViews
    public void init(){

        String entryPoint = Settings.getEndpoint();

        if(entryPoint.length() > 0){
            entryPointTextView.setText(entryPoint);
        }

    }

    public void onSaveSettingsButtonClicked(View view) {
        saveSettings();
        finish();
    }

    private void saveSettings() {

        String entryPoint = entryPointTextView.getText().toString();
        String appKey = appKeyEditText.getText().toString();

        Settings.setEndpoint(entryPoint);

        if(appKey.length() > 0)
            Settings.setKey(appKey);
    }

}
