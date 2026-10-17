package model;

public class Chapter {
    private int id;
    private String title;

    public Chapter(int id, String title) { this.id = id; this.title = title; }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}