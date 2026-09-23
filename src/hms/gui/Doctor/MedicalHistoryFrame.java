package hms.gui.Doctor;

import hms.model.Doctor;
import hms.model.MedicalRecord;
import hms.service.DoctorService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MedicalHistoryFrame extends JFrame {
    public MedicalHistoryFrame() {
        this(new Doctor("D-DEMO", "doctor.demo", "1234", "Dr. Demo", "General Medicine"), "P001", "Demo Patient");
    }

    public MedicalHistoryFrame(Doctor doctor, String patientId, String patientName) {
        setTitle("Medical History - " + patientId);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(820, 620);
        setLocationRelativeTo(null);
        buildScreen(doctor, patientId, patientName);
    }

    private void buildScreen(Doctor doctor, String patientId, String patientName) {
        JPanel page = DoctorTheme.backgroundPanel();
        page.setBorder(BorderFactory.createEmptyBorder(28, 35, 28, 35));
        page.setLayout(new BorderLayout(16, 18));

        JLabel heading = new JLabel("Prior Medical History");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(DoctorTheme.NAVY);
        JLabel patient = new JLabel(patientName + "  |  Patient ID: " + patientId);
        patient.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        patient.setForeground(DoctorTheme.TEXT);
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 5));
        header.setOpaque(false);
        header.add(heading);
        header.add(patient);
        page.add(header, BorderLayout.NORTH);

        JPanel records = new JPanel();
        records.setOpaque(false);
        records.setLayout(new javax.swing.BoxLayout(records, javax.swing.BoxLayout.Y_AXIS));
        ArrayList<MedicalRecord> history = new DoctorService().getMedicalRecordsForPatient(patientId);
        if (history.isEmpty()) {
            JLabel empty = new JLabel("No prior medical history found.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            empty.setForeground(DoctorTheme.TEXT);
            records.add(empty);
        } else {
            for (MedicalRecord record : history) {
                records.add(recordCard(record));
                records.add(javax.swing.Box.createVerticalStrut(12));
            }
        }
        JScrollPane scroll = new JScrollPane(records);
        scroll.setBorder(BorderFactory.createLineBorder(DoctorTheme.BORDER));
        scroll.getViewport().setBackground(new Color(248, 252, 253));
        page.add(scroll, BorderLayout.CENTER);

        JButton close = new JButton("Exit");
        close.setBackground(DoctorTheme.BLUE);
        close.setForeground(Color.WHITE);
        close.setFocusPainted(false);
        close.setFont(new Font("Segoe UI", Font.BOLD, 13));
        close.addActionListener(event -> dispose());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.add(close);
        page.add(footer, BorderLayout.SOUTH);
        setContentPane(page);
    }

    private JPanel recordCard(MedicalRecord record) {
        JPanel card = new JPanel(new BorderLayout(12, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DoctorTheme.BORDER, 2),
                BorderFactory.createEmptyBorder(13, 16, 13, 16)));

        JLabel date = new JLabel("Record date: " + record.getRecordDate());
        date.setFont(new Font("Segoe UI", Font.BOLD, 15));
        date.setForeground(DoctorTheme.NAVY);
        card.add(date, BorderLayout.NORTH);

        JLabel vitals = new JLabel("Blood pressure: " + record.getBloodPressure()
                + "     Heart rate: " + record.getHeartRate()
                + "     Temperature: " + record.getTemperature() + " C");
        vitals.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        vitals.setForeground(DoctorTheme.TEXT);
        card.add(vitals, BorderLayout.CENTER);

        JLabel notes = new JLabel("Notes: " + record.getNotes());
        notes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        notes.setForeground(DoctorTheme.TEXT);
        card.add(notes, BorderLayout.SOUTH);
        return card;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new MedicalHistoryFrame().setVisible(true));
    }
}
