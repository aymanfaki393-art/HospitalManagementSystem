package hms.gui.Doctor;

import hms.dao.UserDAO;
import hms.model.Appointment;
import hms.model.Doctor;
import hms.model.LabRequest;
import hms.model.MedicalRecord;
import hms.model.Prescription;
import hms.service.BillingService;
import hms.service.DoctorService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DoctorConsultationFrame extends JFrame {
    private final Doctor doctor;
    private final DoctorService service = new DoctorService();
    private final UserDAO userDAO = new UserDAO();
    private final JComboBox<String> appointmentField = new JComboBox<>();
    private final JComboBox<String> patientField = new JComboBox<>();
    private final JLabel appointmentDetails = new JLabel();
    private final ArrayList<Appointment> appointments = new ArrayList<>();
    private final JTextField bloodPressureField = new JTextField();
    private final JTextField heartRateField = new JTextField();
    private final JTextField temperatureField = new JTextField();
    private final JTextArea notesArea = new JTextArea(7, 35);
    private final JComboBox<String> medicationField = new JComboBox<>();
    private final JTextField dosageField = new JTextField();
    private final JComboBox<String> dosageUnitField = new JComboBox<>(new String[]{"mg", "g", "ml", "L"});
    private final JTextArea prescriptionArea = new JTextArea(7, 35);
    private final JComboBox<String> labTestField = new JComboBox<>();
    private final JTextArea labNotesArea = new JTextArea(7, 35);

    public DoctorConsultationFrame() {
        this(new Doctor("D-DEMO", "doctor.demo", "1234", "Dr. Demo", "General Medicine"));
    }

    public DoctorConsultationFrame(Doctor doctor) {
        this.doctor = doctor;
        setTitle("Patient Consultation - " + doctor.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(820, 650);
        setLocationRelativeTo(null);
        buildScreen();
        loadPatients();
        loadOptions();
    }

    private void buildScreen() {
        JPanel page = DoctorTheme.backgroundPanel();
        page.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        page.setLayout(new BorderLayout(12, 15));
        JLabel heading = new JLabel("Patient Consultation Workspace");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 27));
        heading.setForeground(DoctorTheme.NAVY);
        page.add(heading, BorderLayout.NORTH);

        JPanel appointmentBar = new JPanel(new BorderLayout(8, 8));
        appointmentBar.setOpaque(false);
        JPanel selector = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        selector.setOpaque(false);
        JLabel label = new JLabel("Select appointment:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selector.add(label);
        appointmentField.setPreferredSize(new Dimension(280, 30));
        appointmentField.addActionListener(event -> showSelectedAppointment());
        selector.add(appointmentField);
        selector.add(new JLabel("Patient:"));
        patientField.setPreferredSize(new Dimension(190, 30));
        patientField.setEnabled(false);
        selector.add(patientField);
        appointmentBar.add(selector, BorderLayout.NORTH);
        appointmentDetails.setOpaque(true);
        appointmentDetails.setBackground(Color.WHITE);
        appointmentDetails.setForeground(DoctorTheme.TEXT);
        appointmentDetails.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DoctorTheme.BORDER),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        appointmentBar.add(appointmentDetails, BorderLayout.CENTER);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Patient Details and Vitals", createRecordTab());
        tabs.addTab("Order Medication", createPrescriptionTab());
        tabs.addTab("Order Lab or Imaging", createLabTab());
        page.add(appointmentBar, BorderLayout.NORTH);
        page.add(tabs, BorderLayout.CENTER);
        JButton exit = primaryButton("Exit");
        exit.addActionListener(event -> dispose());
        JButton history = primaryButton("View History");
        history.addActionListener(event -> viewPatientHistory());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.add(history);
        footer.add(exit);
        page.add(footer, BorderLayout.SOUTH);
        setContentPane(page);
    }

    private JPanel createRecordTab() {
        JPanel panel = formPanel();
        JPanel fields = new JPanel(new GridLayout(3, 2, 12, 12));
        fields.setOpaque(false);
        fields.add(new JLabel("Blood pressure (e.g. 120/80)"));
        fields.add(bloodPressureField);
        fields.add(new JLabel("Heart rate"));
        fields.add(heartRateField);
        fields.add(new JLabel("Temperature (Celsius)"));
        fields.add(temperatureField);
        panel.add(fields, BorderLayout.NORTH);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBorder(BorderFactory.createTitledBorder("Consultation notes"));
        panel.add(new JScrollPane(notesArea), BorderLayout.CENTER);
        JButton save = primaryButton("Save Patient Details");
        save.addActionListener(event -> saveRecord());
        panel.add(buttonBar(save), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createPrescriptionTab() {
        JPanel panel = formPanel();
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        addField(fields, "Medication", medicationField, 0);
        addField(fields, "Dosage", dosageField, 1);
        addField(fields, "Unit", dosageUnitField, 2);
        panel.add(fields, BorderLayout.NORTH);
        prescriptionArea.setLineWrap(true);
        prescriptionArea.setWrapStyleWord(true);
        prescriptionArea.setBorder(BorderFactory.createTitledBorder("Instructions"));
        panel.add(new JScrollPane(prescriptionArea), BorderLayout.CENTER);
        JButton issue = primaryButton("Issue Prescription");
        issue.addActionListener(event -> issuePrescription());
        panel.add(buttonBar(issue), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createLabTab() {
        JPanel panel = formPanel();
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        addField(fields, "Test or imaging type", labTestField, 0);
        panel.add(fields, BorderLayout.NORTH);
        labNotesArea.setLineWrap(true);
        labNotesArea.setWrapStyleWord(true);
        labNotesArea.setBorder(BorderFactory.createTitledBorder("Lab request notes"));
        panel.add(new JScrollPane(labNotesArea), BorderLayout.CENTER);
        JButton request = primaryButton("Submit Lab Request");
        request.addActionListener(event -> requestLab());
        panel.add(buttonBar(request), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel formPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        return panel;
    }

    private JPanel buttonBar(JButton button) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bar.setOpaque(false);
        bar.add(button);
        return bar;
    }

    private void addField(JPanel panel, String label, java.awt.Component field, int row) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 15);
        panel.add(new JLabel(label), constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        panel.add(field, constraints);
    }

    private JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(DoctorTheme.BLUE);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        return button;
    }

    private void loadPatients() {
        appointments.clear();
        for (Appointment appointment : service.getAppointmentsForDoctor(doctor.getUserId())) {
            if (isActiveAppointment(appointment)) {
                appointments.add(appointment);
            }
        }
        for (Appointment appointment : appointments) {
            patientField.addItem(appointment.getPatientId());
            appointmentField.addItem(appointment.getAppointmentId() + " | "
                + appointment.getAppointmentDate() + " " + appointment.getAppointmentTime());
        }
        if (appointments.isEmpty()) {
            appointmentField.addItem("No appointments available");
            patientField.addItem("No appointment patient");
            appointmentDetails.setText("No appointment details are available.");
        } else {
            appointmentField.setSelectedIndex(0);
            showSelectedAppointment();
        }
    }

    private boolean isActiveAppointment(Appointment appointment) {
        return !"Completed".equalsIgnoreCase(appointment.getStatus())
                && !"Cancelled".equalsIgnoreCase(appointment.getStatus());
    }

    private void loadOptions() {
        for (String medication : loadAvailableOptions("medication.txt", 4, 3)) {
            medicationField.addItem(medication);
        }
        for (String test : loadAvailableOptions("test.txt", 2, 1)) {
            labTestField.addItem(test);
        }
    }

    private String selectedPatientId() {
        String selected = String.valueOf(patientField.getSelectedItem());
        return selected == null ? "" : selected.trim();
    }

    private void showSelectedAppointment() {
        int index = appointmentField.getSelectedIndex();
        if (index < 0 || index >= appointments.size()) {
            return;
        }
        Appointment appointment = appointments.get(index);
        if (index >= patientField.getItemCount()) {
            return;
        }
        patientField.setSelectedIndex(index);
        appointmentDetails.setText("<html><b>Appointment ID:</b> " + appointment.getAppointmentId()
                + " &nbsp;&nbsp; <b>Date:</b> " + appointment.getAppointmentDate()
                + " &nbsp;&nbsp; <b>Time:</b> " + appointment.getAppointmentTime()
                + " &nbsp;&nbsp; <b>Status:</b> " + appointment.getStatus()
                + "<br><b>Reason:</b> " + appointment.getReason() + "</html>");
    }

    private boolean validPatient() {
        String patientId = selectedPatientId();
        if (patientId.isEmpty() || !service.canAccessPatient(doctor.getUserId(), patientId)) {
            JOptionPane.showMessageDialog(this, "Select a patient with an active appointment.");
            return false;
        }
        return true;
    }

    private void viewPatientHistory() {
        if (!validPatient()) {
            return;
        }
        String patientId = selectedPatientId();
        String patientName = patientId;
        for (hms.model.User user : userDAO.loadAllUsers()) {
            if (user instanceof hms.model.Patient && patientId.equals(user.getUserId())) {
                patientName = user.getName();
                break;
            }
        }
        MedicalHistoryFrame history = new MedicalHistoryFrame(doctor, patientId, patientName);
        history.setVisible(true);
        history.toFront();
        history.requestFocus();
    }

    private void saveRecord() {
        if (!validPatient()) return;
        String bp = bloodPressureField.getText().trim();
        String heartRate = heartRateField.getText().trim();
        String temperature = temperatureField.getText().trim();
        if (!validVitals(bp, heartRate, temperature)) {
            JOptionPane.showMessageDialog(this, "Enter valid vitals: BP 60/30-250/150, heart rate 20-250, temperature 30-45 C.");
            return;
        }
        String grade = BillingService.getMedicalGrade(bp, heartRate, temperature);
        service.saveMedicalRecord(new MedicalRecord(DoctorService.generateId("MR"), selectedPatientId(), doctor.getUserId(),
                DoctorService.today(), bp, heartRate, temperature, notesArea.getText().trim() + " (Grade " + grade + ")"));
        JOptionPane.showMessageDialog(this, "Patient details saved. Medical grade: " + grade);
        bloodPressureField.setText("");
        heartRateField.setText("");
        temperatureField.setText("");
        notesArea.setText("");
    }

    private void issuePrescription() {
        if (!validPatient()) return;
        String medication = String.valueOf(medicationField.getSelectedItem());
        String dosage = dosageField.getText().trim();
        String unit = String.valueOf(dosageUnitField.getSelectedItem());
        String instructions = prescriptionArea.getText().trim();
        try {
            if (medication.isEmpty() || dosage.isEmpty() || instructions.isEmpty() || Double.parseDouble(dosage) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Select medication and enter a positive dosage with instructions.");
            return;
        }
        service.createPrescription(new Prescription(DoctorService.generateId("RX"), selectedPatientId(), doctor.getUserId(), medication,
                dosage + " " + unit, instructions, DoctorService.today()));
        JOptionPane.showMessageDialog(this, "Prescription issued successfully.");
        dosageField.setText("");
        prescriptionArea.setText("");
    }

    private void requestLab() {
        if (!validPatient()) return;
        String test = String.valueOf(labTestField.getSelectedItem());
        if (test.isEmpty() || "null".equals(test)) {
            JOptionPane.showMessageDialog(this, "Select a lab or imaging test.");
            return;
        }
        service.requestLabTest(new LabRequest(DoctorService.generateId("LAB"), selectedPatientId(), doctor.getUserId(), test,
                labNotesArea.getText().trim().isEmpty() ? "Requested by doctor" : labNotesArea.getText().trim(), DoctorService.today()));
        JOptionPane.showMessageDialog(this, "Lab request submitted successfully.");
        labNotesArea.setText("");
    }

    private boolean validVitals(String bloodPressure, String heartRate, String temperature) {
        String[] parts = bloodPressure.split("/");
        if (parts.length != 2) return false;
        try {
            int systolic = Integer.parseInt(parts[0].trim());
            int diastolic = Integer.parseInt(parts[1].trim());
            int pulse = Integer.parseInt(heartRate);
            double degrees = Double.parseDouble(temperature);
            return systolic >= 60 && systolic <= 250 && diastolic >= 30 && diastolic <= 150
                    && systolic > diastolic && pulse >= 20 && pulse <= 250 && degrees >= 30 && degrees <= 45;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private String[] loadAvailableOptions(String fileName, int expectedParts, int availabilityIndex) {
        ArrayList<String> options = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return new String[0];
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",", expectedParts);
                if (parts.length == expectedParts && "available".equalsIgnoreCase(parts[availabilityIndex].trim())) {
                    options.add(parts[0].trim());
                }
            }
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(this, "Unable to read " + fileName + ".");
        }
        return options.toArray(new String[0]);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new DoctorConsultationFrame().setVisible(true));
    }
}
