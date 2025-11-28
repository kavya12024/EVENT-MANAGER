package ui;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame {
    private JButton adminLoginBtn;
    private JButton studentLoginBtn;
    private JLabel titleLabel;

    public Login() {
        setTitle("College Event Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        titleLabel = new JLabel("College Event Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));
        buttonPanel.setBackground(new Color(240, 240, 240));

        adminLoginBtn = new JButton("Admin Login");
        adminLoginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        adminLoginBtn.setBackground(new Color(52, 152, 219));
        adminLoginBtn.setForeground(Color.WHITE);
        adminLoginBtn.setFocusPainted(false);
        adminLoginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAdminDashboard();
            }
        });

        studentLoginBtn = new JButton("Student Login");
        studentLoginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        studentLoginBtn.setBackground(new Color(46, 204, 113));
        studentLoginBtn.setForeground(Color.WHITE);
        studentLoginBtn.setFocusPainted(false);
        studentLoginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        studentLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStudentLoginDialog();
            }
        });

        JButton studentRegisterBtn = new JButton("New Student Registration");
        studentRegisterBtn.setFont(new Font("Arial", Font.BOLD, 14));
        studentRegisterBtn.setBackground(new Color(230, 126, 34));
        studentRegisterBtn.setForeground(Color.WHITE);
        studentRegisterBtn.setFocusPainted(false);
        studentRegisterBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        studentRegisterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStudentRegistrationDialog();
            }
        });

        buttonPanel.add(adminLoginBtn);
        buttonPanel.add(studentLoginBtn);
        buttonPanel.add(studentRegisterBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void openAdminDashboard() {
        this.dispose();
        new AdminDashboard();
    }

    private void showStudentLoginDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JComboBox<String> deptCombo = new JComboBox<>();
        loadDepartmentsForLogin(deptCombo);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JTextField emailField = new JTextField(10);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPasswordField passwordField = new JPasswordField(10);

        panel.add(deptLabel);
        panel.add(deptCombo);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Student Login", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String selectedDept = (String) deptCombo.getSelectedItem();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (selectedDept.equals("Select Department")) {
                    JOptionPane.showMessageDialog(this, "Please select a department!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Email and password are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get department ID based on selected combo value
                int deptId = getDepartmentIdFromCombo(selectedDept);
                if (deptId <= 0) {
                    JOptionPane.showMessageDialog(this, "Invalid department!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validate credentials and get student ID
                int retrievedStudentId = validateAndGetStudentId(email, password, deptId);
                if (retrievedStudentId > 0) {
                    this.dispose();
                    new StudentDashboard(retrievedStudentId);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid email, password, or department!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Login error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadDepartmentsForLogin(JComboBox<String> deptCombo) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT dept_id, dept_name FROM department ORDER BY dept_id";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            deptCombo.addItem("Select Department");
            
            while (rs.next()) {
                int deptId = rs.getInt("dept_id");
                String deptName = rs.getString("dept_name");
                deptCombo.addItem(deptId + " - " + deptName);
            }
            
            rs.close();
            pstmt.close();
            DBConnection.closeConnection(conn);
        } catch (Exception e) {
            deptCombo.addItem("Error loading departments");
        }
    }

    private int getDepartmentIdFromCombo(String selectedDept) {
        if (selectedDept.equals("Select Department")) {
            return -1;
        }
        try {
            // Extract ID from "1 - Computer Science"
            String[] parts = selectedDept.split(" - ");
            return Integer.parseInt(parts[0]);
        } catch (Exception e) {
            return -1;
        }
    }

    private int validateAndGetStudentId(String email, String password, int deptId) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT student_id FROM student WHERE email = ? AND password = ? AND department_id = ? AND is_active = 1";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setInt(3, deptId);
            
            ResultSet rs = pstmt.executeQuery();
            int studentId = -1;
            
            if (rs.next()) {
                studentId = rs.getInt("student_id");
            }
            
            DBConnection.closeResultSet(rs);
            DBConnection.closeStatement(pstmt);
            DBConnection.closeConnection(conn);
            
            return studentId;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    private void showStudentRegistrationDialog() {
        JDialog registrationFrame = new JDialog(this, "Student Registration", true);
        registrationFrame.setSize(700, 600);
        registrationFrame.setLocationRelativeTo(this);
        registrationFrame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(230, 126, 34));
        JLabel headerLabel = new JLabel("New Student Registration");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Form Panel - Using GridBagLayout for proper alignment
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField(15);

        JLabel enrollLabel = new JLabel("Enrollment No:");
        JTextField enrollField = new JTextField(15);

        JLabel deptLabel = new JLabel("Department:");
        String[] departments = {"1 - Computer Science", "2 - Mechanical Engineering", 
                               "3 - Electrical Engineering", "4 - Civil Engineering", "5 - Electronics"};
        JComboBox<String> deptCombo = new JComboBox<>(departments);
        deptCombo.setSelectedIndex(0);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(15);

        JLabel yearLabel = new JLabel("Year (1-4):");
        JComboBox<Integer> yearCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        yearCombo.setSelectedIndex(0);

        JLabel semLabel = new JLabel("Semester (1-8):");
        JComboBox<Integer> semCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        semCombo.setSelectedIndex(0);

        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);

        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPassField = new JPasswordField(15);

        // Row 1: Full Name & Enrollment No
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(nameLabel, gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);
        gbc.gridx = 2; formPanel.add(enrollLabel, gbc);
        gbc.gridx = 3; formPanel.add(enrollField, gbc);

        // Row 2: Department & Email
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(deptLabel, gbc);
        gbc.gridx = 1; formPanel.add(deptCombo, gbc);
        gbc.gridx = 2; formPanel.add(emailLabel, gbc);
        gbc.gridx = 3; formPanel.add(emailField, gbc);

        // Row 3: Year & Semester
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(yearLabel, gbc);
        gbc.gridx = 1; formPanel.add(yearCombo, gbc);
        gbc.gridx = 2; formPanel.add(semLabel, gbc);
        gbc.gridx = 3; formPanel.add(semCombo, gbc);

        // Row 4: Phone
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(phoneLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; formPanel.add(phoneField, gbc);
        gbc.gridwidth = 1;

        // Row 5: Password
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; formPanel.add(passwordField, gbc);
        gbc.gridwidth = 1;

        // Row 6: Confirm Password
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; formPanel.add(confirmPassLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; formPanel.add(confirmPassField, gbc);
        gbc.gridwidth = 1;

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(46, 204, 113));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Arial", Font.BOLD, 12));
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String deptStr = (String) deptCombo.getSelectedItem();
                String dept = deptStr.substring(0, 1); // Extract just the number
                int year = (Integer) yearCombo.getSelectedItem();
                int semester = (Integer) semCombo.getSelectedItem();
                registerNewStudent(nameField.getText(), enrollField.getText(), dept,
                        emailField.getText(), String.valueOf(year), String.valueOf(semester), phoneField.getText(),
                        new String(passwordField.getPassword()),
                        new String(confirmPassField.getPassword()), registrationFrame);
            }
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrationFrame.dispose();
            }
        });

        buttonPanel.add(registerBtn);
        buttonPanel.add(cancelBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        registrationFrame.add(mainPanel);
        registrationFrame.setVisible(true);
    }

    private void registerNewStudent(String name, String enrollment, String department, String email,
                                     String year, String semester, String phone, String password, String confirmPassword, JDialog parentDialog) {
        // Validation
        if (name.isEmpty() || enrollment.isEmpty() || department.isEmpty() || 
            email.isEmpty() || year.isEmpty() || semester.isEmpty() || phone.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(parentDialog, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Email validation
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(parentDialog, "Please enter a valid email address!\n(Must contain @ and proper format)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Phone number validation - must be 10 digits
        if (!isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(parentDialog, "Phone number must be exactly 10 digits!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(parentDialog, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(parentDialog, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int deptId = Integer.parseInt(department);
            int yearVal = Integer.parseInt(year);
            int semVal = Integer.parseInt(semester);

            if (deptId <= 0 || yearVal < 1 || yearVal > 4 || semVal < 1 || semVal > 8) {
                JOptionPane.showMessageDialog(parentDialog, "Invalid Department ID (1-4), Year (1-4), or Semester (1-8)!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert into database
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO student (email, password, full_name, enrollment_no, department_id, year, semester, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, enrollment);
            pstmt.setInt(5, deptId);
            pstmt.setInt(6, yearVal);
            pstmt.setInt(7, semVal);
            pstmt.setString(8, phone);

            int result = pstmt.executeUpdate();
            DBConnection.closeStatement(pstmt);
            DBConnection.closeConnection(conn);

            if (result > 0) {
                JOptionPane.showMessageDialog(parentDialog, 
                    "Registration successful!\n\nName: " + name + 
                    "\nEnrollment: " + enrollment + 
                    "\nDepartment: " + deptId + 
                    "\nYear: " + yearVal + 
                    "\nSemester: " + semVal +
                    "\nEmail: " + email + 
                    "\n\nYou can now login with your email and password.",
                    "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
                parentDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(parentDialog, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentDialog, "Please enter valid numbers for Department ID, Year, and Semester!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentDialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        // Email validation: must contain @ and a domain
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailPattern);
    }

    private boolean isValidPhoneNumber(String phone) {
        // Phone validation: must be exactly 10 digits
        String phonePattern = "^[0-9]{10}$";
        return phone.matches(phonePattern);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login();
            }
        });
    }
}
