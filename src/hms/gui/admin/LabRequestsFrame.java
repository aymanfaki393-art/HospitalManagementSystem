package hms.gui.admin;

import hms.dao.LabRequestDAO;
import hms.model.LabRequest;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class LabRequestsFrame extends JFrame {
    private final LabRequestDAO labRequestDAO = new LabRequestDAO();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Request ID", "Patient ID", "Doctor ID", "Test / Imaging", "Notes", "Date", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public LabRequestsFrame() {
        setTitle("Lab and Imaging Requests");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 420);
        setLocationRelativeTo(null);

        JTable requestTable = new JTable(tableModel);
        requestTable.setRowHeight(26);
        requestTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(evt -> loadRequests());
        JButton progressButton = new JButton("Mark In Progress");
        progressButton.addActionListener(evt -> updateSelectedRequest(requestTable, "In Progress"));
        JButton completeButton = new JButton("Mark Completed");
        completeButton.addActionListener(evt -> updateSelectedRequest(requestTable, "Completed"));
        JPanel actions = new JPanel();
        actions.add(refreshButton);
        actions.add(progressButton);
        actions.add(completeButton);

        add(new JScrollPane(requestTable), BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        loadRequests();
    }

    private void loadRequests() {
        tableModel.setRowCount(0);
        ArrayList<LabRequest> requests = labRequestDAO.loadAllLabRequests();
        for (LabRequest request : requests) {
            tableModel.addRow(new Object[]{
                    request.getRequestId(),
                    request.getPatientId(),
                    request.getDoctorId(),
                    request.getTestType(),
                    request.getNotes(),
                    request.getRequestDate(),
                    request.getStatus()
            });
        }
    }

    private void updateSelectedRequest(JTable requestTable, String status) {
        int selectedRow = requestTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a request first.");
            return;
        }
        String requestId = String.valueOf(requestTable.getValueAt(selectedRow, 0));
        if (labRequestDAO.updateRequestStatus(requestId, status)) {
            loadRequests();
        } else {
            JOptionPane.showMessageDialog(this, "Requests must move from Pending to In Progress to Completed.");
        }
    }
}
