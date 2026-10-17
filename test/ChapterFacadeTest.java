import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.*;
import business.*;
import data.dao.ChapterDAO;
import model.Chapter;

class ChapterFacadeTest {

    // Custom stub: sirf utna behaviour jitna tests ko chahiye
    static class StubChapterDAO implements ChapterDAO {
        private final Map<Integer, Chapter> store = new LinkedHashMap<>();
        private int next = 1;
        public Chapter create(String t) { Chapter c = new Chapter(next++, t); store.put(c.getId(), c); return c; }
        public List<Chapter> findAll() { return new ArrayList<>(store.values()); }
        public Chapter findById(int id) { return store.get(id); }
        public boolean updateTitle(int id, String t) {
            Chapter c = store.get(id); if (c == null) return false; c.setTitle(t); return true; }
        public boolean delete(int id) { return store.remove(id) != null; }
    }

    private ChapterFacade facade;

    @BeforeEach
    void setUp() { facade = new ChapterFacade(new StubChapterDAO()); }  // har test ke liye fresh state

    @Test void createValidChapter() throws Exception {
        Chapter c = facade.createChapter("سلام و احوالپرسی");
        assertEquals("سلام و احوالپرسی", facade.getChapter(c.getId()).getTitle());
    }

    @Test void blankTitleRejected() {
        assertThrows(ValidationException.class, () -> facade.createChapter("   "));
        assertThrows(ValidationException.class, () -> facade.createChapter(null));
        assertTrue(facade.getAllChapters().isEmpty());
    }

    @Test void listReturnsAllChapters() throws Exception {
        facade.createChapter("A"); facade.createChapter("B");
        assertEquals(2, facade.getAllChapters().size());
    }

    @Test void renameKeepsOldTitleWhenInvalid() throws Exception {
        Chapter c = facade.createChapter("Old");
        assertThrows(ValidationException.class, () -> facade.renameChapter(c.getId(), ""));
        assertEquals("Old", facade.getChapter(c.getId()).getTitle());
    }

    @Test void renameValid() throws Exception {
        Chapter c = facade.createChapter("Old");
        assertTrue(facade.renameChapter(c.getId(), "New"));
        assertEquals("New", facade.getChapter(c.getId()).getTitle());
    }

    @Test void confirmedDeleteRemovesChapter() throws Exception {
        Chapter c = facade.createChapter("X");
        assertTrue(facade.deleteChapter(c.getId(), true));
        assertNull(facade.getChapter(c.getId()));
    }

    @Test void cancelledDeleteLeavesChapter() throws Exception {
        Chapter c = facade.createChapter("X");
        assertFalse(facade.deleteChapter(c.getId(), false));
        assertNotNull(facade.getChapter(c.getId()));
    }

    @Test void deleteWarningMentionsDependentData() throws Exception {
        Chapter c = facade.createChapter("X");
        String msg = facade.getDeleteWarning(c.getId());
        assertTrue(msg.contains("اجزاء") && msg.contains("لغت"));
    }
}