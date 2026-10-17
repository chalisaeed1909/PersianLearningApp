package presentation;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import business.ChapterFacade;
import business.ValidationException;
import model.Chapter;

/** UC-01: Manage Chapters and Basic Details. Ye screen sirf ChapterFacade se baat karti hai. */
public class ChapterManagementFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String[] OK_ONLY = { "ٹھیک ہے" };
    private static final String NOT_FOUND = "یہ باب اب موجود نہیں۔ براہ کرم فہرست تازہ کریں۔";

    private final ChapterFacade facade;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public ChapterManagementFrame(ChapterFacade facade) {
        super("باب کا انتظام");
        this.facade = facade;

        tableModel = new DefaultTableModel(new Object[] { "نمبر", "عنوان" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setMaxWidth(90);

        DefaultTableCellRenderer idRenderer = new DefaultTableCellRenderer();
        idRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer titleRenderer = new DefaultTableCellRenderer();
        titleRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        titleRenderer.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        table.getColumnModel().getColumn(0).setCellRenderer(idRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(titleRenderer);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) viewSelected();
            }
        });

        JButton createButton = new JButton("نیا باب بنائیں");
        JButton renameButton = new JButton("عنوان تبدیل کریں");
        JButton viewButton = new JButton("باب دیکھیں");
        JButton deleteButton = new JButton("باب حذف کریں");
        JButton refreshButton = new JButton("تازہ کریں");

        createButton.addActionListener(e -> createChapter());
        renameButton.addActionListener(e -> renameSelected());
        viewButton.addActionListener(e -> viewSelected());
        deleteButton.addActionListener(e -> deleteSelected());
        refreshButton.addActionListener(e -> loadChapters());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEADING, 8, 8));
        toolbar.add(createButton);
        toolbar.add(renameButton);
        toolbar.add(viewButton);
        toolbar.add(deleteButton);
        toolbar.add(refreshButton);

        JLabel hint = new JLabel("فہرست سے باب منتخب کریں۔ تفصیل دیکھنے کے لیے باب پر ڈبل کلک کریں۔");
        hint.setBorder(BorderFactory.createEmptyBorder(6, 12, 8, 12));

        getContentPane().add(toolbar, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        getContentPane().add(hint, BorderLayout.SOUTH);
        getContentPane().applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760, 440);
        setLocationRelativeTo(null);

        loadChapters();
    }

    // ---------- Actions ----------

    private void loadChapters() {
        try {
            tableModel.setRowCount(0);
            for (Chapter c : facade.getAllChapters()) {
                tableModel.addRow(new Object[] { c.getId(), c.getTitle() });
            }
        } catch (RuntimeException ex) {
            showDbError();
        }
    }

    private void createChapter() {
        ChapterFormDialog dialog = new ChapterFormDialog(this, "نیا باب", "",
                title -> facade.createChapter(title));
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadChapters();
            int last = tableModel.getRowCount() - 1;
            if (last >= 0) table.setRowSelectionInterval(last, last);
        }
    }

    private void renameSelected() {
        Integer id = selectedId();
        if (id == null) return;
        try {
            Chapter chapter = facade.getChapter(id);
            if (chapter == null) {
                showMessage("خرابی", NOT_FOUND, JOptionPane.ERROR_MESSAGE);
                loadChapters();
                return;
            }
            ChapterFormDialog dialog = new ChapterFormDialog(this, "عنوان میں ترمیم", chapter.getTitle(),
                    title -> {
                        if (!facade.renameChapter(id, title)) {
                            throw new ValidationException(NOT_FOUND);
                        }
                    });
            dialog.setVisible(true);
            if (dialog.isSaved()) loadChapters();
        } catch (RuntimeException ex) {
            showDbError();
        }
    }

    private void viewSelected() {
        Integer id = selectedId();
        if (id == null) return;
        try {
            Chapter chapter = facade.getChapter(id);
            if (chapter == null) {
                showMessage("خرابی", NOT_FOUND, JOptionPane.ERROR_MESSAGE);
                loadChapters();
                return;
            }
            showMessage("باب کی تفصیل",
                    "نمبر: " + chapter.getId() + "<br>عنوان: " + escape(chapter.getTitle()),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            showDbError();
        }
    }

    private void deleteSelected() {
        Integer id = selectedId();
        if (id == null) return;
        try {
            String warning = facade.getDeleteWarning(id);
            String[] options = { "ہاں، حذف کریں", "نہیں" };
            int choice = showOptions("حذف کی تصدیق", htmlLabel(escape(warning)),
                    JOptionPane.WARNING_MESSAGE, options, options[1]);
            if (choice == 0) {
                if (!facade.deleteChapter(id, true)) {
                    showMessage("خرابی", NOT_FOUND, JOptionPane.ERROR_MESSAGE);
                }
                loadChapters();
            }
            // Tasdeeq na ho to baab jaisa tha waisa hi rehta hai
        } catch (RuntimeException ex) {
            showDbError();
        }
    }

    // ---------- Helpers ----------

    private Integer selectedId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showMessage("انتخاب ضروری ہے", "پہلے فہرست سے کوئی باب منتخب کریں۔",
                    JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return (Integer) tableModel.getValueAt(row, 0);
    }

    private void showDbError() {
        showMessage("ڈیٹا بیس کی خرابی",
                "ڈیٹا بیس سے رابطہ نہیں ہو سکا۔ براہ کرم MySQL چلنے اور db.properties کی جانچ کریں۔",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String title, String htmlBody, int type) {
        showOptions(title, htmlLabel(htmlBody), type, OK_ONLY, OK_ONLY[0]);
    }

    private int showOptions(String title, JLabel message, int type, String[] options, String initial) {
        JOptionPane pane = new JOptionPane(message, type, JOptionPane.DEFAULT_OPTION, null, options, initial);
        pane.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        JDialog dialog = pane.createDialog(this, title);
        dialog.setVisible(true);
        Object value = pane.getValue();
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(value)) return i;
        }
        return -1;
    }

    private static JLabel htmlLabel(String htmlBody) {
        return new JLabel("<html><body dir='rtl' style='width:320px'>" + htmlBody + "</body></html>");
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}