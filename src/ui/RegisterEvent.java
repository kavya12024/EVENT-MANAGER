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
    private JTextArea eventDetailsArea;
    private JButton registerBtn;
    private JButton cancelBtn;

    public RegisterEvent(int studentId) {
        this.studentId = studentId;

        setTitle("Register for Event");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
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
        formPanel.setLayout(new BorderLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(new Color(240, 240, 240));

        JLabel eventLabel = new JLabel("Select Event:");
        eventLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        eventCombo = new JComboBox<>();
        eventCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEventDetails();
            }
        });
        loadAvailableEvents();

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboPanel.setBackground(new Color(240, 240, 240));
        comboPanel.add(eventLabel);
        comboPanel.add(eventCombo);

        eventDetailsArea = new JTextArea(8, 60);
        eventDetailsArea.setEditable(false);
        eventDetailsArea.setLineWrap(true);
        eventDetailsArea.setWrapStyleWord(true);
        eventDetailsArea.setFont(new Font("Arial", Font.PLAIN, 11));
        eventDetailsArea.setBackground(new Color(255, 255, 255));
        eventDetailsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(eventDetailsArea);

        formPanel.add(comboPanel, BorderLayout.NORTH);
        formPanel.add(scrollPane, BorderLayout.CENTER);

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
            
            // Get student's email to find associated participant
            String studentSql = "SELECT email FROM student WHERE student_id = ?";
            PreparedStatement studentPstmt = conn.prepareStatement(studentSql);
            studentPstmt.setInt(1, studentId);
            ResultSet studentRs = studentPstmt.executeQuery();
            
            String email = null;
            if (studentRs.next()) {
                email = studentRs.getString("email");
            }
            studentRs.close();
            studentPstmt.close();
            
            if (email == null) {
                eventCombo.addItem("Error: Student not found");
                return;
            }
            
            // Get participant_id from email
            String participantSql = "SELECT participant_id FROM participant WHERE email = ?";
            PreparedStatement participantPstmt = conn.prepareStatement(participantSql);
            participantPstmt.setString(1, email);
            ResultSet participantRs = participantPstmt.executeQuery();
            
            int participantId = -1;
            if (participantRs.next()) {
                participantId = participantRs.getInt("participant_id");
            }
            participantRs.close();
            participantPstmt.close();
            
            // Load events - either registered or available
            String sql = "SELECT e.event_id, e.event_name, e.event_date FROM event e ";
            if (participantId > 0) {
                sql += "WHERE e.event_id NOT IN (SELECT event_id FROM participation WHERE participant_id = ?) ";
            }
            sql += "ORDER BY e.event_date";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            if (participantId > 0) {
                pstmt.setInt(1, participantId);
            }
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

    private void updateEventDetails() {
        String selectedEvent = eventCombo.getSelectedItem().toString();
        
        if (selectedEvent.equals("No available events") || selectedEvent.contains("Error")) {
            eventDetailsArea.setText("");
            return;
        }

        try {
            int eventId = Integer.parseInt(selectedEvent.split(" - ")[0]);
            
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT e.event_name, e.event_date, e.event_location, e.event_fees, e.event_description, o.organiser_name FROM event e " +
                        "LEFT JOIN organiser o ON e.organiser_id = o.organiser_id WHERE e.event_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                StringBuilder details = new StringBuilder();
                details.append("EVENT DETAILS\n");
                details.append("==========================================\n\n");
                details.append("Event Name: ").append(rs.getString("event_name")).append("\n");
                details.append("Date: ").append(rs.getDate("event_date")).append("\n");
                details.append("Location: ").append(rs.getString("event_location")).append("\n");
                details.append("Fees: ₹").append(rs.getDouble("event_fees")).append("\n");
                details.append("Organiser: ").append(rs.getString("organiser_name")).append("\n\n");
                details.append("Description:\n").append(rs.getString("event_description"));
                
                eventDetailsArea.setText(details.toString());
            }
            
            rs.close();
            pstmt.close();
            DBConnection.closeConnection(conn);
        } catch (Exception e) {
            eventDetailsArea.setText("Error loading event details: " + e.getMessage());
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
            
            // First, get student details and check if participant record exists
            String studentSql = "SELECT full_name, department_id, email FROM student WHERE student_id = ?";
            PreparedStatement studentPstmt = conn.prepareStatement(studentSql);
            studentPstmt.setInt(1, studentId);
            ResultSet studentRs = studentPstmt.executeQuery();
            
            if (!studentRs.next()) {
                JOptionPane.showMessageDialog(this, "Student record not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String studentName = studentRs.getString("full_name");
            int deptId = studentRs.getInt("department_id");
            String email = studentRs.getString("email");
            
            studentRs.close();
            studentPstmt.close();
            
            // Check if participant record already exists for this student
            String checkParticipantSql = "SELECT participant_id FROM participant WHERE email = ?";
            PreparedStatement checkPstmt = conn.prepareStatement(checkParticipantSql);
            checkPstmt.setString(1, email);
            ResultSet checkRs = checkPstmt.executeQuery();
            
            int participantId = -1;
            if (checkRs.next()) {
                participantId = checkRs.getInt("participant_id");
            }
            checkRs.close();
            checkPstmt.close();
            
            // If no participant record, create one
            if (participantId == -1) {
                String insertParticipantSql = "INSERT INTO participant (participant_name, department_id, email) VALUES (?, ?, ?)";
                PreparedStatement insertPstmt = conn.prepareStatement(insertParticipantSql, PreparedStatement.RETURN_GENERATED_KEYS);
                insertPstmt.setString(1, studentName);
                insertPstmt.setInt(2, deptId);
                insertPstmt.setString(3, email);
                insertPstmt.executeUpdate();
                
                ResultSet generatedKeys = insertPstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    participantId = generatedKeys.getInt(1);
                }
                generatedKeys.close();
                insertPstmt.close();
            }
            
            // Now register for the event using the participant_id
            String sql = "INSERT INTO participation (participant_id, event_id, score) VALUES (?, ?, 0)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, participantId);
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
