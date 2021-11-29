package com.example.bancoapp_mejorado.ActivityPrincipales;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enviar usuario a LoginActivity tan pronto como se cargue esta actividad
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        // eliminar esta actividad de la pila
        finish();
    }
}
