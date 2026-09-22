package hms.gui.Doctor;

import hms.dao.UserDAO;
import hms.model.Doctor;
import java.awt.*;
import javax.swing.*;

public class DoctorProfileFrame extends JFrame {
    private final Doctor doctor;
    private final UserDAO userDAO = new UserDAO();

    private final JTextField nameField;
    private final JTextField usernameField;
    private final JTextField specialtyField;
    private final JPasswordField passwordField;

    public DoctorProfileFrame(Doctor doctor) {
        this.doctor = doctor;

        setTitle("Doctor Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Doctor ID:"));
        panel.add(new JLabel(doctor.getUserId()));

        panel.add(new JLabel("Name:"));
        nameField = new PlaceholderTextField("Example: Dr. Jane Smith");
        nameField.setText(doctor.getName());
        panel.add(nameField);

        panel.add(new JLabel("Username:"));
        usernameField = new PlaceholderTextField("Example: doctor01");
        usernameField.setText(doctor.getUserName());
        panel.add(usernameField);

        panel.add(new JLabel("Specialty:"));
        specialtyField = new PlaceholderTextField("Example: General Medicine");
        specialtyField.setText(doctor.getSpecialty());
        panel.add(specialtyField);

        panel.add(new JLabel("Password:"));
        passwordField = new PlaceholderPasswordField("Example: minimum 4 characters");
        passwordField.setText(doctor.getPassword());
        panel.add(passwordField);

        JButton saveBtn = new JButton("Save Profile");
        saveBtn.addActionListener(evt -> saveProfile());

        add(panel, BorderLayout.CENTER);
        add(saveBtn, BorderLayout.SOUTH);
    }

    private void saveProfile() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String specialty = specialtyField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || username.isEmpty() || specialty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, username, and specialty are required.");
            return;
        }
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must contain at least 4 characters.");
            return;
        }

        doctor.setName(name);
        doctor.setUserName(username);
        doctor.setSpecialty(specialty);
        doctor.setPassword(password);
        userDAO.updateUser(doctor);
        JOptionPane.showMessageDialog(this, "Profile updated successfully.");
        dispose();
    }
}
