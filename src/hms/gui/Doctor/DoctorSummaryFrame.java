package hms.gui.Doctor;

import hms.model.Doctor;
import hms.service.DoctorService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DoctorSummaryFrame extends JFrame {
    private final Doctor doctor;
    private final DoctorService service = new DoctorService();

    public DoctorSummaryFrame() {
        this(new Doctor("D-DEMO", "doctor.demo", "1234", "Dr. Demo", "General Medicine"));
    }

    public DoctorSummaryFrame(Doctor doctor) {
        this.doctor = doctor;
        setTitle("Doctor Summary - " + doctor.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 520);
        setLocationRelativeTo(null);
        buildScreen();
    }

    private void buildScreen() {
        JPanel page = DoctorTheme.backgroundPanel();
        page.setBorder(BorderFactory.createEmptyBorder(32, 42, 32, 42));
        page.setLayout(new BorderLayout(18, 24));

        JLabel heading = new JLabel("Doctor Summary");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 30));
        heading.setForeground(DoctorTheme.NAVY);
        JLabel subtitle = new JLabel(doctor.getName() + "  |  " + doctor.getSpecialty()
                + "  |  Doctor ID: " + doctor.getUserId());
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(DoctorTheme.TEXT);
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 6));
        header.setOpaque(false);
        header.add(heading);
        header.add(subtitle);
        page.add(header, BorderLayout.NORTH);

        JPanel statistics = new JPanel(new GridLayout(2, 3, 16, 16));
        statistics.setOpaque(false);
        statistics.add(statistic("Appointments", service.getAppointmentsForDoctor(doctor.getUserId()).size(), DoctorTheme.BLUE));
        statistics.add(statistic("Assigned Patients", service.getPatientsForDoctor(doctor.getUserId()).size(), DoctorTheme.NAVY));
        statistics.add(statistic("Medical Records", service.countMedicalRecordsForDoctor(doctor.getUserId()), new Color(60, 143, 126)));
        statistics.add(statistic("Prescriptions", service.countPrescriptionsForDoctor(doctor.getUserId()), new Color(190, 137, 56)));
        statistics.add(statistic("Lab Requests", service.countLabRequestsForDoctor(doctor.getUserId()), new Color(135, 91, 150)));
        page.add(statistics, BorderLayout.CENTER);

        JButton close = new JButton("Exit");
        close.setBackground(DoctorTheme.NAVY);
        close.setForeground(Color.WHITE);
        close.setFocusPainted(false);
        close.setFont(new Font("Segoe UI", Font.BOLD, 13));
        close.addActionListener(event -> dispose());
        JPanel footer = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.add(close);
        page.add(footer, BorderLayout.SOUTH);
        setContentPane(page);
    }

    private JPanel statistic(String title, int value, Color accent) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent, 2),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        JLabel number = new JLabel(String.valueOf(value));
        number.setFont(new Font("Segoe UI", Font.BOLD, 30));
        number.setForeground(accent);
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(DoctorTheme.TEXT);
        panel.add(number);
        panel.add(label);
        return panel;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new DoctorSummaryFrame().setVisible(true));
    }
}
