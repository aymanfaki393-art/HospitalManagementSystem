package hms.gui.Doctor;

import hms.dao.UserDAO;
import hms.model.Appointment;
import hms.model.Doctor;
import hms.model.LabRequest;
import hms.model.MedicalRecord;
import hms.model.Patient;
import hms.model.Prescription;
import hms.model.User;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class DoctorDashboard extends JFrame {
    private final Doctor currentDoctor;
    private final DoctorService doctorService = new DoctorService();
    private final UserDAO userDAO = new UserDAO();

    private JLabel welcomeLabel;
    private JTable appointmentTable;
    private JTable patientTable;
    private JComboBox<String> patientIdField;
    private JTextField bloodPressureField;
    private JTextField heartRateField;
    private JTextField temperatureField;
    private JTextArea notesArea;
    private JComboBox<String> medicationField;
    private JTextField dosageField;
    private JComboBox<String> dosageUnitField;
    private JTextArea prescriptionArea;
    private JComboBox<String> labTestField;
    private JTextArea labNotesArea;

    public DoctorDashboard(Doctor doctor) {
        this.currentDoctor = doctor;
        initComponents();
        setupFrame();
        loadDoctorData();
    }

    private void setupFrame() {
        setTitle("Doctor Dashboard - " + currentDoctor.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBackground(new Color(236, 239, 244));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        welcomeLabel = new JLabel("Welcome, Dr. " + currentDoctor.getName() + " (" + currentDoctor.getUserId() + ")");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(40, 40, 40));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 12, 12));
        centerPanel.setOpaque(false);

        JPanel appointmentPanel = new JPanel(new BorderLayout(8, 8));
        appointmentPanel.setBackground(new Color(248, 249, 251));
        appointmentPanel.setBorder(createSectionBorder("My Appointments"));
        appointmentPanel.setPreferredSize(new Dimension(560, 220));
        appointmentTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Date", "Time", "Patient", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        appointmentTable.setRowHeight(28);
        appointmentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        appointmentTable.setFillsViewportHeight(true);
        appointmentTable.setGridColor(new Color(210, 218, 227));
        appointmentTable.setPreferredScrollableViewportSize(new Dimension(500, 120));
        appointmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        appointmentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        appointmentTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        appointmentTable.getColumnModel().getColumn(1).setPreferredWidth(110);
        appointmentTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        appointmentTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        appointmentTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        appointmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && appointmentTable.getSelectedRow() >= 0) {
                String appointmentId = String.valueOf(appointmentTable.getValueAt(appointmentTable.getSelectedRow(), 0));
                String patientId = findPatientIdForAppointment(appointmentId);
                patientIdField.setSelectedItem(patientId);
            }
        });
        appointmentPanel.add(createScrollPane(appointmentTable), BorderLayout.CENTER);
        JButton completeBtn = new JButton("Mark as Completed");
        completeBtn.setBackground(new Color(160, 190, 220));
        completeBtn.setForeground(new Color(25, 30, 40));
        completeBtn.setFocusPainted(false);
        completeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        completeBtn.addActionListener(evt -> completeSelectedAppointment());
        JPanel appointmentActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        appointmentActions.setOpaque(false);
        appointmentActions.add(completeBtn);
        JButton rescheduleBtn = new JButton("Reschedule");
        rescheduleBtn.addActionListener(evt -> rescheduleSelectedAppointment());
        appointmentActions.add(rescheduleBtn);
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(evt -> cancelSelectedAppointment());
        appointmentActions.add(cancelBtn);
        appointmentPanel.add(appointmentActions, BorderLayout.SOUTH);

        JPanel patientPanel = new JPanel(new BorderLayout(8, 8));
        patientPanel.setBackground(new Color(248, 249, 251));
        patientPanel.setBorder(createSectionBorder("Assigned Patients"));
        patientPanel.setPreferredSize(new Dimension(560, 220));
        patientTable = new JTable(new DefaultTableModel(
                new Object[]{"Patient ID", "Name", "Contact"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        patientTable.setRowHeight(28);
        patientTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        patientTable.setFillsViewportHeight(true);
        patientTable.setGridColor(new Color(210, 218, 227));
        patientTable.setPreferredScrollableViewportSize(new Dimension(500, 120));
        patientTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        patientTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        patientTable.getColumnModel().getColumn(0).setPreferredWidth(110);
        patientTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        patientTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        patientPanel.add(createScrollPane(patientTable), BorderLayout.CENTER);
        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && patientTable.getSelectedRow() >= 0) {
                patientIdField.setSelectedItem(String.valueOf(patientTable.getValueAt(patientTable.getSelectedRow(), 0)));
            }
        });
        JButton historyBtn = new JButton("View Medical History");
        historyBtn.addActionListener(evt -> viewSelectedPatientHistory());
        JPanel patientActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        patientActions.setOpaque(false);
        patientActions.add(historyBtn);
        patientPanel.add(patientActions, BorderLayout.SOUTH);

        centerPanel.add(appointmentPanel);
        centerPanel.add(patientPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(3, 1, 12, 12));
        formPanel.setOpaque(false);

        JPanel recordPanel = new JPanel(new BorderLayout(10, 10));
        recordPanel.setBackground(new Color(248, 249, 251));
        recordPanel.setBorder(createSectionBorder("Consultation / Vital Signs"));
        JPanel recordFields = new JPanel(new GridBagLayout());
        recordFields.setOpaque(false);
        patientIdField = new JComboBox<>();
        patientIdField.setEditable(false);
        bloodPressureField = new PlaceholderTextField("Example: 120/80");
        heartRateField = new PlaceholderTextField("Example: 72");
        temperatureField = new PlaceholderTextField("Example: 36.8");
        notesArea = new PlaceholderTextArea("Example: Patient reports mild headache", 4, 20);
        notesArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(190, 198, 210)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        addLabeledField(recordFields, "Patient ID", patientIdField, 0, 0, 180, 28);
        addLabeledField(recordFields, "Blood Pressure", bloodPressureField, 2, 0, 180, 28);
        addLabeledField(recordFields, "Heart Rate", heartRateField, 0, 1, 180, 28);
        addLabeledField(recordFields, "Temperature", temperatureField, 2, 1, 180, 28);

        recordPanel.add(recordFields, BorderLayout.NORTH);
        recordPanel.add(createScrollPane(notesArea), BorderLayout.CENTER);
        JButton saveRecordBtn = new JButton("Save Medical Record");
        saveRecordBtn.setBackground(new Color(176, 208, 236));
        saveRecordBtn.setForeground(new Color(26, 38, 53));
        saveRecordBtn.setFocusPainted(false);
        saveRecordBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveRecordBtn.addActionListener(evt -> saveMedicalRecord());
        recordPanel.add(saveRecordBtn, BorderLayout.SOUTH);

        JPanel prescriptionPanel = new JPanel(new BorderLayout(10, 10));
        prescriptionPanel.setBackground(new Color(248, 249, 251));
        prescriptionPanel.setBorder(createSectionBorder("Prescription"));
        JPanel prescriptionFields = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        prescriptionFields.setOpaque(false);
        medicationField = new JComboBox<>(loadAvailableMedications());
        dosageField = new PlaceholderTextField("Example: 500");
        dosageUnitField = new JComboBox<>(new String[]{"mg", "g", "ml", "L"});
        prescriptionArea = new PlaceholderTextArea("Example: Take with food for 7 days", 3, 30);
        prescriptionArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(190, 198, 210)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        prescriptionFields.add(createFieldLabel("Medication"));
        medicationField.setPreferredSize(new Dimension(230, 28));
        prescriptionFields.add(medicationField);
        prescriptionFields.add(createFieldLabel("Dosage"));
        dosageField.setPreferredSize(new Dimension(120, 28));
        prescriptionFields.add(dosageField);
        prescriptionFields.add(createFieldLabel("Unit"));
        dosageUnitField.setPreferredSize(new Dimension(75, 28));
        prescriptionFields.add(dosageUnitField);
        prescriptionPanel.add(prescriptionFields, BorderLayout.NORTH);
        prescriptionPanel.add(createScrollPane(prescriptionArea), BorderLayout.CENTER);
        JButton issuePrescriptionBtn = new JButton("Issue Prescription");
        issuePrescriptionBtn.setBackground(new Color(176, 208, 236));
        issuePrescriptionBtn.setForeground(new Color(26, 38, 53));
        issuePrescriptionBtn.setFocusPainted(false);
        issuePrescriptionBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        issuePrescriptionBtn.addActionListener(evt -> issuePrescription());
        prescriptionPanel.add(issuePrescriptionBtn, BorderLayout.SOUTH);

        JPanel labPanel = new JPanel(new BorderLayout(10, 10));
        labPanel.setBackground(new Color(248, 249, 251));
        labPanel.setBorder(createSectionBorder("Lab / Imaging Request"));
        JPanel labFields = new JPanel(new GridBagLayout());
        labFields.setOpaque(false);
        labTestField = new JComboBox<>(loadAvailableOptions("test.txt"));
        labNotesArea = new PlaceholderTextArea("Example: Fasting required", 3, 30);
        labNotesArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(190, 198, 210)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        addLabeledField(labFields, "Test / Imaging Type", labTestField, 0, 0, 260, 28);
        labPanel.add(labFields, BorderLayout.NORTH);
        labPanel.add(createScrollPane(labNotesArea), BorderLayout.CENTER);
        JButton labBtn = new JButton("Request Lab Test");
        labBtn.setBackground(new Color(176, 208, 236));
        labBtn.setForeground(new Color(26, 38, 53));
        labBtn.setFocusPainted(false);
        labBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labBtn.addActionListener(evt -> requestLabTest());
        labPanel.add(labBtn, BorderLayout.SOUTH);

        formPanel.add(recordPanel);
        formPanel.add(prescriptionPanel);
        formPanel.add(labPanel);

        mainPanel.add(formPanel, BorderLayout.SOUTH);
        JScrollPane dashboardScrollPane = createScrollPane(mainPanel);
        dashboardScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dashboardScrollPane.setBorder(null);
        dashboardScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        dashboardScrollPane.getViewport().setBackground(new Color(236, 239, 244));
        add(dashboardScrollPane);

        JButton profileBtn = new JButton("Edit Profile");
        profileBtn.setBackground(new Color(205, 220, 236));
        profileBtn.setForeground(new Color(30, 40, 50));
        profileBtn.setFocusPainted(false);
        profileBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        profileBtn.addActionListener(evt -> openProfile());
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(94, 104, 118));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.addActionListener(evt -> logout());
        JButton summaryBtn = new JButton("View Summary");
        summaryBtn.addActionListener(evt -> showDoctorSummary());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.add(summaryBtn);
        footer.add(profileBtn);
        footer.add(logoutBtn);
        mainPanel.add(footer, BorderLayout.EAST);

        pack();
    }

    private javax.swing.border.Border createSectionBorder(String title) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(177, 194, 214), 1),
                        title,
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                BorderFactory.createEmptyBorder(12, 10, 10, 10)
        );
    }

    private void addLabeledField(JPanel panel, String labelText, JComponent field, int x, int y, int width, int height) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setPreferredSize(new Dimension(120, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        panel.add(label, gbc);

        gbc.gridx = x + 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        field.setPreferredSize(new Dimension(width, height));
        field.setMinimumSize(new Dimension(width, height));
        panel.add(field, gbc);
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }

    private JScrollPane createScrollPane(java.awt.Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private String[] loadAvailableOptions(String fileName) {
        ArrayList<String> options = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) {
            return new String[0];
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",", 2);
                if (parts.length == 2 && "available".equalsIgnoreCase(parts[1].trim())
                        && !parts[0].trim().isEmpty()) {
                    options.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to read " + fileName + ".");
        }
        return options.toArray(new String[0]);
    }

    private String[] loadAvailableMedications() {
        ArrayList<String> medications = new ArrayList<>();
        File file = new File("medication.txt");
        if (!file.exists()) {
            return new String[0];
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",", 4);
                if (parts.length == 4 && "available".equalsIgnoreCase(parts[3].trim())
                        && !parts[0].trim().isEmpty()
                        && !parts[1].trim().isEmpty()
                        && !parts[2].trim().isEmpty()) {
                    medications.add(parts[0].trim() + " | Brand: " + parts[1].trim()
                            + " | Formula: " + parts[2].trim());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to read medication.txt.");
        }
        return medications.toArray(new String[0]);
    }

    private boolean containsComboItem(JComboBox<String> comboBox, String value) {
        for (int index = 0; index < comboBox.getItemCount(); index++) {
            if (value.equals(comboBox.getItemAt(index))) {
                return true;
            }
        }
        return false;
    }

    private String getSelectedComboValue(JComboBox<String> comboBox) {
        Object selectedItem = comboBox.getSelectedItem();
        return selectedItem == null ? "" : selectedItem.toString().trim();
    }

    private void loadDoctorData() {
        DefaultTableModel appointmentModel = (DefaultTableModel) appointmentTable.getModel();
        appointmentModel.setRowCount(0);
        ArrayList<Appointment> appointments = doctorService.getAppointmentsForDoctor(currentDoctor.getUserId());
        for (Appointment appointment : appointments) {
            String patientName = getPatientName(appointment.getPatientId());
            appointmentModel.addRow(new Object[]{
                    appointment.getAppointmentId(),
                    appointment.getAppointmentDate(),
                    appointment.getAppointmentTime(),
                    patientName,
                    appointment.getStatus()
            });
        }

        DefaultTableModel patientModel = (DefaultTableModel) patientTable.getModel();
        patientModel.setRowCount(0);
        ArrayList<Patient> patients = doctorService.getPatientsForDoctor(currentDoctor.getUserId());
        for (Patient patient : patients) {
            patientModel.addRow(new Object[]{
                    patient.getUserId(),
                    patient.getName(),
                    patient.getContactnumber()
            });
        }

        String selectedPatientId = getSelectedPatientId();
        patientIdField.removeAllItems();
        int activeAppointmentCount = 0;
        for (Appointment appointment : appointments) {
            String status = appointment.getStatus();
            if (!"Completed".equalsIgnoreCase(status) && !"Cancelled".equalsIgnoreCase(status)
                    && !containsComboItem(patientIdField, appointment.getPatientId())) {
                patientIdField.addItem(appointment.getPatientId());
                activeAppointmentCount++;
            }
        }
        if (activeAppointmentCount == 0) {
            patientIdField.addItem("No appointment left");
            patientIdField.setSelectedItem("No appointment left");
        } else if (!selectedPatientId.isEmpty()) {
            patientIdField.setSelectedItem(selectedPatientId);
        }
    }

    private String getPatientName(String patientId) {
        for (User user : userDAO.loadAllUsers()) {
            if (user instanceof Patient && user.getUserId().equals(patientId)) {
                return user.getName();
            }
        }
        return patientId;
    }

    private String findPatientIdForAppointment(String appointmentId) {
        for (Appointment appointment : doctorService.getAppointmentsForDoctor(currentDoctor.getUserId())) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                return appointment.getPatientId();
            }
        }
        return "";
    }

    private boolean canAccessPatient(String patientId) {
        return doctorService.canAccessPatient(currentDoctor.getUserId(), patientId);
    }

    private void saveMedicalRecord() {
        String patientId = getSelectedPatientId();
        if (patientId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a patient first.");
            return;
        }
        if (!canAccessPatient(patientId)) {
            JOptionPane.showMessageDialog(this, "This patient is not assigned to your schedule.");
            return;
        }

        String bp = bloodPressureField.getText().trim();
        String heartRate = heartRateField.getText().trim();
        String temperature = temperatureField.getText().trim();
        String notes = notesArea.getText().trim();

        if (bp.isEmpty() || heartRate.isEmpty() || temperature.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all vital sign fields.");
            return;
        }
        if (!areVitalsValid(bp, heartRate, temperature)) {
            JOptionPane.showMessageDialog(this, "Enter valid vitals: blood pressure 60/30-250/150, heart rate 20-250, temperature 30-45 C.");
            return;
        }

        String grade = BillingService.getMedicalGrade(bp, heartRate, temperature);
        MedicalRecord record = new MedicalRecord(
                DoctorService.generateId("MR"),
                patientId,
                currentDoctor.getUserId(),
                DoctorService.today(),
                bp,
                heartRate,
                temperature,
                notes.isEmpty() ? "No additional notes (Grade " + grade + ")" : notes + " (Grade " + grade + ")"
        );

        doctorService.saveMedicalRecord(record);
        JOptionPane.showMessageDialog(this, "Medical record saved successfully. Grade: " + grade);
        clearRecordFields();
    }

    private void issuePrescription() {
        String patientId = getSelectedPatientId();
        if (patientId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a patient first.");
            return;
        }
        if (!canAccessPatient(patientId)) {
            JOptionPane.showMessageDialog(this, "This patient is not assigned to your schedule.");
            return;
        }

        String medication = getSelectedComboValue(medicationField);
        String dosage = dosageField.getText().trim();
        String dosageUnit = getSelectedComboValue(dosageUnitField);
        String instructions = prescriptionArea.getText().trim();
        if (medication.isEmpty() || dosage.isEmpty() || dosageUnit.isEmpty() || instructions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill medication, dosage, and instructions.");
            return;
        }
        try {
            if (Double.parseDouble(dosage) <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Dosage must be a positive number.");
            return;
        }

        Prescription prescription = new Prescription(
                DoctorService.generateId("RX"),
                patientId,
                currentDoctor.getUserId(),
                medication,
                dosage + " " + dosageUnit,
                instructions,
                DoctorService.today()
        );

        doctorService.createPrescription(prescription);
        JOptionPane.showMessageDialog(this, "Prescription issued successfully.");
        clearPrescriptionFields();
    }

    private void requestLabTest() {
        String patientId = getSelectedPatientId();
        if (patientId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a patient first.");
            return;
        }
        if (!canAccessPatient(patientId)) {
            JOptionPane.showMessageDialog(this, "This patient is not assigned to your schedule.");
            return;
        }

        String testType = getSelectedComboValue(labTestField);
        String notes = labNotesArea.getText().trim();
        if (testType.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please specify a lab or imaging test.");
            return;
        }

        LabRequest request = new LabRequest(
                DoctorService.generateId("LAB"),
                patientId,
                currentDoctor.getUserId(),
                testType,
                notes.isEmpty() ? "Requested by doctor" : notes,
                DoctorService.today()
        );

        doctorService.requestLabTest(request);
        JOptionPane.showMessageDialog(this, "Lab request submitted successfully.");
        clearLabFields();
    }

    private void completeSelectedAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to complete.");
            return;
        }

        String appointmentId = String.valueOf(appointmentTable.getValueAt(selectedRow, 0));
        if (doctorService.completeAppointment(appointmentId, currentDoctor.getUserId())) {
            JOptionPane.showMessageDialog(this, "Appointment marked as completed.");
            loadDoctorData();
        } else {
            JOptionPane.showMessageDialog(this, "This appointment cannot be completed.");
        }
    }

    private void cancelSelectedAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to cancel.");
            return;
        }

        String appointmentId = String.valueOf(appointmentTable.getValueAt(selectedRow, 0));
        int choice = JOptionPane.showConfirmDialog(this, "Cancel this appointment?", "Confirm cancellation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            doctorService.cancelAppointment(appointmentId, currentDoctor.getUserId());
            loadDoctorData();
        }
    }

    private void rescheduleSelectedAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to reschedule.");
            return;
        }

        String date = JOptionPane.showInputDialog(this, "Enter the new date (YYYY-MM-DD):");
        if (date == null || date.trim().isEmpty()) {
            return;
        }
        String time = JOptionPane.showInputDialog(this, "Enter the new time (HH:MM):");
        if (time == null || time.trim().isEmpty()) {
            return;
        }
        date = date.trim();
        time = time.trim();
        try {
            java.time.LocalDate.parse(date);
            java.time.LocalTime.parse(time);
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Use date format YYYY-MM-DD and time format HH:MM.");
            return;
        }

        String appointmentId = String.valueOf(appointmentTable.getValueAt(selectedRow, 0));
        if (doctorService.rescheduleAppointment(appointmentId, currentDoctor.getUserId(), date, time)) {
            JOptionPane.showMessageDialog(this, "Appointment rescheduled successfully.");
            loadDoctorData();
        } else {
            JOptionPane.showMessageDialog(this, "That time is already booked or the appointment cannot be rescheduled.");
        }
    }

    private boolean areVitalsValid(String bloodPressure, String heartRate, String temperature) {
        String[] pressureParts = bloodPressure.split("/");
        if (pressureParts.length != 2) {
            return false;
        }
        try {
            int systolic = Integer.parseInt(pressureParts[0].trim());
            int diastolic = Integer.parseInt(pressureParts[1].trim());
            int heartRateValue = Integer.parseInt(heartRate.trim());
            double temperatureValue = Double.parseDouble(temperature.trim());
            return systolic >= 60 && systolic <= 250
                    && diastolic >= 30 && diastolic <= 150
                    && systolic > diastolic
                    && heartRateValue >= 20 && heartRateValue <= 250
                    && temperatureValue >= 30.0 && temperatureValue <= 45.0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void viewSelectedPatientHistory() {
        String patientId = getSelectedPatientId();
        if (patientId.isEmpty() || !canAccessPatient(patientId)) {
            JOptionPane.showMessageDialog(this, "Please select an assigned patient first.");
            return;
        }

        StringBuilder history = new StringBuilder();
        for (MedicalRecord record : doctorService.getMedicalRecordsForPatient(patientId)) {
            history.append("Date: ").append(record.getRecordDate())
                    .append("\nBlood pressure: ").append(record.getBloodPressure())
                    .append("\nHeart rate: ").append(record.getHeartRate())
                    .append("\nTemperature: ").append(record.getTemperature())
                    .append("\nNotes: ").append(record.getNotes())
                    .append("\n\n");
        }
        if (history.length() == 0) {
            history.append("No medical records found for this patient.");
        }

        JTextArea historyArea = new JTextArea(history.toString(), 14, 45);
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);
        JOptionPane.showMessageDialog(this, createScrollPane(historyArea), "Medical History - " + patientId, JOptionPane.INFORMATION_MESSAGE);
    }

    private String getSelectedPatientId() {
        String selectedPatientId = getSelectedComboValue(patientIdField);
        if (!selectedPatientId.isEmpty() && !"No appointment left".equals(selectedPatientId)) {
            return selectedPatientId;
        }
        if (patientTable.getSelectedRow() >= 0) {
            return String.valueOf(patientTable.getValueAt(patientTable.getSelectedRow(), 0));
        }
        return "";
    }

    private void openProfile() {
        new DoctorProfileFrame(currentDoctor).setVisible(true);
    }

    private void showDoctorSummary() {
        int appointments = doctorService.getAppointmentsForDoctor(currentDoctor.getUserId()).size();
        int patients = doctorService.getPatientsForDoctor(currentDoctor.getUserId()).size();
        int records = doctorService.countMedicalRecordsForDoctor(currentDoctor.getUserId());
        int prescriptions = doctorService.countPrescriptionsForDoctor(currentDoctor.getUserId());
        int labRequests = doctorService.countLabRequestsForDoctor(currentDoctor.getUserId());

        String summary = "Doctor: " + currentDoctor.getName()
                + "\nSpecialty: " + currentDoctor.getSpecialty()
                + "\n\nAppointments: " + appointments
                + "\nAssigned patients: " + patients
                + "\nMedical records: " + records
                + "\nPrescriptions: " + prescriptions
                + "\nLab requests: " + labRequests;
        JOptionPane.showMessageDialog(this, summary, "Doctor Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearRecordFields() {
        patientIdField.setSelectedItem(null);
        bloodPressureField.setText("");
        heartRateField.setText("");
        temperatureField.setText("");
        notesArea.setText("");
    }

    private void clearPrescriptionFields() {
        medicationField.setSelectedItem(null);
        dosageField.setText("");
        dosageUnitField.setSelectedItem("mg");
        prescriptionArea.setText("");
    }

    private void clearLabFields() {
        labTestField.setSelectedItem(null);
        labNotesArea.setText("");
    }

    private void logout() {
        this.dispose();
        new hms.gui.LoginFrame().setVisible(true);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            Doctor doctor = new Doctor("D001", "doctor01", "1234", "Dr. Smith", "General Medicine");
            new DoctorDashboard(doctor).setVisible(true);
        });
    }
}
