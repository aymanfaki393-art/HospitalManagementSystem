package hms.gui.Doctor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public final class DoctorTheme {
    public static final Color NAVY = new Color(26, 76, 110);
    public static final Color BLUE = new Color(65, 157, 192);
    public static final Color PALE_BLUE = new Color(232, 246, 251);
    public static final Color BORDER = new Color(184, 218, 231);
    public static final Color TEXT = new Color(35, 61, 76);

    private DoctorTheme() {
    }

    public static JPanel backgroundPanel() {
        return new MedicalBackgroundPanel();
    }

    public static javax.swing.border.Border panelBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER), title);
    }

    private static class MedicalBackgroundPanel extends JPanel {
        MedicalBackgroundPanel() {
            setOpaque(true);
            setBackground(PALE_BLUE);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(199, 229, 239, 130));
            g.fillOval(getWidth() - 210, -80, 280, 280);
            g.fillOval(-100, getHeight() - 170, 240, 240);
            g.setColor(new Color(145, 201, 219, 105));
            g.setStroke(new BasicStroke(3f));
            int x = getWidth() - 105;
            int y = 75;
            g.drawLine(x - 18, y, x + 18, y);
            g.drawLine(x, y - 18, x, y + 18);
            g.drawLine(65, getHeight() - 75, 105, getHeight() - 75);
            g.drawLine(85, getHeight() - 95, 85, getHeight() - 55);
            g.dispose();
        }
    }
}
