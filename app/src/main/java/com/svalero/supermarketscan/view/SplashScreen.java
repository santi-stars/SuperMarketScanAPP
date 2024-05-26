package com.svalero.supermarketscan.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.svalero.supermarketscan.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ;
                startActivity(new Intent(SplashScreen.this, LoginView.class));
                finish();
            }
        }, 4200);

        fullScreen();
    }

    private void fullScreen() {
        // Consume las áreas de las barras de estado y navegación.
        getWindow().getDecorView().setOnApplyWindowInsetsListener((v, insets) -> {
            return insets.consumeSystemWindowInsets();
        });
        // Esta línea hace que la ventana de la Activity se extienda detrás de las barras del sistema
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        // Oculta la barra de navegación y la barra de estado con el modo inmersivo pegajoso
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
}