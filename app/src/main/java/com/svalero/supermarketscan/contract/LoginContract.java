package com.svalero.supermarketscan.contract;

import android.content.Context;

import com.svalero.supermarketscan.domain.login.LoginRequest;
import com.svalero.supermarketscan.domain.login.LoginResponse;

public interface LoginContract {
    interface Model {
        interface OnLoginListener {
            void onLoginSuccess(LoginResponse loginResponse);

            void onLoginError(int message);
        }

        void startDb(Context context);

        void loginRequest(OnLoginListener listener, LoginRequest loginRequest);
    }

    interface View {
        void loginResponse(LoginResponse loginResponse);

        void showMessage(int message);
    }

    interface Presenter {
        void loginRequest(LoginRequest loginRequest);
    }
}
