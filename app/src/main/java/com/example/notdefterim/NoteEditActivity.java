package com.example.notdefterim;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteEditActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private TextView tvDate;
    private ImageButton btnBack, btnSave, btnDelete;
    private View[] colorViews;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    private int noteId = -1;
    private int selectedColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        etTitle   = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        tvDate    = findViewById(R.id.tvDate);
        btnBack   = findViewById(R.id.btnBack);
        btnSave   = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        colorViews = new View[]{
                findViewById(R.id.color0),
                findViewById(R.id.color1),
                findViewById(R.id.color2),
                findViewById(R.id.color3),
                findViewById(R.id.color4),
                findViewById(R.id.color5),
        };

        tvDate.setText(getCurrentDate());

        if (getIntent().hasExtra("note_id")) {
            noteId = getIntent().getIntExtra("note_id", -1);
            etTitle.setText(getIntent().getStringExtra("note_title"));
            etContent.setText(getIntent().getStringExtra("note_content"));
            selectedColor = getIntent().getIntExtra("note_color", 0);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        updateColorSelection();

        // Renk tıklamaları
        for (int i = 0; i < colorViews.length; i++) {
            final int index = i;
            colorViews[i].setOnClickListener(v -> {
                selectedColor = index;
                updateColorSelection();
            });
        }

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveNote());
        btnDelete.setOnClickListener(v -> deleteNote());
    }

    private void saveNote() {
        String title   = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Başlık gerekli");
            etTitle.requestFocus();
            return;
        }

        String date = getCurrentDate();

        if (noteId == -1) {
            // Yeni not
            long id = dbHelper.addNote(sessionManager.getUserId(), title, content, date, selectedColor);
            if (id != -1) {
                Toast.makeText(this, "Not kaydedildi", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Kayıt hatası", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Güncelleme
            boolean ok = dbHelper.updateNote(noteId, title, content, date, selectedColor);
            if (ok) {
                Toast.makeText(this, "Not güncellendi", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Güncelleme hatası", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteNote() {
        if (noteId != -1) {
            dbHelper.deleteNote(noteId);
            Toast.makeText(this, "Not silindi", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateColorSelection() {
        for (int i = 0; i < colorViews.length; i++) {
            colorViews[i].setAlpha(i == selectedColor ? 1.0f : 0.4f);
            colorViews[i].setScaleX(i == selectedColor ? 1.2f : 1.0f);
            colorViews[i].setScaleY(i == selectedColor ? 1.2f : 1.0f);
        }
        // Arka planı seçilen renge göre değiştir
        if (selectedColor >= 0 && selectedColor < NoteAdapter.NOTE_COLORS.length) {
            etTitle.getRootView().setBackgroundColor(NoteAdapter.NOTE_COLORS[selectedColor]);
        }
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("d MMM yyyy", new Locale("tr")).format(new Date());
    }
}