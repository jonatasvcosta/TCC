package politcc2017.tcc_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.afollestad.materialdialogs.MaterialDialog;

import politcc2017.tcc_app.Components.CustomButton;
import politcc2017.tcc_app.Components.CustomEditText;
import politcc2017.tcc_app.Components.Helpers.DialogHelper;
import politcc2017.tcc_app.Components.Helpers.SharedPreferencesHelper;
import politcc2017.tcc_app.R;

/**
 * Created by Jonatas on 25/10/2016.
 */

public class LoginActivity extends AppCompatActivity {
    private CustomEditText emailEditText, passwordEditText;
    private MaterialDialog errorDialog;
    private CheckBox mCheckBox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);
        ActionBar bar = getSupportActionBar();
        if(bar != null) bar.hide();
        CustomButton loginButton = (CustomButton) findViewById(R.id.login_activity_login_button);
        emailEditText = (CustomEditText) findViewById(R.id.login_activity_email);
        passwordEditText = (CustomEditText) findViewById(R.id.login_activity_password);
        mCheckBox = (CheckBox) findViewById(R.id.login_activity_remain_conected_checkbox);

        Intent intent = getIntent();
        if(intent != null){
            if(intent.getStringExtra(SharedPreferencesHelper.EMAIL_KEY) != null) emailEditText.setText(intent.getStringExtra(SharedPreferencesHelper.EMAIL_KEY));
            if(intent.getStringExtra(SharedPreferencesHelper.PASSWORD_KEY) != null) passwordEditText.setText(intent.getStringExtra(SharedPreferencesHelper.PASSWORD_KEY));
        }
        errorDialog = DialogHelper.ErrorDialog(LoginActivity.this, R.drawable.ic_wrong, getResources().getString(R.string.login_activity_invalid_user_error), getResources().getString(R.string.dialog_confirm));
        SharedPreferencesHelper.Initialize(getApplicationContext());
        if(SharedPreferencesHelper.getBoolean(SharedPreferencesHelper.AUTOMATIC_AUTHENTICATION_KEY)) startHomeActivity();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateLogin()){
                    if(mCheckBox.isChecked()) SharedPreferencesHelper.addBoolean(SharedPreferencesHelper.AUTOMATIC_AUTHENTICATION_KEY, true);
                    startHomeActivity();
                }
                else errorDialog.show();
            }
        });
        CustomButton signupButton = (CustomButton) findViewById(R.id.login_activity_signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignupActivity();
            }
        });
    }

    protected boolean validateLogin(){
        if(SharedPreferencesHelper.getString(SharedPreferencesHelper.EMAIL_KEY).toLowerCase().trim().equals(emailEditText.getText().toLowerCase().trim()) &&
                SharedPreferencesHelper.getString(SharedPreferencesHelper.PASSWORD_KEY).toLowerCase().trim().equals(passwordEditText.getText().toLowerCase().trim())) return true;

        return false;
    }

    protected void startHomeActivity(){
        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
        finishAffinity();
        startActivity(intent);
    }

    protected void startSignupActivity(){
        Intent intent = new Intent(getBaseContext(), SignupActivity.class);
        startActivity(intent);
    }
}