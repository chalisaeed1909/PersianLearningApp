package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import business.ValidationException;

/** Naya baab banane aur baab ka unwan tabdeel karne ka form. */
public class ChapterFormDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public interface Saver {
        void save(String title) throws ValidationException;
    }

    private final JTextField titleField = new JTextField(24);
    private final JLabel errorLabel = new JLabel(" ");
    private final Saver saver;
    private boolean saved = false;

    public ChapterFormDialog(Frame owner, String dialogTitle, String initialTitle, Saver saver) {
        super(owner, dialogTitle, true);
        this.saver = saver;

        titleField.setText(initialTitle);
        errorLabel.setForeground(new Color(180, 0, 0));

        JPanel form = new JPanel(new BorderLayout(0, 8));
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        form.add(new JLabel("باب کا عنوان:"), BorderLayout.NORTH);
        form.add(titleField, BorderLayout.CENTER);
        form.add(errorLabel, BorderLayout.SOUTH);

        JButton saveButton = new JButton("محفوظ کریں");
        JButton cancelButton = new JButton("منسوخ کریں");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEADING, 8, 8));
        buttons.add(saveButton);
        buttons.add(cancelButton);

        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
        getContentPane().applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        getRootPane().setDefaultButton(saveButton);

        saveButton.addActionListener(e -> onSave());
        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isSaved() { return saved; }

    private void onSave() {
        try {
            saver.save(titleField.getText());
            saved = true;
            dispose();
        } catch (ValidationException ex) {
            // Galti ka message dikhao, form khula rakho aur likha hua text na mitao (BR-07)
            showError(ex.getMessage());
            titleField.requestFocusInWindow();
            titleField.selectAll();
        } catch (RuntimeException ex) {
            showError("محفوظ نہیں ہو سکا۔ براہ کرم MySQL چلنے اور db.properties کی جانچ کریں۔");
        }
    }

    private void showError(String message) {
        errorLabel.setText("<html><body dir='rtl' style='width:300px'>" + message + "</body></html>");
        pack();
    }
}