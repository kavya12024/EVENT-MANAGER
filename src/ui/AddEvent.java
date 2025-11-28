package ui;

import db.DBConnection;
import models.Event;
import models.Organiser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddEvent extends JFrame {
    private JTextField eventNameField;
    private JTextField descriptionField;
    private JTextField maxParticipantsField;
    private JComboBox<Organiser> organiserCombo;
    private JTextField dateField;
    private JButton addBtn;
    private JButton cancelBtn;

    public AddEvent() {
        setTitle("Add Event");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Add Event");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 240, 240));

        JLabel eventLabel = new JLabel("Event Name:");
        eventLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        eventNameField = new JTextField();

        JLabel dateLabel = new JLabel("Event Date (YYYY-MM-DD):");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateField = new JTextField();

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionField = new JTextField();

        JLabel organiserLabel = new JLabel("Organiser:");
        organiserLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        organiserCombo = new JComboBox<>();
        loadOrganisers();

        JLabel maxLabel = new JLabel("Max Participants:");
        maxLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        maxParticipantsField = new JTextField("100");

        formPanel.add(eventLabel);
        formPanel.add(eventNameField);
        formPanel.add(dateLabel);
        formPanel.add(dateField);
        formPanel.add(descriptionLabel);
        formPanel.add(descriptionField);
        formPanel.add(organiserLabel);
        formPanel.add(organiserCombo);
        formPanel.add(maxLabel);
        formPanel.add(maxParticipantsField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        addBtn = new JButton("Add");
        addBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addBtn.setBackground(new Color(46, 204, 113));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEvent();
            }
        });

        cancelBtn = new JButton("Back");
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

        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void loadOrganisers() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT o.organiser_id, o.organiser_name, o.dept_id FROM organiser o ORDER BY o.organiser_name";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                organiserCombo.addItem(new Organiser(rs.getInt("organiser_id"), rs.getString("organiser_name"), rs.getInt("dept_id"), "", ""));
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading organisers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    private void addEvent() {
        String eventName = eventNameField.getText().trim();
        String date = dateField.getText().trim();
        String description = descriptionField.getText().trim();
        Organiser selectedOrganiser = (Organiser) organiserCombo.getSelectedItem();
        String maxParticipantsStr = maxParticipantsField.getText().trim();

        if (eventName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter event name!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter event date!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedOrganiser == null) {
            JOptionPane.showMessageDialog(this, "Please select an organiser!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Date.valueOf(date);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use YYYY-MM-DD", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maxParticipants;
        try {
            maxParticipants = Integer.parseInt(maxParticipantsStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Max participants must be a number!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO event (event_name, event_date, event_description, organiser_id, max_participants) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, eventName);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setString(3, description.isEmpty() ? null : description);
            pstmt.setInt(4, selectedOrganiser.getOrganiserId());
            pstmt.setInt(5, maxParticipants);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Event added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                eventNameField.setText("");
                dateField.setText("");
                descriptionField.setText("");
                maxParticipantsField.setText("100");
                dispose();
            }
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
}
