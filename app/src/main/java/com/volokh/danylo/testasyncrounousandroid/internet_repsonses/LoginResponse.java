package com.volokh.danylo.testasyncrounousandroid.internet_repsonses;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class LoginResponse implements Parcelable {

    private static final String LOGIN_RESPONSE_KEY = "LOGIN_RESPONSE_KEY";
    public final boolean isLoggedIn;

    public LoginResponse(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    protected LoginResponse(Parcel in) {
        isLoggedIn = in.readByte() != 0;
    }

    public static final Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel in) {
            return new LoginResponse(in);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isLoggedIn ? 1 : 0));
    }

    public static void putLoginResponse(Intent intent, boolean isLoggedIn){
        intent.putExtra(LOGIN_RESPONSE_KEY, new LoginResponse(isLoggedIn));
    }

    public static LoginResponse getLoginResponse(Intent intent){
        return intent.getParcelableExtra(LOGIN_RESPONSE_KEY);
    }
}
