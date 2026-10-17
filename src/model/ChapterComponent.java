package model;

public class ChapterComponent {
    private int id;
    private int chapterId;
    private String type;
    private int order;
    private String content;

    public ChapterComponent(int id, int chapterId, String type, int order, String content) {
        this.id = id; this.chapterId = chapterId; this.type = type;
        this.order = order; this.content = content;
    }
    public int getId() { return id; }
    public int getChapterId() { return chapterId; }
    public String getType() { return type; }
    public int getOrder() { return order; }
    public String getContent() { return content; }
}