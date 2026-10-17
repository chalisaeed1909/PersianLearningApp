package data.factory;

import data.dao.*;

public abstract class DAOFactory {
    public abstract ChapterDAO getChapterDAO();
    public abstract ComponentDAO getComponentDAO();
    public abstract GlossaryDAO getGlossaryDAO();
}