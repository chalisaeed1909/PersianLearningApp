package business;

import java.util.List;
import data.dao.ChapterDAO;
import model.Chapter;

public class ChapterFacade {
    private final ChapterDAO dao;

    public ChapterFacade(ChapterDAO dao) { this.dao = dao; }

    private String validateTitle(String title) throws ValidationException {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Title khali nahi ho sakta. Barah-e-meharbani chapter ka title likhein.");
        }
        return title.trim();
    }

    public Chapter createChapter(String title) throws ValidationException {
        return dao.create(validateTitle(title));
    }

    public List<Chapter> getAllChapters() { return dao.findAll(); }

    public Chapter getChapter(int id) { return dao.findById(id); }

    public boolean renameChapter(int id, String newTitle) throws ValidationException {
        String clean = validateTitle(newTitle);   // fail ho to purana data safe (BR-06)
        return dao.updateTitle(id, clean);
    }

    /** BR-05: confirmation dialog mein ye message dikhao. */
    public String getDeleteWarning(int id) {
        Chapter c = dao.findById(id);
        return "Chapter \"" + c.getTitle() + "\" delete karne par iske tamam components "
             + "aur glossary entries bhi delete ho jayenge. Kya aap confirm karte hain?";
    }

    /** confirmed=false ho to kuch delete nahi hota (BR-04). */
    public boolean deleteChapter(int id, boolean confirmed) {
        if (!confirmed) return false;
        return dao.delete(id);
    }
}