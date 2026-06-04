public class LoginActivity extends AppCompatActivity {

    // Kullanıcı adı ve şifre giriş alanları
    private EditText etUsername, etPassword;

    // Giriş yapma butonu ve kayıt ekranına geçiş bağlantısı
    private Button btnLogin;
    private TextView tvGoRegister;

    // Veritabanı ve oturum yönetimi nesneleri
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Veritabanı ve oturum yöneticisi başlatılır
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Arayüz bileşenleri tanımlanır
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLogin);
        tvGoRegister = findViewById(R.id.tvGoRegister);

        // Giriş yap butonuna basıldığında giriş işlemi başlatılır
        btnLogin.setOnClickListener(v -> attemptLogin());

        // Kayıt ol ekranına geçiş sağlar
        tvGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }

    // Kullanıcının giriş bilgilerini kontrol eder
    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Kullanıcı adı boş kontrolü
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Kullanıcı adı gerekli");
            etUsername.requestFocus();
            return;
        }

        // Şifre boş kontrolü
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Şifre gerekli");
            etPassword.requestFocus();
            return;
        }

        // Veritabanında kullanıcı doğrulaması yapılır
        User user = dbHelper.loginUser(username, password);

        if (user != null) {
            // Başarılı girişte oturum oluşturulur
            sessionManager.createSession(user);

            // Notlar ekranına yönlendirilir
            Intent intent = new Intent(this, NotesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            // Hatalı giriş mesajı gösterilir
            Toast.makeText(this, "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_SHORT).show();
        }
    }
}
