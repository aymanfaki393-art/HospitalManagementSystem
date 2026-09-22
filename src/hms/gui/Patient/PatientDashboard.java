package hms.gui.Patient;

import hms.gui.LoginFrame;
import hms.model.Appointment;
import hms.model.Doctor;
import hms.model.DoctorSlot;
import hms.model.Feedback;
import hms.model.MedicalRecord;
import hms.model.Patient;
import hms.model.PatientProfile;
import hms.model.Prescription;
import hms.service.AppointmentService;
import hms.service.FeedbackService;
import hms.service.PatientService;
import hms.service.ServiceResult;
import hms.util.AppLogger;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * The main window presented to a patient after a successful login.
 *
 * <p>Every functionality listed for the Patients role in Table 1.0 of the coursework
 * specification is reachable from one of its tabs:</p>
 *
 * <ul>
 *   <li><b>My Profile</b> &ndash; edit personal details and change the login password;</li>
 *   <li><b>Book Appointment</b> &ndash; browse the available slots of doctors' consultations
 *       and book one;</li>
 *   <li><b>My Appointments</b> &ndash; reschedule or cancel an existing booking;</li>
 *   <li><b>Medical History</b> &ndash; view the clinical records written by doctors;</li>
 *   <li><b>Prescriptions</b> &ndash; view the medication issued;</li>
 *   <li><b>Feedback</b> &ndash; submit ratings and comments about doctors and clinic visits.</li>
 * </ul>
 *
 * <p><b>Note for the development team:</b> this window is written by hand rather than by the
 * NetBeans Form Editor, because a tabbed interface of this size is clearer and easier to
 * review as plain Java. There is deliberately no {@code .form} file; please edit this class
 * in the source editor rather than the design view.</p>
 *
 * <p>The class contains presentation logic only. Every rule about what a patient may or may
 * not do lives in {@link PatientService}, {@link AppointmentService} and
 * {@link FeedbackService}, which return a {@link ServiceResult} that this window simply
 * displays. That separation keeps the user interface thin and the rules testable.</p>
 *
 * @author Patient module
 */
public class PatientDashboard extends JFrame {

    /** The patient who is currently logged in. Never {@code null}. */
    private final Patient patient;

    /** Profile, history and prescription operations. */
    private final PatientService patientService = new PatientService();

    /** Booking, rescheduling and cancellation operations. */
    private final AppointmentService appointmentService = new AppointmentService();

    /** Rating and comment operations. */
    private final FeedbackService feedbackService = new FeedbackService();

    // ---- Profile tab -------------------------------------------------------------------

    /** Editable full name. */
    private JTextField nameField;

    /** Editable contact number. */
    private JTextField contactField;

    /** Editable e-mail address. */
    private JTextField emailField;

    /** Editable date of birth. */
    private JTextField dobField;

    /** Editable residential address. */
    private JTextField addressField;

    /** Current password, required before a password change. */
    private JPasswordField currentPasswordField;

    /** Requested new password. */
    private JPasswordField newPasswordField;

    /** Confirmation of the requested new password. */
    private JPasswordField confirmPasswordField;

    // ---- Booking tab -------------------------------------------------------------------

    /** Filters the slot list by doctor. */
    private JComboBox<String> doctorFilterCombo;

    /** Slots currently open for booking. */
    private JTable slotsTable;

    /** Backing data for {@link #slotsTable}. */
    private DefaultTableModel slotsModel;

    /** The slot identifiers behind each displayed row. */
    private List<DoctorSlot> displayedSlots;

    /** The patient's stated reason for the visit. */
    private JTextArea reasonArea;

    // ---- Appointments tab --------------------------------------------------------------

    /** The patient's own bookings. */
    private JTable appointmentsTable;

    /** Backing data for {@link #appointmentsTable}. */
    private DefaultTableModel appointmentsModel;

    /** The appointments behind each displayed row. */
    private List<Appointment> displayedAppointments;

    // ---- History and prescriptions tabs -------------------------------------------------

    /** Clinical records written about the patient. */
    private JTable historyTable;

    /** Backing data for {@link #historyTable}. */
    private DefaultTableModel historyModel;

    /** Medication issued to the patient. */
    private JTable prescriptionsTable;

