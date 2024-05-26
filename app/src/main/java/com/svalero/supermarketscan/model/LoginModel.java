package com.svalero.supermarketscan.model;

import android.content.Context;

import com.svalero.supermarketscan.R;
import com.svalero.supermarketscan.api.SmScanApi;
import com.svalero.supermarketscan.api.SmScanApiInterface;
import com.svalero.supermarketscan.contract.LoginContract;
import com.svalero.supermarketscan.domain.login.LoginRequest;
import com.svalero.supermarketscan.domain.login.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginModel implements LoginContract.Model {

    private SmScanApiInterface api;

    @Override
    public void startDb(Context context) {
        api = SmScanApi.buildInstance(context);
    }

    @Override
    public void loginRequest(OnLoginListener listener, LoginRequest loginRequest) {
        Call<LoginResponse> call = api.login(loginRequest);

        loginRequestCallEnqueue(listener, call);
    }

    private void loginRequestCallEnqueue(OnLoginListener listener, Call<LoginResponse> call) {
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    listener.onLoginSuccess(loginResponse);
                } else {
                    listener.onLoginError(R.string.login_error);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                listener.onLoginError(R.string.login_error);
                t.printStackTrace();
            }
        });
    }
}
