package com.svalero.supermarketscan.presenter;

import com.svalero.supermarketscan.contract.LoginContract;
import com.svalero.supermarketscan.domain.login.LoginRequest;
import com.svalero.supermarketscan.domain.login.LoginResponse;
import com.svalero.supermarketscan.model.LoginModel;
import com.svalero.supermarketscan.view.LoginView;

public class LoginPresenter implements LoginContract.Presenter, LoginContract.Model.OnLoginListener {

    private LoginContract.Model model;
    private LoginContract.View view;

    public LoginPresenter(LoginView view) {
        model = new LoginModel();
        model.startDb(view.getApplicationContext());

        this.view = view;
    }

    @Override
    public void loginRequest(LoginRequest loginRequest) {
        model.loginRequest(this, loginRequest);
    }

    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {
        view.loginResponse(loginResponse);
    }

    @Override
    public void onLoginError(int message) {
        view.showMessage(message);
    }
}
