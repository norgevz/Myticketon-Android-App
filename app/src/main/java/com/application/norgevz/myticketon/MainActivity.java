package com.application.norgevz.myticketon;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.application.norgevz.myticketon.rest.Credentials;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements Authenticator.OnLogin {


    @ViewById(R.id.email_edit_text)
    public EditText emailTextView;

    @ViewById(R.id.password_text_view)
    public EditText passwordTextView;

    @ViewById(R.id.invalid_credentials_text_view)
    public TextView invalidCredentialsTextView;

    @ViewById(R.id.text_view)
    public TextView tx;

    private Authenticator authenticator;

    @AfterViews
    public void init(){
        setUpComponents();
        setTextViewFont();

        authenticator.setListener(this);
    }

    private void setUpComponents() {
        authenticator = new Authenticator(Authenticator.myKey);
    }

    void setTextViewFont(){

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/GothamLight.otf");
        tx.setTypeface(custom_font);
        invalidCredentialsTextView.setTypeface(custom_font);
    }



    public void onLogInButtonClicked(View view) {
        invalidCredentialsTextView.setVisibility(View.INVISIBLE);

        String line = String.valueOf(emailTextView.getText());
        String password = String.valueOf(passwordTextView.getText());

        if(Credentials.isValidCredentialsFormat(line, password)){
            Credentials myCredentials = Credentials.getCredentials(line , password);

            authenticator.Validate(myCredentials, this);

        }else{
            invalidCredentialsTextView.setText("Invalid Credentials Format");
            invalidCredentialsTextView.setVisibility(View.VISIBLE);

        }
    }

    public void onRegisterButtonClicked(View view) {
        Intent getRegisterScreenIntent = new Intent(this , RegisterScreen_.class);
        startActivity(getRegisterScreenIntent);
    }

    public void onSettingsButtonClicked(View view) {
        Intent getSettingsScreenIntent = new Intent(this , SettingsScreen_.class);
        startActivity(getSettingsScreenIntent);
    }

    @Override
    public void OnResult(boolean value) {
        if(!value)
            failLogin("Invalid Credentials");
        else{
            // todo enter to main
        }
    }

    @UiThread
    public void failLogin(String text){
        invalidCredentialsTextView.setText(text);
        invalidCredentialsTextView.setVisibility(View.VISIBLE);
    }

}