public class DatabaseHelper extends SQLiteOpenHelper {

    // Veritabanı adı ve sürüm bilgileri
    private static final String DATABASE_NAME = "notdefterim.db";
    private static final int DATABASE_VERSION = 1;

    // Kullanıcı tablosu ve sütunları
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FULL_NAME = "full_name";

    // Notlar tablosu ve sütunları
    public static final String TABLE_NOTES = "notes";
    public static final String COL_NOTE_ID = "id";
    public static final String COL_NOTE_USER_ID = "user_id";
    public static final String COL_NOTE_TITLE = "title";
    public static final String COL_NOTE_CONTENT = "content";
    public static final String COL_NOTE_DATE = "date";
    public static final String COL_NOTE_COLOR = "color";

    // Veritabanı bağlantısını oluşturur
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Kullanıcılar tablosunu oluşturur
        String createUsers = ...

        // Notlar tablosunu oluşturur
        String createNotes = ...

        db.execSQL(createUsers);
        db.execSQL(createNotes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Sürüm değiştiğinde tabloları silip yeniden oluşturur
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Yeni kullanıcı kaydı ekler
    public boolean registerUser(String username, String password, String fullName)

    // Kullanıcı giriş bilgilerini kontrol eder
    public User loginUser(String username, String password)

    // Kullanıcı adının daha önce kullanılıp kullanılmadığını kontrol eder
    public boolean usernameExists(String username)

    // Veritabanına yeni not ekler
    public long addNote(int userId, String title, String content, String date, int color)

    // Mevcut notu günceller
    public boolean updateNote(int noteId, String title, String content, String date, int color)

    // Seçilen notu siler
    public boolean deleteNote(int noteId)

    // Belirli kullanıcıya ait tüm notları getirir
    public List<Note> getUserNotes(int userId)

    // Başlık veya içerikte arama yaparak notları listeler
    public List<Note> searchNotes(int userId, String query)
}
