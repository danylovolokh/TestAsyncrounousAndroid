package com.volokh.danylo.testasyncrounousandroid.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.volokh.danylo.testasyncrounousandroid.service.LoginService;
import com.volokh.danylo.testasyncrounousandroid.R;
import com.volokh.danylo.testasyncrounousandroid.internet_repsonses.LoginResponse;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_RECEIVER_FILTER = "LOGIN_RECEIVER_FILTER";

    private TextView mLogin;
    private TextView mPassword;

    private BroadcastReceiver mLoginBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogin = (TextView) findViewById(R.id.login);
        mPassword = (TextView) findViewById(R.id.password);

        Button performLoginButton = (Button) findViewById(R.id.perform_login);
        performLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login = mLogin.getText().toString();
                String password = mPassword.getText().toString();

                performLoginViaService(login, password);
            }
        });

    }

    private void performLoginViaService(String login, String password) {
        mLoginBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver(this);
                mLoginBroadcastReceiver = null;

                LoginResponse loginResponse = LoginResponse.getLoginResponse(intent);
                handleLoginResponse(loginResponse);
            }
        };
        registerReceiver(mLoginBroadcastReceiver, new IntentFilter(LOGIN_RECEIVER_FILTER));
        LoginService.performAsyncLogin(this, LOGIN_RECEIVER_FILTER, login, password);

    }

    /**
     * This is a method that will be overridden in order to test response.
     *
     * @param loginResponse - a response provided by {@link LoginService}
     */
    @VisibleForTesting
    public void handleLoginResponse(LoginResponse loginResponse) {
        if(loginResponse.isLoggedIn){
            Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "User failed to login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mLoginBroadcastReceiver != null){
            unregisterReceiver(mLoginBroadcastReceiver);
            mLoginBroadcastReceiver = null;
        }
    }
}
