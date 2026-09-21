package hms.gui.Doctor;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPasswordField;

public class PlaceholderPasswordField extends JPasswordField {
    private final String placeholder;

    public PlaceholderPasswordField(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (getPassword().length == 0 && !isFocusOwner()) {
            Graphics2D copy = (Graphics2D) graphics.create();
            copy.setColor(new Color(145, 150, 158));
            FontMetrics metrics = copy.getFontMetrics(getFont());
            copy.drawString(placeholder, getInsets().left, getInsets().top + metrics.getAscent());
            copy.dispose();
        }
    }
}
