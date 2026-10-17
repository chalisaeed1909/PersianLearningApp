package model;

public class GlossaryEntry {
    private int id;
    private int chapterId;
    private String persianWord;
    private String urduMeaning;

    public GlossaryEntry(int id, int chapterId, String persianWord, String urduMeaning) {
        this.id = id; this.chapterId = chapterId;
        this.persianWord = persianWord; this.urduMeaning = urduMeaning;
    }
    public int getId() { return id; }
    public int getChapterId() { return chapterId; }
    public String getPersianWord() { return persianWord; }
    public String getUrduMeaning() { return urduMeaning; }
}