package com.example.notdefterim;

// Kullanıcı bilgilerini temsil eden model sınıfı
public class User {

    // Kullanıcıya ait temel bilgiler
    private int id;
    private String username;
    private String fullName;

    // Kullanıcı nesnesi oluşturmak için constructor
    public User(int id, String username, String fullName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
    }

    // Getter metotları (veri erişimi sağlar)
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
}
