package com.example.notdefterim;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etUsername, etPassword, etPasswordConfirm;
    private Button btnRegister;
    private TextView tvGoLogin;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        etFullName       = findViewById(R.id.etFullName);
        etUsername       = findViewById(R.id.etUsername);
        etPassword       = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        btnRegister      = findViewById(R.id.btnRegister);
        tvGoLogin        = findViewById(R.id.tvGoLogin);

        btnRegister.setOnClickListener(v -> attemptRegister());
        tvGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegister() {
        String fullName  = etFullName.getText().toString().trim();
        String username  = etUsername.getText().toString().trim();
        String password  = etPassword.getText().toString().trim();
        String confirm   = etPasswordConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Ad soyad gerekli");
            etFullName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Kullanıcı adı gerekli");
            etUsername.requestFocus();
            return;
        }
        if (username.length() < 3) {
            etUsername.setError("En az 3 karakter olmalı");
            etUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Şifre gerekli");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 4) {
            etPassword.setError("En az 4 karakter olmalı");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirm)) {
            etPasswordConfirm.setError("Şifreler eşleşmiyor");
            etPasswordConfirm.requestFocus();
            return;
        }
        if (dbHelper.usernameExists(username)) {
            etUsername.setError("Bu kullanıcı adı zaten alınmış");
            etUsername.requestFocus();
            return;
        }

        boolean success = dbHelper.registerUser(username, password, fullName);
        if (success) {
            Toast.makeText(this, "Hesap oluşturuldu! Giriş yapabilirsiniz.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Kayıt sırasında bir hata oluştu", Toast.LENGTH_SHORT).show();
        }
    }
}