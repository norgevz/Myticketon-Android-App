package com.example.norgevz.myticketon;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

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

    public void init(){

    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.settings_layout);
//    }

    public void onSaveSettingsButtonClicked(View view) {
        saveSettings();
        finish();
    }

    private void saveSettings() {
        System.out.println(String.valueOf(entryPointTextView.getText()));
    }

}
