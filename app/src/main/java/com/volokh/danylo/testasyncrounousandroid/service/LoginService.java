package com.volokh.danylo.testasyncrounousandroid.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.volokh.danylo.testasyncrounousandroid.internet_repsonses.LoginResponse;

/**
 * This Service simulates Http call for login. And sends a mock result as a broadcast.
 */
public class LoginService extends IntentService {

    public static final String LOGIN_ACTION = "LOGIN_ACTION";
    public static final String RESPONSE_BROADCAST_FILTER = "RESPONSE_BROADCAST_FILTER";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LoginService(String name) {
        super(name);
    }

    public LoginService() {
        super(LoginService.class.getSimpleName());
    }

    public static void performAsyncLogin(Context context, String responseBroadcastFiler, String login, String password){
        Intent loginIntent = new Intent(context, LoginService.class);
        loginIntent.setAction(LOGIN_ACTION);
        loginIntent.putExtra(RESPONSE_BROADCAST_FILTER, responseBroadcastFiler);
        context.startService(loginIntent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        switch (intent.getAction()){
            case LOGIN_ACTION:
                performLogin(intent);
                break;
            default:
                throw new RuntimeException("not handled action[" + intent.getAction() + "]");
        }
    }

    private void performLogin(Intent intent) {
        try {
            /**
             * Simulate performing login for 3 seconds
             */
            Thread.sleep(3000);

            sendLoginResponce(true, intent.getStringExtra(RESPONSE_BROADCAST_FILTER));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendLoginResponce(boolean loggedInSuccessfully, String responseBroadcastFiler) {
        Intent loginResponce = new Intent();
        LoginResponse.putLoginResponse(loginResponce, loggedInSuccessfully);
        loginResponce.setAction(responseBroadcastFiler);
        sendBroadcast(loginResponce);
    }
}
