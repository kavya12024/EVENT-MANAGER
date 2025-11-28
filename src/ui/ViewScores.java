package ui;

import db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewScores extends JFrame {
    private int studentId;
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewScores(int studentId) {
        this.studentId = studentId;

        setTitle("View My Scores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        JLabel titleLabel = new JLabel("My Event Scores");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Table
        String[] columns = {"Event ID", "Event Name", "Score", "Registration Date", "Event Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        loadTable();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            
            // First get student email to find participant_id
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
                JOptionPane.showMessageDialog(this, "Student record not found!", "Error", JOptionPane.ERROR_MESSAGE);
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
            
            if (participantId <= 0) {
                JOptionPane.showMessageDialog(this, "No scores available yet!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String sql = "SELECT e.event_id, e.event_name, p.score, p.registration_time, e.event_date " +
                         "FROM participation p " +
                         "JOIN event e ON p.event_id = e.event_id " +
                         "WHERE p.participant_id = ? " +
                         "ORDER BY e.event_date DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, participantId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {rs.getInt("event_id"), rs.getString("event_name"), 
                                rs.getInt("score"), rs.getTimestamp("registration_time"),
                                rs.getDate("event_date")};
                tableModel.addRow(row);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No scores available yet!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading scores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
}
