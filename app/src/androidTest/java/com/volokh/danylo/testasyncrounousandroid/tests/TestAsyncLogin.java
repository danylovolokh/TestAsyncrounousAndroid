package com.volokh.danylo.testasyncrounousandroid.tests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.volokh.danylo.testasyncronousandroid.activity.LoginTestActivity;
import com.volokh.danylo.testasyncrounousandroid.R;
import com.volokh.danylo.testasyncrounousandroid.internet_repsonses.LoginResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertTrue;

/**
 * This test is created for testing asynchronous login.
 *
 */
@RunWith(AndroidJUnit4.class)
public class TestAsyncLogin {

    private static final String TAG = TestAsyncLogin.class.getSimpleName();

    @Rule
    public ActivityTestRule<LoginTestActivity> mActivityRule = new ActivityTestRule<>(LoginTestActivity.class);

    /**
     * This method uses {@link LoginTestActivity} and does few things:
     *
     * 1. Writes "UserLogin" into login field
     * 2. Writes "UserPassword" into password field
     * 3. Presses "Perform login" button
     *
     * 4. Waits for {@link com.volokh.danylo.testasyncronousandroid.activity.LoginTestActivity.LoginTestCallback#onHandleResponseCalled(LoginResponse)}
     * and validates that user is logged in successfully.
     *
     * NOTE: It makes synchronization on local variable but in our case it doesn't matter.
     *
     * @throws Exception
     */
    @Test
    public void testLoginViaService() throws Exception {
        /** 1.*/
        onView(withId(R.id.login)).perform(typeText("UserLogin"));
        /** 2.*/
        onView(withId(R.id.password)).perform(typeText("UserPassword"));

        final Object syncObject = new Object();

        LoginTestActivity loginActivity = mActivityRule.getActivity();
        loginActivity.setLoginCallback(new LoginTestActivity.LoginTestCallback() {
            @Override
            public void onHandleResponseCalled(LoginResponse loginResponse) {
                Log.v(TAG, "onHandleResponseCalled in thread " + Thread.currentThread().getId());

                assertTrue(loginResponse.isLoggedIn);
                synchronized (syncObject){
                    syncObject.notify();
                }
            }
        });

        /** 3.*/
        onView(withId(R.id.perform_login)).perform(click());

        /** 4.
         * Here is a synchronization on local variable but it's ok.
         */
        synchronized (syncObject){
            syncObject.wait();
        }
    }
}