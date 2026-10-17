package presentation;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/** Persian/Urdu ke liye qabil-e-mutalia font chunta hai aur poori app par lagata hai. */
public final class UiFonts {

    // Order badlo agar koi aur font pasand ho (jaise "Noto Nastaliq Urdu")
    private static final String[] PREFERRED = {
        "Geeza Pro", "Tahoma", "Arial Unicode MS", "Arial", "Dialog"
    };

    private static String family;

    private UiFonts() { }

    private static String family() {
        if (family == null) {
            Set<String> available = new HashSet<>(Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
            family = "Dialog";
            for (String name : PREFERRED) {
                if (available.contains(name)) { family = name; break; }
            }
        }
        return family;
    }

    public static Font regular(int size) { return new Font(family(), Font.PLAIN, size); }

    public static Font bold(int size) { return new Font(family(), Font.BOLD, size); }

    /** Swing ke tamam default fonts badal deta hai. */
    public static void install() {
        FontUIResource font = new FontUIResource(regular(15));
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (UIManager.get(key) instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}