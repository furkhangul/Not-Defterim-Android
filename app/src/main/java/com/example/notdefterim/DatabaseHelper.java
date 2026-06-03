package com.example.notdefterim;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notdefterim.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FULL_NAME = "full_name";
    public static final String TABLE_NOTES = "notes";
    public static final String COL_NOTE_ID = "id";
    public static final String COL_NOTE_USER_ID = "user_id";
    public static final String COL_NOTE_TITLE = "title";
    public static final String COL_NOTE_CONTENT = "content";
    public static final String COL_NOTE_DATE = "date";
    public static final String COL_NOTE_COLOR = "color";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_FULL_NAME + " TEXT NOT NULL)";

        String createNotes = "CREATE TABLE " + TABLE_NOTES + " (" +
                COL_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOTE_USER_ID + " INTEGER NOT NULL, " +
                COL_NOTE_TITLE + " TEXT NOT NULL, " +
                COL_NOTE_CONTENT + " TEXT, " +
                COL_NOTE_DATE + " TEXT NOT NULL, " +
                COL_NOTE_COLOR + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COL_NOTE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";

        db.execSQL(createUsers);
        db.execSQL(createNotes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean registerUser(String username, String password, String fullName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username.trim().toLowerCase());
        values.put(COL_PASSWORD, password);
        values.put(COL_FULL_NAME, fullName.trim());
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public User loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                null,
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username.trim().toLowerCase(), password},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_FULL_NAME))
            );
        }
        cursor.close();
        db.close();
        return user;
    }

    public boolean usernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID},
                COL_USERNAME + "=?",
                new String[]{username.trim().toLowerCase()},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }


    public long addNote(int userId, String title, String content, String date, int color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOTE_USER_ID, userId);
        values.put(COL_NOTE_TITLE, title);
        values.put(COL_NOTE_CONTENT, content);
        values.put(COL_NOTE_DATE, date);
        values.put(COL_NOTE_COLOR, color);
        long id = db.insert(TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    public boolean updateNote(int noteId, String title, String content, String date, int color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOTE_TITLE, title);
        values.put(COL_NOTE_CONTENT, content);
        values.put(COL_NOTE_DATE, date);
        values.put(COL_NOTE_COLOR, color);
        int rows = db.update(TABLE_NOTES, values, COL_NOTE_ID + "=?",
                new String[]{String.valueOf(noteId)});
        db.close();
        return rows > 0;
    }

    public boolean deleteNote(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_NOTES, COL_NOTE_ID + "=?",
                new String[]{String.valueOf(noteId)});
        db.close();
        return rows > 0;
    }

    public List<Note> getUserNotes(int userId) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null,
                COL_NOTE_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, COL_NOTE_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                notes.add(new Note(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_NOTE_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_NOTE_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE_CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_NOTE_COLOR))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    public List<Note> searchNotes(int userId, String query) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String like = "%" + query + "%";
        Cursor cursor = db.query(TABLE_NOTES, null,
                COL_NOTE_USER_ID + "=? AND (" + COL_NOTE_TITLE + " LIKE ? OR " + COL_NOTE_CONTENT + " LIKE ?)",
                new String[]{String.valueOf(userId), like, like},
                null, null, COL_NOTE_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                notes.add(new Note(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_NOTE_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_NOTE_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE_CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_NOTE_COLOR))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }
}