package com.example.notdefterim;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private EditText etSearch;
    private TextView tvEmpty, tvGreeting;
    private FloatingActionButton fabAdd;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        recyclerView = findViewById(R.id.recyclerView);
        etSearch     = findViewById(R.id.etSearch);
        tvEmpty      = findViewById(R.id.tvEmpty);
        tvGreeting   = findViewById(R.id.tvGreeting);
        fabAdd       = findViewById(R.id.fabAdd);

        String firstName = sessionManager.getFullName().split(" ")[0];
        tvGreeting.setText("Merhaba, " + firstName + " 👋");

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        findViewById(R.id.btnLogout).setOnClickListener(v -> showLogoutDialog());

        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, NoteEditActivity.class));
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                filterNotes(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        List<Note> notes = dbHelper.getUserNotes(userId);
        showNotes(notes);
    }

    private void filterNotes(String query) {
        if (query.isEmpty()) {
            loadNotes();
        } else {
            List<Note> notes = dbHelper.searchNotes(userId, query);
            showNotes(notes);
        }
    }

    private void showNotes(List<Note> notes) {
        if (notes.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (adapter == null) {
            adapter = new NoteAdapter(notes, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateNotes(notes);
        }
    }

    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(this, NoteEditActivity.class);
        intent.putExtra("note_id", note.getId());
        intent.putExtra("note_title", note.getTitle());
        intent.putExtra("note_content", note.getContent());
        intent.putExtra("note_color", note.getColorIndex());
        startActivity(intent);
    }

    @Override
    public void onNoteLongClick(Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Notu Sil")
                .setMessage("\"" + note.getTitle() + "\" silinsin mi?")
                .setPositiveButton("Sil", (d, w) -> {
                    dbHelper.deleteNote(note.getId());
                    loadNotes();
                })
                .setNegativeButton("İptal", null)
                .show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Çıkış Yap")
                .setMessage("Hesabından çıkış yapmak istiyor musun?")
                .setPositiveButton("Çıkış Yap", (d, w) -> {
                    sessionManager.logout();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("İptal", null)
                .show();
    }
}