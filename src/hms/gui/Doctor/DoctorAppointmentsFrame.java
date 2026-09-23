package hms.gui.Doctor;

import hms.dao.UserDAO;
import hms.model.Appointment;
import hms.model.Doctor;
import hms.model.Patient;
import hms.model.User;
import hms.service.DoctorService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class DoctorAppointmentsFrame extends JFrame {
    private final Doctor doctor;
    private final DoctorService service = new DoctorService();
    private final UserDAO userDAO = new UserDAO();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Appointment", "Patient ID", "Patient", "Date", "Time", "Reason", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public DoctorAppointmentsFrame() {
        this(new Doctor("D-DEMO", "doctor.demo", "1234", "Dr. Demo", "General Medicine"));
    }

    public DoctorAppointmentsFrame(Doctor doctor) {
        this.doctor = doctor;
        setTitle("Appointments and Patients - " + doctor.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 620);
        setLocationRelativeTo(null);
        buildScreen();
        loadAppointments();
    }

    private void buildScreen() {
        JPanel page = DoctorTheme.backgroundPanel();
        page.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        page.setLayout(new BorderLayout(12, 15));
        JLabel heading = new JLabel("Appointments and Patients");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 27));
        heading.setForeground(DoctorTheme.NAVY);
        page.add(heading, BorderLayout.NORTH);

        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(DoctorTheme.NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        page.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton complete = actionButton("Mark Completed");
        complete.addActionListener(event -> completeSelected());
        JButton reschedule = actionButton("Reschedule");
        reschedule.addActionListener(event -> rescheduleSelected());
        JButton cancel = actionButton("Cancel Appointment");
        cancel.addActionListener(event -> cancelSelected());
        JButton history = actionButton("View History");
        history.addActionListener(event -> viewSelectedPatientHistory());
        JButton consultation = actionButton("Open Consultation");
        consultation.addActionListener(event -> new DoctorConsultationFrame(doctor).setVisible(true));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(complete);
        actions.add(reschedule);
        actions.add(cancel);
        actions.add(history);
        actions.add(consultation);
        JButton exit = actionButton("Exit");
        exit.addActionListener(event -> dispose());
        actions.add(exit);
        page.add(actions, BorderLayout.SOUTH);
        setContentPane(page);
    }

    private JButton actionButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setForeground(DoctorTheme.NAVY);
        button.setFocusPainted(false);
        return button;
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);
        for (Appointment appointment : service.getAppointmentsForDoctor(doctor.getUserId())) {
            if (!isActiveAppointment(appointment)) {
                continue;
            }
            tableModel.addRow(new Object[]{appointment.getAppointmentId(), appointment.getPatientId(),
                getPatientName(appointment.getPatientId()), appointment.getAppointmentDate(),
                appointment.getAppointmentTime(), appointment.getReason(), appointment.getStatus()});
        }
    }

    private boolean isActiveAppointment(Appointment appointment) {
        return !"Completed".equalsIgnoreCase(appointment.getStatus())
                && !"Cancelled".equalsIgnoreCase(appointment.getStatus());
    }

    private String getPatientName(String patientId) {
        for (User user : userDAO.loadAllUsers()) {
            if (user instanceof Patient && patientId.equals(user.getUserId())) {
                return user.getName();
            }
        }
        return patientId;
    }

    private String selectedAppointmentId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an appointment first.");
            return null;
        }
        return String.valueOf(tableModel.getValueAt(row, 0));
    }

    private void completeSelected() {
        String id = selectedAppointmentId();
        if (id != null) {
            if (service.completeAppointment(id, doctor.getUserId())) {
                loadAppointments();
            } else {
                JOptionPane.showMessageDialog(this, "This appointment cannot be completed.");
            }
        }
    }

    private void cancelSelected() {
        String id = selectedAppointmentId();
        if (id != null && JOptionPane.showConfirmDialog(this, "Cancel this appointment?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            service.cancelAppointment(id, doctor.getUserId());
            loadAppointments();
        }
    }

    private void rescheduleSelected() {
        String id = selectedAppointmentId();
        if (id == null) {
            return;
        }
        String date = JOptionPane.showInputDialog(this, "New date (YYYY-MM-DD):");
        String time = JOptionPane.showInputDialog(this, "New time (HH:MM):");
        if (date == null || time == null || date.trim().isEmpty() || time.trim().isEmpty()) {
            return;
        }
        try {
            LocalDate.parse(date.trim());
            LocalTime.parse(time.trim());
        } catch (DateTimeParseException exception) {
            JOptionPane.showMessageDialog(this, "Use date YYYY-MM-DD and time HH:MM.");
            return;
        }
        if (service.rescheduleAppointment(id, doctor.getUserId(), date.trim(), time.trim())) {
            loadAppointments();
        } else {
            JOptionPane.showMessageDialog(this, "That time is occupied or the appointment cannot be rescheduled.");
        }
    }

    private void viewSelectedPatientHistory() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a patient appointment first.");
            return;
        }

        String patientId = String.valueOf(tableModel.getValueAt(row, 1));
        openHistory(patientId, getPatientName(patientId));
    }

    private void openHistory(String patientId, String patientName) {
        MedicalHistoryFrame history = new MedicalHistoryFrame(doctor, patientId, patientName);
        history.setVisible(true);
        history.toFront();
        history.requestFocus();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new DoctorAppointmentsFrame().setVisible(true));
    }
}
