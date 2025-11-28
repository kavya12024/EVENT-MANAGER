package ui;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterEvent extends JFrame {
    private int studentId;
    private JComboBox<String> eventCombo;
    private JButton registerBtn;
    private JButton cancelBtn;

    public RegisterEvent(int studentId) {
        this.studentId = studentId;

        setTitle("Register for Event");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Register for Event");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 240, 240));

        JLabel eventLabel = new JLabel("Select Event:");
        eventLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        eventCombo = new JComboBox<>();
        loadAvailableEvents();

        formPanel.add(eventLabel);
        formPanel.add(eventCombo);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Arial", Font.BOLD, 12));
        registerBtn.setBackground(new Color(46, 204, 113));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerForEvent();
            }
        });

        cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(registerBtn);
        buttonPanel.add(cancelBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void loadAvailableEvents() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT e.event_id, e.event_name, e.event_date FROM event e " +
                         "WHERE e.event_id NOT IN (SELECT event_id FROM participation WHERE participant_id = ?) " +
                         "ORDER BY e.event_date";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            boolean hasEvents = false;
            while (rs.next()) {
                hasEvents = true;
                eventCombo.addItem(rs.getInt("event_id") + " - " + rs.getString("event_name") + " (" + rs.getDate("event_date") + ")");
            }

            if (!hasEvents) {
                eventCombo.addItem("No available events");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    private void registerForEvent() {
        String selectedEvent = eventCombo.getSelectedItem().toString();

        if (selectedEvent.equals("No available events")) {
            JOptionPane.showMessageDialog(this, "No events available for registration!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int eventId = Integer.parseInt(selectedEvent.split(" - ")[0]);

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO participation (participant_id, event_id, score) VALUES (?, ?, 0)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, eventId);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Successfully registered for event!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
            pstmt.close();
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(this, "Already registered for this event!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
}
