package com.oneness.fdxmerchant.Activity.EntryPoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.oneness.fdxmerchant.R;

public class Login extends AppCompatActivity {

    Button btnContinue;
    TextView tvEmailLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnContinue = findViewById(R.id.btn_continue);
        tvEmailLogin = findViewById(R.id.tvEmailLogin);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, OTPVerification.class));
            }
        });

        tvEmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, EmailLogin.class));
            }
        });

    }
}