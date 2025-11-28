package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JButton adminLoginBtn;
    private JButton studentLoginBtn;
    private JLabel titleLabel;
    private int studentId = -1;

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

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JTextField idField = new JTextField(10);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JTextField emailField = new JTextField(10);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JPasswordField passwordField = new JPasswordField(10);

        panel.add(idLabel);
        panel.add(idField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Student Login", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String idText = idField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (idText.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                studentId = Integer.parseInt(idText);
                if (studentId > 0) {
                    // Validate credentials (you can add database validation here)
                    if (validateStudentCredentials(studentId, email, password)) {
                        this.dispose();
                        new StudentDashboard(studentId);
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Student ID!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Student ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateStudentCredentials(int studentId, String email, String password) {
        // Simple validation - you can enhance this with database queries
        // For now, accepting student1@college.edu / student123 as demo credential
        return email.equals("student1@college.edu") && password.equals("student123");
    }

    private void showStudentRegistrationDialog() {
        JDialog registrationFrame = new JDialog(this, "Student Registration", true);
        registrationFrame.setSize(500, 450);
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

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 240, 240));

        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField();

        JLabel enrollLabel = new JLabel("Enrollment No:");
        JTextField enrollField = new JTextField();

        JLabel deptLabel = new JLabel("Department:");
        JTextField deptField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPassField = new JPasswordField();

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(enrollLabel);
        formPanel.add(enrollField);
        formPanel.add(deptLabel);
        formPanel.add(deptField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(confirmPassLabel);
        formPanel.add(confirmPassField);

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
                registerNewStudent(nameField.getText(), enrollField.getText(), deptField.getText(),
                        emailField.getText(), new String(passwordField.getPassword()),
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
                                     String password, String confirmPassword, JDialog parentDialog) {
        // Validation
        if (name.isEmpty() || enrollment.isEmpty() || department.isEmpty() || 
            email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(parentDialog, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(parentDialog, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(parentDialog, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(parentDialog, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Here you would add database insertion code
        // For now, showing success message
        JOptionPane.showMessageDialog(parentDialog, 
            "Registration successful!\n\nName: " + name + 
            "\nEnrollment: " + enrollment + 
            "\nDepartment: " + department + 
            "\nEmail: " + email + 
            "\n\nYou can now login with your email and password.",
            "Registration Successful", JOptionPane.INFORMATION_MESSAGE);

        parentDialog.dispose();
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
