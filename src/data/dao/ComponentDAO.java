package data.dao;

import java.util.List;
import model.ChapterComponent;

public interface ComponentDAO {
    List<ChapterComponent> findByChapter(int chapterId);
    void saveStructure(int chapterId, List<ChapterComponent> components);
    boolean updateContent(int componentId, String content);
}