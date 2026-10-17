package presentation;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import business.ChapterFacade;
import data.factory.DAOFactory;
import data.factory.MySQLDAOFactory;

public class MainApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // default look and feel chalega
            }
            UiFonts.install();

            DAOFactory factory = new MySQLDAOFactory();   // MongoDB test ke waqt yahan badlega
            ChapterFacade facade = new ChapterFacade(factory.getChapterDAO());
            new ChapterManagementFrame(facade).setVisible(true);
        });
    }
}