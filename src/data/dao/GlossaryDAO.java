package data.dao;

import java.util.List;
import model.GlossaryEntry;

public interface GlossaryDAO {
    GlossaryEntry create(int chapterId, String persianWord, String urduMeaning);
    List<GlossaryEntry> findByChapter(int chapterId);
    boolean update(int id, String persianWord, String urduMeaning);
    boolean delete(int id);
}