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
        buttonPanel.setLayout(new GridLayout(2, 1, 20, 20));
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

        buttonPanel.add(adminLoginBtn);
        buttonPanel.add(studentLoginBtn);

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
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel label = new JLabel("Enter Student ID:");
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        JTextField idField = new JTextField(10);

        panel.add(label);
        panel.add(idField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Student Login", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                studentId = Integer.parseInt(idField.getText().trim());
                if (studentId > 0) {
                    this.dispose();
                    new StudentDashboard(studentId);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Student ID!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Student ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
