package ui;

import db.DBConnection;
import models.Department;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddOrganiser extends JFrame {
    private JTextField organiserNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<Department> deptCombo;
    private JButton addBtn;
    private JButton cancelBtn;

    public AddOrganiser() {
        setTitle("Add Organiser");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Add Organiser");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 240, 240));

        JLabel organiserLabel = new JLabel("Organiser Name:");
        organiserLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        organiserNameField = new JTextField();

        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        deptCombo = new JComboBox<>();
        loadDepartments();

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailField = new JTextField();

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        phoneField = new JTextField();

        formPanel.add(organiserLabel);
        formPanel.add(organiserNameField);
        formPanel.add(deptLabel);
        formPanel.add(deptCombo);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);

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
                addOrganiser();
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

        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void loadDepartments() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM department ORDER BY dept_name";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                deptCombo.addItem(new Department(rs.getInt("dept_id"), rs.getString("dept_name")));
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading departments: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    private void addOrganiser() {
        String organiserName = organiserNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        Department selectedDept = (Department) deptCombo.getSelectedItem();

        if (organiserName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter organiser name!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedDept == null) {
            JOptionPane.showMessageDialog(this, "Please select a department!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO organiser (organiser_name, dept_id, email, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, organiserName);
            pstmt.setInt(2, selectedDept.getDeptId());
            pstmt.setString(3, email.isEmpty() ? null : email);
            pstmt.setString(4, phone.isEmpty() ? null : phone);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Organiser added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                organiserNameField.setText("");
                emailField.setText("");
                phoneField.setText("");
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
