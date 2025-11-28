package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {
    private JButton addDeptBtn;
    private JButton addOrgBtn;
    private JButton addEventBtn;
    private JButton updateScoreBtn;
    private JButton viewParticipantsBtn;
    private JButton viewEventParticipantsBtn;
    private JButton logoutBtn;

    public AdminDashboard() {
        setTitle("College Event Management System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(new Color(240, 240, 240));

        addDeptBtn = createButton("Add Department");
        addDeptBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddDepartment();
            }
        });

        addOrgBtn = createButton("Add Organiser");
        addOrgBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddOrganiser();
            }
        });

        addEventBtn = createButton("Add Event");
        addEventBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddEvent();
            }
        });

        updateScoreBtn = createButton("Update Score");
        updateScoreBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ScoreUpdate();
            }
        });

        viewParticipantsBtn = createButton("View All Participants");
        viewParticipantsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewParticipants();
            }
        });

        viewEventParticipantsBtn = createButton("View Event-wise Participants");
        viewEventParticipantsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewEventParticipants();
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

        buttonPanel.add(addDeptBtn);
        buttonPanel.add(addOrgBtn);
        buttonPanel.add(addEventBtn);
        buttonPanel.add(updateScoreBtn);
        buttonPanel.add(viewParticipantsBtn);
        buttonPanel.add(viewEventParticipantsBtn);
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
}
