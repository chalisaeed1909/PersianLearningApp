package data.factory;

import data.dao.ChapterDAO;
import data.dao.ComponentDAO;
import data.dao.GlossaryDAO;
import data.mysql.MySQLChapterDAO;

public class MySQLDAOFactory extends DAOFactory {

    @Override
    public ChapterDAO getChapterDAO() {
        return new MySQLChapterDAO();
    }

    @Override
    public ComponentDAO getComponentDAO() {
        throw new UnsupportedOperationException("UC2/UC3 ke members isay implement karenge.");
    }

    @Override
    public GlossaryDAO getGlossaryDAO() {
        throw new UnsupportedOperationException("UC4 ka member isay implement karega.");
    }
}