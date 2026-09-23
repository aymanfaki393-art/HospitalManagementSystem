package hms.gui.Doctor;

import hms.model.Doctor;
import hms.service.DoctorService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DoctorMainMenuFrame extends JFrame {
    private final Doctor doctor;
    private JLabel subtitle;

    public DoctorMainMenuFrame() {
        this(new Doctor("D-DEMO", "doctor.demo", "1234", "Dr. Demo", "General Medicine"));
    }

    public DoctorMainMenuFrame(Doctor doctor) {
        this.doctor = doctor;
        setTitle("Doctor's Dashboard - " + doctor.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        buildScreen();
    }

    private void buildScreen() {
        JPanel page = DoctorTheme.backgroundPanel();
        page.setBorder(BorderFactory.createEmptyBorder(35, 55, 35, 55));
        page.setLayout(new BorderLayout(20, 28));

        JLabel heading = new JLabel("Doctor's Dashboard");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        heading.setForeground(DoctorTheme.NAVY);
        subtitle = new JLabel();
        refreshDoctorDetails();
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(DoctorTheme.TEXT);
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 6));
        header.setOpaque(false);
        header.add(heading);
        header.add(subtitle);
        page.add(header, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridBagLayout());
        menu.setOpaque(false);
        addMenuButton(menu, menuButton("Appointments", "View patients and manage appointments", () -> openWindow(new DoctorAppointmentsFrame(doctor))), 0, 0);
        addMenuButton(menu, menuButton("Patient Consultation", "Enter vitals, notes, medicines, and lab requests", this::openConsultation), 1, 0);
        addMenuButton(menu, menuButton("Change Doctor Profile", "Update your name, specialty, username, or password", this::openProfile), 0, 1);
        addMenuButton(menu, menuButton("Doctor Summary", "View your activity and clinical work summary", () -> openWindow(new DoctorSummaryFrame(doctor))), 1, 1);
        page.add(menu, BorderLayout.CENTER);

        JButton logout = new JButton("Logout");
        styleButton(logout, new Color(74, 104, 119));
        logout.addActionListener(event -> {
            dispose();
            new hms.gui.LoginFrame().setVisible(true);
        });
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.add(logout, BorderLayout.EAST);
        page.add(footer, BorderLayout.SOUTH);
        setContentPane(page);
    }

    private JButton menuButton(String title, String description, Runnable action) {
        JButton button = new JButton("<html><center><b>" + title + "</b><br><font size='3'>" + description + "</font></center></html>");
        button.setPreferredSize(new Dimension(280, 105));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(DoctorTheme.NAVY);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DoctorTheme.BORDER, 2),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        button.addActionListener(event -> action.run());
        return button;
    }

    private void addMenuButton(JPanel menu, JButton button, int column, int row) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.CENTER;
        menu.add(button, constraints);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private void openWindow(JFrame window) {
        window.setVisible(true);
        window.toFront();
        window.requestFocus();
    }

    private void openConsultation() {
        try {
            DoctorConsultationFrame consultation = new DoctorConsultationFrame(doctor);
            consultation.setVisible(true);
            consultation.toFront();
            consultation.requestFocus();
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Unable to open Patient Consultation: " + exception.getMessage(),
                    "Doctor Portal Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openProfile() {
        DoctorProfileFrame profile = new DoctorProfileFrame(doctor, this::refreshDoctorDetails);
        openWindow(profile);
    }

    private void refreshDoctorDetails() {
        if (subtitle != null) {
            subtitle.setText("Welcome, " + doctor.getName() + "  |  " + doctor.getSpecialty());
        }
        setTitle("Doctor's Dashboard - " + doctor.getName());
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new DoctorMainMenuFrame().setVisible(true));
    }
}
