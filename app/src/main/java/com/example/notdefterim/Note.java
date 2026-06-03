package com.example.notdefterim;

public class Note {
    private int id;
    private int userId;
    private String title;
    private String content;
    private String date;
    private int colorIndex;

    public Note(int id, int userId, String title, String content, String date, int colorIndex) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = date;
        this.colorIndex = colorIndex;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getDate() { return date; }
    public int getColorIndex() { return colorIndex; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setDate(String date) { this.date = date; }
    public void setColorIndex(int colorIndex) { this.colorIndex = colorIndex; }
}