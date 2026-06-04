package com.example.notdefterim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Kullanıcının oturum durumunu kontrol eden nesne
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SessionManager başlatılır
        sessionManager = new SessionManager(this);

        // Kullanıcı giriş yapmışsa doğrudan notlar ekranına yönlendirilir
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, NotesActivity.class));
            finish(); // MainActivity kapatılır
            return;
        }

        // Giriş yapılmamışsa ana ekran gösterilir
        setContentView(R.layout.activity_main);
    }

    // Giriş Yap butonuna basıldığında LoginActivity açılır
    public void loginMethod(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    // Kayıt Ol butonuna basıldığında RegisterActivity açılır
    public void signupMethod(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
