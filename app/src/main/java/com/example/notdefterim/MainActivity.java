package com.example.notdefterim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, NotesActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
    }

    public void loginMethod(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void signupMethod(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}