    /** Backing data for {@link #prescriptionsTable}. */
    private DefaultTableModel prescriptionsModel;

    // ---- Feedback tab ------------------------------------------------------------------

    /** Consultations that have taken place and may still be rated. */
    private JComboBox<String> rateableCombo;

    /** The appointments behind each entry of {@link #rateableCombo}. */
    private List<Appointment> rateableAppointments;

    /** Star rating from 1 to 5. */
    private JComboBox<String> ratingCombo;

    /** Free-text comments about the visit. */
    private JTextArea commentsArea;

    /** Feedback the patient has already submitted. */
    private JTable feedbackTable;

    /** Backing data for {@link #feedbackTable}. */
    private DefaultTableModel feedbackModel;

    /**
     * Builds and populates the dashboard for a logged-in patient.
     *
     * @param patient the authenticated patient; must not be {@code null}
     * @throws IllegalArgumentException when no patient is supplied
     */
    public PatientDashboard(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("A PatientDashboard requires a logged-in patient.");
        }
        this.patient = patient;

        setTitle("APU Medical Centre - Patient Portal");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(920, 620);
        setMinimumSize(new Dimension(820, 560));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                confirmAndLogout();
            }
        });

        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("My Profile", buildProfileTab());
        tabs.addTab("Book Appointment", buildBookingTab());
        tabs.addTab("My Appointments", buildAppointmentsTab());
        tabs.addTab("Medical History", buildHistoryTab());
        tabs.addTab("Prescriptions", buildPrescriptionsTab());
        tabs.addTab("Feedback", buildFeedbackTab());
        add(tabs, BorderLayout.CENTER);

        loadProfile();
        refreshSlots();
        refreshAppointments();
        refreshHistory();
        refreshPrescriptions();
        refreshFeedback();

        AppLogger.info("PatientDashboard", "Patient " + patient.getUserId() + " opened the portal");
    }

    // ================================================================================
    // Layout construction
    // ================================================================================

    /**
     * Builds the banner shown above the tabs.
     *
     * @return the header panel
     */
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 102, 153));
        header.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel title = new JLabel("Welcome, " + patient.getName());
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        JLabel subtitle = new JLabel("Patient ID: " + patient.getUserId());
        subtitle.setForeground(new Color(215, 235, 245));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(title);
        text.add(subtitle);

        JButton logout = new JButton("Log Out");
        logout.addActionListener(event -> confirmAndLogout());

        header.add(text, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);
        return header;
    }

    /**
     * Builds the tab on which the patient edits personal details and changes the password.
     *
     * @return the profile panel
     */
    private JPanel buildProfileTab() {
        nameField = new JTextField(24);
        contactField = new JTextField(24);
        emailField = new JTextField(24);
        dobField = new JTextField(24);
        addressField = new JTextField(24);

        JPanel details = new JPanel(new GridBagLayout());
        details.setBorder(BorderFactory.createTitledBorder("Personal Details"));
        addLabelledField(details, 0, "Full name:", nameField);
        addLabelledField(details, 1, "Contact number:", contactField);
        addLabelledField(details, 2, "E-mail:", emailField);
        addLabelledField(details, 3, "Date of birth (yyyy-MM-dd):", dobField);
        addLabelledField(details, 4, "Address:", addressField);

        JButton saveDetails = new JButton("Save Details");
        saveDetails.addActionListener(event -> saveProfile());
        JButton reloadDetails = new JButton("Discard Changes");
        reloadDetails.addActionListener(event -> loadProfile());

        JPanel detailButtons = new JPanel();
        detailButtons.add(saveDetails);
        detailButtons.add(reloadDetails);
        addWide(details, 5, detailButtons);

        currentPasswordField = new JPasswordField(24);
        newPasswordField = new JPasswordField(24);
        confirmPasswordField = new JPasswordField(24);

        JPanel security = new JPanel(new GridBagLayout());
        security.setBorder(BorderFactory.createTitledBorder("Change Password"));
        addLabelledField(security, 0, "Current password:", currentPasswordField);
        addLabelledField(security, 1, "New password:", newPasswordField);
        addLabelledField(security, 2, "Confirm new password:", confirmPasswordField);

        JButton changePassword = new JButton("Change Password");
        changePassword.addActionListener(event -> changePassword());
        JPanel securityButtons = new JPanel();
        securityButtons.add(changePassword);
        addWide(security, 3, securityButtons);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.add(details);
        content.add(Box.createVerticalStrut(12));
        content.add(security);
        content.add(Box.createVerticalGlue());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(content, BorderLayout.NORTH);
        return wrapper;
    }

    /**
     * Builds the tab on which the patient browses and books consultation slots.
     *
     * @return the booking panel
     */
    private JPanel buildBookingTab() {
        doctorFilterCombo = new JComboBox<>();
        doctorFilterCombo.addItem("All doctors");
        for (Doctor doctor : appointmentService.getAllDoctors()) {
            doctorFilterCombo.addItem(doctor.getName() + " [" + doctor.getUserId() + "]");
        }
        doctorFilterCombo.addActionListener(event -> refreshSlots());

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(event -> refreshSlots());

        JPanel filters = new JPanel();
        filters.add(new JLabel("Show slots for:"));
        filters.add(doctorFilterCombo);
        filters.add(refresh);

        slotsModel = readOnlyModel(new String[]{"Slot", "Doctor", "Date", "Time"});
        slotsTable = buildTable(slotsModel);

        reasonArea = new JTextArea(3, 40);
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);

        JButton book = new JButton("Book Selected Slot");
        book.addActionListener(event -> bookSelectedSlot());

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setBorder(BorderFactory.createTitledBorder("Reason for visit"));
        bottom.add(new JScrollPane(reasonArea), BorderLayout.CENTER);
        JPanel bookPanel = new JPanel();
        bookPanel.add(book);
        bottom.add(bookPanel, BorderLayout.SOUTH);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(filters, BorderLayout.NORTH);
        panel.add(new JScrollPane(slotsTable), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Builds the tab on which the patient reviews, reschedules and cancels bookings.
     *
     * @return the appointments panel
     */
    private JPanel buildAppointmentsTab() {
        appointmentsModel = readOnlyModel(
                new String[]{"Appointment", "Date", "Time", "Doctor", "Status", "Reason"});
        appointmentsTable = buildTable(appointmentsModel);

        JButton reschedule = new JButton("Reschedule");
        reschedule.addActionListener(event -> rescheduleSelected());
        JButton cancel = new JButton("Cancel Booking");
        cancel.addActionListener(event -> cancelSelected());
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(event -> refreshAppointments());

        JPanel buttons = new JPanel();
        buttons.add(reschedule);
        buttons.add(cancel);
        buttons.add(refresh);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(new JScrollPane(appointmentsTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Builds the read-only tab listing clinical records.
     *
     * @return the medical-history panel
     */
    private JPanel buildHistoryTab() {
        historyModel = readOnlyModel(
                new String[]{"Record", "Date", "Doctor", "Vitals", "Diagnosis", "Notes"});
        historyTable = buildTable(historyModel);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(event -> refreshHistory());
        JPanel buttons = new JPanel();
        buttons.add(refresh);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(new JLabel("Clinical records entered by your doctors. This view is read-only."),
                BorderLayout.NORTH);
        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Builds the read-only tab listing prescriptions.
     *
     * @return the prescriptions panel
     */
    private JPanel buildPrescriptionsTab() {
        prescriptionsModel = readOnlyModel(
                new String[]{"Prescription", "Date", "Doctor", "Medication", "Dosage", "Instructions"});
        prescriptionsTable = buildTable(prescriptionsModel);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(event -> refreshPrescriptions());
        JPanel buttons = new JPanel();
        buttons.add(refresh);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(new JLabel("Medication issued to you by your doctors. This view is read-only."),
                BorderLayout.NORTH);
        panel.add(new JScrollPane(prescriptionsTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Builds the tab on which the patient rates completed consultations.
     *
     * @return the feedback panel
     */
    private JPanel buildFeedbackTab() {
        rateableCombo = new JComboBox<>();
        ratingCombo = new JComboBox<>(new String[]{"5", "4", "3", "2", "1"});
        commentsArea = new JTextArea(4, 40);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Rate a completed consultation"));
        addLabelledField(form, 0, "Consultation:", rateableCombo);
        addLabelledField(form, 1, "Rating (5 = excellent):", ratingCombo);
        addLabelledField(form, 2, "Comments:", new JScrollPane(commentsArea));

        JButton submit = new JButton("Submit Feedback");
        submit.addActionListener(event -> submitFeedback());
        JPanel submitPanel = new JPanel();
        submitPanel.add(submit);
        addWide(form, 3, submitPanel);

        feedbackModel = readOnlyModel(new String[]{"Feedback", "Date", "Doctor", "Rating", "Comments"});
        feedbackTable = buildTable(feedbackModel);

        JPanel history = new JPanel(new BorderLayout());
        history.setBorder(BorderFactory.createTitledBorder("Feedback you have already submitted"));
        history.add(new JScrollPane(feedbackTable), BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(form, BorderLayout.NORTH);
        panel.add(history, BorderLayout.CENTER);
        return panel;
    }

    // ================================================================================
    // Profile behaviour
    // ================================================================================

    /** Loads the stored profile into the editable fields, discarding any unsaved edits. */
    private void loadProfile() {
        PatientProfile profile = patientService.getProfile(patient.getUserId());
        nameField.setText(patient.getName());
        contactField.setText(profile.getContactNumber());
        emailField.setText(profile.getEmail());
        dobField.setText(profile.getDateOfBirth());
        addressField.setText(profile.getAddress());
    }

    /** Validates and saves the personal details, reporting the outcome to the patient. */
    private void saveProfile() {
        ServiceResult nameResult = patientService.updateName(patient, nameField.getText());
        if (!nameResult.isSuccess()) {
            showError(nameResult.getMessage());
            return;
        }

        ServiceResult result = patientService.updateProfile(
                patient.getUserId(), contactField.getText(), emailField.getText(),
                dobField.getText(), addressField.getText());

        if (result.isSuccess()) {
            patient.setContactNumber(contactField.getText());
            showInfo(result.getMessage());
        } else {
            showError(result.getMessage());
        }
    }

    /** Validates and applies a password change, then clears the password fields. */
    private void changePassword() {
        ServiceResult result = patientService.changePassword(patient,
                new String(currentPasswordField.getPassword()),
                new String(newPasswordField.getPassword()),
                new String(confirmPasswordField.getPassword()));

        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");

        if (result.isSuccess()) {
            showInfo(result.getMessage());
        } else {
            showError(result.getMessage());
        }
    }

    // ================================================================================
    // Booking behaviour
    // ================================================================================

    /** Reloads the list of bookable slots, honouring the selected doctor filter. */
    private void refreshSlots() {
        if (slotsModel == null) {
            return;
        }
        displayedSlots = appointmentService.getAvailableSlots(selectedDoctorId());
        slotsModel.setRowCount(0);
        for (DoctorSlot slot : displayedSlots) {
            slotsModel.addRow(new Object[]{
                    slot.getSlotId(),
                    patientService.resolveUserName(slot.getDoctorId()),
                    slot.getDate(),
                    slot.getStartTime() + " - " + slot.getEndTime()
            });
        }
    }

    /** Books the slot highlighted in the table, using the reason the patient typed. */
    private void bookSelectedSlot() {
        int row = slotsTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a consultation slot from the list first.");
            return;
        }

        DoctorSlot slot = displayedSlots.get(row);
        ServiceResult result = appointmentService.book(
                patient.getUserId(), slot.getSlotId(), reasonArea.getText());

        if (result.isSuccess()) {
            reasonArea.setText("");
            refreshSlots();
            refreshAppointments();
            showInfo(result.getMessage());
        } else {
            showError(result.getMessage());
        }
    }

    // ================================================================================
    // Appointment behaviour
    // ================================================================================

    /** Reloads the patient's bookings, first promoting any past booking to completed. */
    private void refreshAppointments() {
        if (appointmentsModel == null) {
            return;
        }
        appointmentService.refreshCompletedAppointments(patient.getUserId());
        displayedAppointments = appointmentService.getAppointmentsForPatient(patient.getUserId());
        appointmentsModel.setRowCount(0);
        for (Appointment appointment : displayedAppointments) {
            appointmentsModel.addRow(new Object[]{
                    appointment.getAppointmentId(),
                    appointment.getDate(),
                    appointment.getTime(),
                    patientService.resolveUserName(appointment.getDoctorId()),
                    appointment.getStatus(),
                    appointment.getReason()
            });
        }
        refreshFeedbackChoices();
    }

    /** Offers the patient a list of alternative slots and moves the booking to the chosen one. */
    private void rescheduleSelected() {
        Appointment appointment = selectedAppointment();
        if (appointment == null) {
            return;
        }
        if (!appointment.isActive()) {
            showError("Only an appointment that is still booked can be rescheduled.");
            return;
        }

        List<DoctorSlot> options = appointmentService.getAvailableSlots();
        if (options.isEmpty()) {
            showError("There are no free consultation slots to move this appointment to.");
            return;
        }

        String[] labels = new String[options.size()];
        for (int i = 0; i < options.size(); i++) {
            DoctorSlot slot = options.get(i);
            labels[i] = slot.getDate() + "  " + slot.getStartTime() + "-" + slot.getEndTime()
                    + "  with " + patientService.resolveUserName(slot.getDoctorId())
                    + "  [" + slot.getSlotId() + "]";
        }

        Object choice = JOptionPane.showInputDialog(this,
                "Choose the new consultation slot for " + appointment.getAppointmentId() + ":",
                "Reschedule Appointment", JOptionPane.QUESTION_MESSAGE, null, labels, labels[0]);
        if (choice == null) {
            return;
        }

        DoctorSlot chosen = options.get(indexOf(labels, choice.toString()));
        ServiceResult result = appointmentService.reschedule(
                patient.getUserId(), appointment.getAppointmentId(), chosen.getSlotId());

        if (result.isSuccess()) {
            refreshSlots();
            refreshAppointments();
            showInfo(result.getMessage());
        } else {
            showError(result.getMessage());
        }
    }

    /** Cancels the highlighted booking after asking the patient to confirm. */
    private void cancelSelected() {
        Appointment appointment = selectedAppointment();
        if (appointment == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Cancel appointment " + appointment.getAppointmentId()
                        + " on " + appointment.getDate() + " at " + appointment.getTime() + "?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        ServiceResult result = appointmentService.cancel(
                patient.getUserId(), appointment.getAppointmentId());

        if (result.isSuccess()) {
            refreshSlots();
            refreshAppointments();
            showInfo(result.getMessage());
        } else {
            showError(result.getMessage());
        }
    }

    // ================================================================================
    // History, prescription and feedback behaviour
    // ================================================================================

    /** Reloads the clinical records written about the patient. */
    private void refreshHistory() {
        if (historyModel == null) {
            return;
        }
        historyModel.setRowCount(0);
        for (MedicalRecord record : patientService.getMedicalHistory(patient.getUserId())) {
            historyModel.addRow(new Object[]{
                    record.getRecordId(),
                    record.getDate(),
                    patientService.resolveUserName(record.getDoctorId()),
                    record.getVitals(),
                    record.getDiagnosis(),
                    record.getNotes()
            });
        }
    }

    /** Reloads the prescriptions issued to the patient. */
    private void refreshPrescriptions() {
        if (prescriptionsModel == null) {
            return;
        }
        prescriptionsModel.setRowCount(0);
        for (Prescription prescription : patientService.getPrescriptions(patient.getUserId())) {
            prescriptionsModel.addRow(new Object[]{
                    prescription.getPrescriptionId(),
                    prescription.getDateIssued(),
                    patientService.resolveUserName(prescription.getDoctorId()),
                    prescription.getMedication(),
                    prescription.getDosage(),
                    prescription.getInstructions()
            });
        }
    }

    /** Reloads both the list of rateable consultations and the feedback already submitted. */
    private void refreshFeedback() {
        refreshFeedbackChoices();
        if (feedbackModel == null) {
            return;
        }
        feedbackModel.setRowCount(0);
        for (Feedback feedback : feedbackService.getFeedbackByPatient(patient.getUserId())) {
            feedbackModel.addRow(new Object[]{
                    feedback.getFeedbackId(),
                    feedback.getDate(),
                    patientService.resolveUserName(feedback.getDoctorId()),
                    feedback.getStars() + "  (" + feedback.getRating() + "/5)",
                    feedback.getComments()
            });
        }
    }

    /** Repopulates the drop-down of consultations that have taken place and are unrated. */
    private void refreshFeedbackChoices() {
        if (rateableCombo == null) {
            return;
        }
        rateableAppointments = feedbackService.getRateableAppointments(patient.getUserId());
        rateableCombo.removeAllItems();
        if (rateableAppointments.isEmpty()) {
            rateableCombo.addItem("No completed consultations awaiting feedback");
            rateableCombo.setEnabled(false);
            return;
        }
        rateableCombo.setEnabled(true);
        for (Appointment appointment : rateableAppointments) {
            rateableCombo.addItem(appointment.getAppointmentId() + "  " + appointment.getDate()
                    + " with " + patientService.resolveUserName(appointment.getDoctorId()));
        }
    }

    /** Validates and stores the rating and comment the patient entered. */
    private void submitFeedback() {
        if (rateableAppointments == null || rateableAppointments.isEmpty()) {
            showError("You have no completed consultations awaiting feedback.");
            return;
        }
        int index = rateableCombo.getSelectedIndex();
        if (index < 0) {
            showError("Please select the consultation you wish to rate.");
            return;
        }

        Appointment appointment = rateableAppointments.get(index);
        ServiceResult result = feedbackService.submitFeedback(
                patient.getUserId(), appointment.getAppointmentId(),
                (String) ratingCombo.getSelectedItem(), commentsArea.getText());

        if (result.isSuccess()) {
            commentsArea.setText("");
            refreshFeedback();
            showInfo(result.getMessage());
        } else {
            showError(result.getMessage());
        }
    }

    // ================================================================================
    // Shared helpers
    // ================================================================================

    /**
     * Returns the appointment highlighted in the appointments table.
     *
     * @return the selected appointment, or {@code null} when nothing is selected
     */
    private Appointment selectedAppointment() {
        int row = appointmentsTable.getSelectedRow();
        if (row < 0) {
            showError("Please select one of your appointments from the list first.");
            return null;
        }
        return displayedAppointments.get(row);
    }

    /**
     * Reads the doctor identifier out of the currently selected filter entry.
     *
     * @return the selected doctor's identifier, or {@code null} when all doctors are shown
     */
    private String selectedDoctorId() {
        if (doctorFilterCombo == null || doctorFilterCombo.getSelectedIndex() <= 0) {
            return null;
        }
        String label = (String) doctorFilterCombo.getSelectedItem();
        int open = label.lastIndexOf('[');
        int close = label.lastIndexOf(']');
        if (open < 0 || close <= open) {
            return null;
        }
        return label.substring(open + 1, close);
    }

    /**
     * Finds the position of a value in an array.
     *
     * @param values the array to search
     * @param target the value to find
     * @return the index of the first match, or {@code 0} when there is none
     */
    private int indexOf(String[] values, String target) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(target)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Creates a table model whose cells cannot be edited in place.
     *
     * @param columns the column headings
     * @return a read-only table model
     */
    private DefaultTableModel readOnlyModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /**
     * Creates a table configured for single-row selection.
     *
     * @param model the backing model
     * @return the configured table
     */
    private JTable buildTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(22);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return table;
    }

    /**
     * Adds a caption and its input control to a grid-bag form.
     *
     * @param panel   the form being built
     * @param row     the row to place the pair on
     * @param caption the caption text
     * @param field   the input control
     */
    private void addLabelledField(JPanel panel, int row, String caption, java.awt.Component field) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(6, 8, 6, 8);
        JLabel label = new JLabel(caption);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(label, labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(6, 8, 6, 8);
        panel.add(field, fieldConstraints);
    }

    /**
     * Adds a component spanning both columns of a grid-bag form.
     *
     * @param panel     the form being built
     * @param row       the row to place the component on
     * @param component the component to add
     */
    private void addWide(JPanel panel, int row, java.awt.Component component) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(4, 8, 4, 8);
        panel.add(component, constraints);
    }

    /**
     * Shows a confirmation message.
     *
     * @param message the text to display
     */
    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "APU Medical Centre",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows an error message.
     *
     * @param message the text to display
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Unable to continue",
                JOptionPane.WARNING_MESSAGE);
    }

    /** Asks the patient to confirm, then returns to the login window. */
    private void confirmAndLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Log out and return to the login screen?", "Confirm Log Out",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        AppLogger.info("PatientDashboard", "Patient " + patient.getUserId() + " logged out");
        dispose();
        new LoginFrame().setVisible(true);
    }
}
