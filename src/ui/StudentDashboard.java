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

public class StudentDashboard extends JFrame {
    private int studentId;
    private String studentName;
    private JButton registerEventBtn;
    private JButton viewEventsBtn;
    private JButton viewScoresBtn;
    private JButton logoutBtn;
    private JLabel welcomeLabel;

    public StudentDashboard(int studentId) {
        this.studentId = studentId;
        this.studentName = getStudentName();

        setTitle("College Event Management System - Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Student Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        welcomeLabel = new JLabel("Welcome, " + studentName + "!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLabel.setForeground(Color.WHITE);

        JPanel headerContent = new JPanel();
        headerContent.setBackground(new Color(46, 204, 113));
        headerContent.setLayout(new GridLayout(2, 1));
        headerContent.add(titleLabel);
        headerContent.add(welcomeLabel);

        headerPanel.add(headerContent, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        buttonPanel.setBackground(new Color(240, 240, 240));

        registerEventBtn = createButton("Register for Event");
        registerEventBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterEvent(studentId);
            }
        });

        viewEventsBtn = createButton("View My Events");
        viewEventsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewMyEvents(studentId);
            }
        });

        viewScoresBtn = createButton("View My Scores");
        viewScoresBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewScores(studentId);
            }
        });

        logoutBtn = createButton("Logout");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login();
            }
        });

        buttonPanel.add(registerEventBtn);
        buttonPanel.add(viewEventsBtn);
        buttonPanel.add(viewScoresBtn);
        buttonPanel.add(logoutBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private String getStudentName() {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT participant_name FROM participant WHERE participant_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("participant_name");
                rs.close();
                pstmt.close();
                return name;
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Error fetching student name: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }

        return "Student";
    }
}
