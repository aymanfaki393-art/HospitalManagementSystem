package hms.gui.Doctor;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JTextArea;

public class PlaceholderTextArea extends JTextArea {
    private final String placeholder;

    public PlaceholderTextArea(String placeholder, int rows, int columns) {
        super(rows, columns);
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (getText().isEmpty() && !isFocusOwner()) {
            Graphics2D copy = (Graphics2D) graphics.create();
            copy.setColor(new Color(145, 150, 158));
            FontMetrics metrics = copy.getFontMetrics(getFont());
            copy.drawString(placeholder, getInsets().left, getInsets().top + metrics.getAscent());
            copy.dispose();
        }
    }
}
