# 📝 Not Defterim

Modern, minimalist bir Android not defteri uygulaması. Her kullanıcı kendi hesabıyla giriş yapar ve yalnızca kendi notlarına erişir.

---

## Özellikler

- 🔐 Kullanıcı kaydı ve girişi (SQLite tabanlı)
- 👤 Her kullanıcıya özel not saklama
- 🎨 6 farklı not rengi
- 🔍 Notlar içinde arama
- 📌 Pinterest tarzı staggered grid görünüm
- 💾 Otomatik oturum — uygulamayı kapatıp açınca tekrar giriş gerekmez
- 🗑️ Uzun basma ile hızlı silme

---

## Ekranlar

| Splash | Giriş Seçimi | Giriş | Kayıt |
|--------|-------------|-------|-------|
| Açılış animasyonu | Login / Signup | Kullanıcı girişi | Yeni hesap |

| Not Listesi | Not Düzenle |
|-------------|-------------|
| Kişisel notlar, arama | Yaz, renk seç, kaydet |

---

## Teknolojiler

- **Java** — Android uygulama dili
- **SQLite** — Yerel veritabanı (kullanıcılar + notlar)
- **SharedPreferences** — Oturum yönetimi
- **RecyclerView** — Staggered grid not listesi
- **Material Design 3** — UI bileşenleri
- **CardView** — Not kartları

---

## Veritabanı Yapısı

```
users
├── id (INTEGER, PRIMARY KEY)
├── username (TEXT, UNIQUE)
├── password (TEXT)
└── full_name (TEXT)

notes
├── id (INTEGER, PRIMARY KEY)
├── user_id (INTEGER, FK → users.id)
├── title (TEXT)
├── content (TEXT)
├── date (TEXT)
└── color (INTEGER)
```

---

## Kurulum

1. Repoyu klonla
```bash
git clone https://github.com/furkhangul/Not-Defterim-Android.git
```

2. Android Studio'da aç
```
File → Open → Not-Defterim-Android klasörünü seç
```

3. Gradle sync bekle, ardından çalıştır
```
Run → Run 'app'  (Shift + F10)
```

> Minimum SDK: API 24 (Android 7.0)  
> Target SDK: API 34 (Android 14)

---

## Uygulama Akışı

```
SplashActivity (3sn)
       ↓
MainActivity — Giriş / Kayıt seçimi
       ↓                  ↓
LoginActivity      RegisterActivity
       ↓
NotesActivity — Kişisel not listesi
       ↓
NoteEditActivity — Not yaz / düzenle / sil
```

---

## Geliştirici

**Furkan**  
Android geliştirme · Java · SQLite
