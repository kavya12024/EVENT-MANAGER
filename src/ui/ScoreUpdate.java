package ui;

import db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreUpdate extends JFrame {
    private JComboBox<String> participantCombo;
    private JComboBox<String> eventCombo;
    private JSpinner scoreSpinner;
    private JButton updateBtn;
    private JButton cancelBtn;
    private JTable table;
    private DefaultTableModel tableModel;

    public ScoreUpdate() {
        setTitle("Update Score");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Update Participation Score");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(new Color(240, 240, 240));

        JLabel participantLabel = new JLabel("Participant:");
        participantLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        participantCombo = new JComboBox<>();
        loadParticipants();

        JLabel eventLabel = new JLabel("Event:");
        eventLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        eventCombo = new JComboBox<>();
        loadEvents();

        JLabel scoreLabel = new JLabel("Score:");
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        scoreSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

        formPanel.add(participantLabel);
        formPanel.add(participantCombo);
        formPanel.add(eventLabel);
        formPanel.add(eventCombo);
        formPanel.add(scoreLabel);
        formPanel.add(scoreSpinner);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        updateBtn = new JButton("Update Score");
        updateBtn.setFont(new Font("Arial", Font.BOLD, 12));
        updateBtn.setBackground(new Color(46, 204, 113));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateScore();
            }
        });

        cancelBtn = new JButton("Refresh");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.setBackground(new Color(52, 152, 219));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTable();
            }
        });

        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);

        // Table
        String[] columns = {"Participation ID", "Participant", "Event", "Current Score"};
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
        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        loadTable();
        add(mainPanel);
        setVisible(true);
    }

    private void loadParticipants() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT participant_id, participant_name FROM participant ORDER BY participant_name";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                participantCombo.addItem(rs.getInt("participant_id") + " - " + rs.getString("participant_name"));
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading participants: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnection.closeConnection(conn);
        }
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

    private void loadTable() {
        tableModel.setRowCount(0);
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT par.participation_id, p.participant_name, e.event_name, par.score FROM participation par " +
                         "JOIN participant p ON par.participant_id = p.participant_id " +
                         "JOIN event e ON par.event_id = e.event_id ORDER BY p.participant_name";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {rs.getInt("participation_id"), rs.getString("participant_name"), 
                                rs.getString("event_name"), rs.getInt("score")};
                tableModel.addRow(row);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    private void updateScore() {
        if (participantCombo.getSelectedItem() == null || eventCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select participant and event!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String participantStr = participantCombo.getSelectedItem().toString();
        String eventStr = eventCombo.getSelectedItem().toString();
        int score = (Integer) scoreSpinner.getValue();

        int participantId = Integer.parseInt(participantStr.split(" - ")[0]);
        int eventId = Integer.parseInt(eventStr.split(" - ")[0]);

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE participation SET score = ? WHERE participant_id = ? AND event_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, score);
            pstmt.setInt(2, participantId);
            pstmt.setInt(3, eventId);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Score updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this, "No participation record found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
}
