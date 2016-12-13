package com.application.norgevz.myticketon;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.application.norgevz.myticketon.network.Credentials;
import com.application.norgevz.myticketon.settings.Settings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements Authenticator.OnLogin {

    //TODO Show a loading when logging in

    @ViewById(R.id.email_edit_text)
    public EditText emailTextView;

    @ViewById(R.id.password_text_view)
    public EditText passwordTextView;

    @ViewById(R.id.invalid_credentials_text_view)
    public TextView invalidCredentialsTextView;

    @ViewById(R.id.text_view)
    public TextView tx;

    private Authenticator authenticator;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @AfterViews
    public void init() {

        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF9800, android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.GONE);

        setUpComponents();
        setTextViewFont();

        authenticator.setListener(this);
    }

    private void setUpComponents() {
        authenticator = new Authenticator(Authenticator.myKey);
    }

    void setTextViewFont() {

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/GothamLight.otf");
        tx.setTypeface(custom_font);
        invalidCredentialsTextView.setTypeface(custom_font);
    }


    public void onLogInButtonClicked(View view) {
        invalidCredentialsTextView.setVisibility(View.INVISIBLE);

        String line = String.valueOf(emailTextView.getText());
        String password = String.valueOf(passwordTextView.getText());

        if (Credentials.isValidCredentialsFormat(line, password)) {
            Credentials myCredentials = Credentials.getCredentials(line, password);

            progressBar.setVisibility(View.VISIBLE);
            authenticator.Validate(myCredentials);

        } else {
            failLogin("Invalid Credentials Format");
        }
    }

    public void onRegisterButtonClicked(View view) {
        Intent getRegisterScreenIntent = new Intent(this, RegisterScreen_.class);
        startActivity(getRegisterScreenIntent);
    }

    public void onSettingsButtonClicked(View view) {
        Intent getSettingsScreenIntent = new Intent(this, SettingsScreen_.class);
        startActivity(getSettingsScreenIntent);
    }

    @Override
    public void OnResult(boolean value, Credentials credentials) {

        if (!value)
            failLogin("Invalid Credentials");
        else {
            registerCredentials(credentials);
            startActivity(new Intent(this, DashboardScreen.class));
            progressBar.setVisibility(View.GONE);
            // TODO remove this activity from queue;
        }
    }

    @UiThread
    public void failLogin(String text) {
        progressBar.setVisibility(View.GONE);
        invalidCredentialsTextView.setText(text);
        invalidCredentialsTextView.setVisibility(View.VISIBLE);
    }


    public void registerCredentials(Credentials credentials) {
        Settings.addSetting("providerId", credentials.providerId);
        Settings.addSetting("user", credentials.email);
    }

}
