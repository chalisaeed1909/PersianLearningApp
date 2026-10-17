package data.dao;

import java.util.List;
import model.Chapter;

public interface ChapterDAO {
    Chapter create(String title);          // generate id sys
    List<Chapter> findAll();
    Chapter findById(int id);             
    boolean updateTitle(int id, String title);
    boolean delete(int id);                // del component & glossary
}