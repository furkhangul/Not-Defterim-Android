// Kullanıcı kayıt işlemlerini yöneten ekran
public class RegisterActivity extends AppCompatActivity {

    // Kullanıcı giriş alanları
    private EditText etFullName, etUsername, etPassword, etPasswordConfirm;

    // Kayıt butonu ve giriş ekranına geçiş metni
    private Button btnRegister;
    private TextView tvGoLogin;

    // Veritabanı ve oturum yönetimi nesneleri
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Veritabanı ve oturum yöneticisi başlatılır
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Arayüz bileşenleri tanımlanır
        etFullName       = findViewById(R.id.etFullName);
        etUsername       = findViewById(R.id.etUsername);
        etPassword       = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        btnRegister      = findViewById(R.id.btnRegister);
        tvGoLogin        = findViewById(R.id.tvGoLogin);

        // Kayıt ol butonu
        btnRegister.setOnClickListener(v -> attemptRegister());

        // Giriş ekranına yönlendirme
        tvGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    // Kullanıcı kayıt işlemini gerçekleştirir
    private void attemptRegister() {
        String fullName  = etFullName.getText().toString().trim();
        String username  = etUsername.getText().toString().trim();
        String password  = etPassword.getText().toString().trim();
        String confirm   = etPasswordConfirm.getText().toString().trim();

        // Boş alan kontrolleri
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

        // Şifre kontrolü
        if (!password.equals(confirm)) {
            etPasswordConfirm.setError("Şifreler eşleşmiyor");
            etPasswordConfirm.requestFocus();
            return;
        }

        // Kullanıcı adı kontrolü
        if (dbHelper.usernameExists(username)) {
            etUsername.setError("Bu kullanıcı adı zaten alınmış");
            etUsername.requestFocus();
            return;
        }

        // Kullanıcı veritabanına kaydedilir
        boolean success = dbHelper.registerUser(username, password, fullName);

        if (success) {
            Toast.makeText(this, "Hesap oluşturuldu! Giriş yapabilirsiniz.", Toast.LENGTH_SHORT).show();

            // Giriş ekranına yönlendirme
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Kayıt sırasında bir hata oluştu", Toast.LENGTH_SHORT).show();
        }
    }
}
