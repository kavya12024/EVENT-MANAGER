package ui;

import db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewEventParticipants extends JFrame {
    private JComboBox<String> eventCombo;
    private JButton viewBtn;
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewEventParticipants() {
        setTitle("View Event-wise Participants");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Event-wise Participants (Using Stored Procedure)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Filter Panel
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(new Color(240, 240, 240));

        JLabel eventLabel = new JLabel("Select Event:");
        eventLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        eventCombo = new JComboBox<>();
        loadEvents();

        viewBtn = new JButton("View Participants");
        viewBtn.setFont(new Font("Arial", Font.BOLD, 12));
        viewBtn.setBackground(new Color(52, 152, 219));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFocusPainted(false);
        viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadEventParticipants();
            }
        });

        filterPanel.add(eventLabel);
        filterPanel.add(eventCombo);
        filterPanel.add(viewBtn);

        // Table
        String[] columns = {"Participant ID", "Participant Name", "Email", "Score", "Registration Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(filterPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void loadEvents() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT event_id, event_name FROM event ORDER BY event_name";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                eventCombo.addItem(rs.getInt("event_id") + " - " + rs.getString("event_name"));
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    private void loadEventParticipants() {
        tableModel.setRowCount(0);

        if (eventCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select an event!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String eventStr = eventCombo.getSelectedItem().toString();
        int eventId = Integer.parseInt(eventStr.split(" - ")[0]);

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "CALL GetEventParticipants(?)";
            CallableStatement cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, eventId);
            ResultSet rs = cstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {rs.getInt("participant_id"), rs.getString("participant_name"), 
                                rs.getString("email"), rs.getInt("score"), rs.getTimestamp("registration_time")};
                tableModel.addRow(row);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No participants found for this event!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

            rs.close();
            cstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading participants: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
}
