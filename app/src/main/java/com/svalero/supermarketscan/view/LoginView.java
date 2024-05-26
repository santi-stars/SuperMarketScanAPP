package com.svalero.supermarketscan.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.svalero.supermarketscan.R;
import com.svalero.supermarketscan.contract.LoginContract;
import com.svalero.supermarketscan.domain.login.LoginRequest;
import com.svalero.supermarketscan.domain.login.LoginResponse;
import com.svalero.supermarketscan.presenter.LoginPresenter;

public class LoginView extends AppCompatActivity implements LoginContract.View {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        presenter = new LoginPresenter(this);

        editTextUsername = findViewById(R.id.edit_text_user_name);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonLogin = findViewById(R.id.init_button);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRequest();
            }
        });

        fullScreen();
    }

    private void fullScreen() {
        // Oculta la barra de navegación y la barra de estado con el modo inmersivo pegajoso
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void loginRequest() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Introduce usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(username, password);

        presenter.loginRequest(loginRequest);
    }

    @Override
    public void loginResponse(LoginResponse loginResponse) {
        saveToken(loginResponse.getToken());

        initSesion(null);
    }

    @Override
    public void showMessage(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("SmScanPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }

    //TODO: hacer privado y quitar onClick de la imagen
    public void initSesion(View view) {
        startActivity(new Intent(LoginView.this, MainActivityView.class));
        finish();
    }
}