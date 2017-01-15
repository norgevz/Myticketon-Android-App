package com.application.norgevz.myticketon;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.application.norgevz.myticketon.global.MyApplication;
import com.application.norgevz.myticketon.models.User;
import com.application.norgevz.myticketon.network.Credentials;
import com.application.norgevz.myticketon.network.DataResponse;
import com.application.norgevz.myticketon.settings.Settings;

import android.support.annotation.UiThread;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements Authenticator.OnLogin {


    @ViewById(R.id.provider_edit_text)
    public EditText providerTextView;

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
        authenticator = new Authenticator();
    }

    void setTextViewFont() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/GothamLight.otf");
        tx.setTypeface(custom_font);
        invalidCredentialsTextView.setTypeface(custom_font);
    }


    public void onLogInButtonClicked(View view) {
        invalidCredentialsTextView.setVisibility(View.INVISIBLE);

        String provider = String.valueOf(providerTextView.getText());
        String email = String.valueOf(emailTextView.getText());
        String password = String.valueOf(passwordTextView.getText());

        if (password.isEmpty() || email.isEmpty() || provider.isEmpty() ) {
            failLogin("Invalid Credentials");
        } else {
            Credentials myCredentials = new Credentials(provider, email, password);
            progressBar.setVisibility(View.VISIBLE);
            authenticator.Validate(myCredentials);
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
    @UiThread
    public void OnResult(DataResponse response, Credentials credentials) {
        boolean value = Boolean.valueOf(response.Succeeded);

        if (!value) {
            progressBar.setVisibility(View.GONE);
            invalidCredentialsTextView.setText("Invalid Credentials");
            invalidCredentialsTextView.setVisibility(View.VISIBLE);
        }
        else {

            registerCredentials(credentials, response.Data);
            startActivity(new Intent(this, DashboardScreen.class));
            progressBar.setVisibility(View.GONE);
        }
    }

    @UiThread
    public void failLogin(String message) {

        progressBar.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(message);
        AlertDialog alert1 = builder.create();
        alert1.show();
    }


    public void registerCredentials(Credentials credentials, User user) {
        MyApplication.getInstance().setLoggedUser(user);
        Settings.addSetting("providerId", credentials.providerId);
        Settings.addSetting("user", credentials.email);
    }


}
