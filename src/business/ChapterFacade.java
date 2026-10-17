package business;

import java.util.List;

import data.dao.ChapterDAO;
import model.Chapter;

public class ChapterFacade {
    private final ChapterDAO dao;

    public ChapterFacade(ChapterDAO dao) { this.dao = dao; }

    private String validateTitle(String title) throws ValidationException {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("عنوان خالی نہیں ہو سکتا۔ براہ کرم باب کا عنوان لکھیں۔");
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

    /** BR-05: confirmation dialog mein ye message dikhta hai. */
    public String getDeleteWarning(int id) {
        Chapter c = dao.findById(id);
        String name = (c == null) ? "" : " \"" + c.getTitle() + "\"";
        return "باب" + name + " حذف کرنے سے اس کے تمام اجزاء اور لغت کے الفاظ بھی حذف ہو جائیں گے۔ "
             + "کیا آپ حذف کی تصدیق کرتے ہیں؟";
    }

    /** confirmed=false ho to kuch delete nahi hota (BR-04). */
    public boolean deleteChapter(int id, boolean confirmed) {
        if (!confirmed) return false;
        return dao.delete(id);
    }
}