package com.example.notdefterim;
import android.content.Intent; 
import android.os.Bundle; 
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

// Kullanıcı oturum bilgilerini yöneten sınıf
public class SessionManager {

    // SharedPreferences dosya adı ve anahtarlar
    private static final String PREF_NAME = "NotDefterimSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "fullName";

    // Oturum verilerini saklamak için SharedPreferences nesneleri
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        // SharedPreferences başlatılır
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Kullanıcı giriş yaptıktan sonra oturum oluşturur
    public void createSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_FULL_NAME, user.getFullName());
        editor.apply();
    }

    // Kullanıcının giriş yapıp yapmadığını kontrol eder
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Giriş yapan kullanıcının ID bilgisini döndürür
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    // Kullanıcı adını döndürür
    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }

    // Kullanıcının tam adını döndürür
    public String getFullName() {
        return prefs.getString(KEY_FULL_NAME, "");
    }

    // Oturumu kapatır ve tüm verileri siler
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
