package com.example.norgevz.myticketon;

import android.graphics.Typeface;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.norgevz.myticketon.rest.Credentials;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {


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

            if (authenticator.Validate(myCredentials, this)){

            }
            else{
                invalidCredentialsTextView.setText("Invalid Credentials");
                invalidCredentialsTextView.setVisibility(View.VISIBLE);
            }
        }else{
            invalidCredentialsTextView.setText("Invalid Credentials Format");
            invalidCredentialsTextView.setVisibility(View.VISIBLE);

        }
    }

    public void onRegisterButtonClicked(View view) {

    }
}
