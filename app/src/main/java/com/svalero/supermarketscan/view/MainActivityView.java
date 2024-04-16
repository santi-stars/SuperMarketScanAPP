package com.svalero.supermarketscan.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.svalero.supermarketscan.R;

public class MainActivityView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void calculadoraCompras(View view) {
        Intent intent = new Intent(this, CalculadoraCompraView.class);
        startActivity(intent);
    }
}