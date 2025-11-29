import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EventManagementPortal extends JFrame {
    private Connection conn;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> eventCombo;

    public EventManagementPortal() {
        setTitle("Event Management Portal");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/event_management?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "thrisha" // replace with your MySQL password
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            System.exit(1);
        }

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Register Participant", createRegisterPanel());
        tabs.add("View Events", createViewPanel("events"));
        tabs.add("View Participants", createViewPanel("participant"));
        tabs.add("View Registrations", createViewPanel("registrations"));

        add(tabs);
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 30, 100, 25);
        panel.add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(160, 30, 200, 25);
        panel.add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 70, 100, 25);
        panel.add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(160, 70, 200, 25);
        panel.add(emailField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 110, 100, 25);
        panel.add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBounds(160, 110, 200, 25);
        panel.add(phoneField);

        JLabel eventLabel = new JLabel("Select Event:");
        eventLabel.setBounds(50, 150, 100, 25);
        panel.add(eventLabel);
        eventCombo = new JComboBox<>();
        eventCombo.setBounds(160, 150, 200, 25);
        panel.add(eventCombo);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(120, 200, 120, 30);
        panel.add(registerButton);

        JButton refreshButton = new JButton("Refresh Events");
        refreshButton.setBounds(260, 200, 150, 30);
        panel.add(refreshButton);

        loadEvents();

        refreshButton.addActionListener(e -> loadEvents());

        registerButton.addActionListener(e -> selfRegisterParticipant());

        return panel;
    }

    private void loadEvents() {
        eventCombo.removeAllItems();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Eid, Ename FROM events");
            while (rs.next()) {
                eventCombo.addItem(rs.getString("Eid") + " - " + rs.getString("Ename"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + e.getMessage());
        }
    }

    private void selfRegisterParticipant() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || eventCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select an event");
            return;
        }

        String eid = eventCombo.getSelectedItem().toString().split(" - ")[0];
        String pid = null;

        try {
            // Check if participant already exists by email
            PreparedStatement psCheck = conn.prepareStatement("SELECT Pid FROM participant WHERE Email=?");
            psCheck.setString(1, email);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                pid = rs.getString("Pid"); // Existing participant
            } else {
                // Create new participant with alphanumeric ID
                Statement stmt = conn.createStatement();
                ResultSet rsMax = stmt.executeQuery("SELECT MAX(Pid) AS MaxPid FROM participant");
                int newIdNum = 101; // default if no participants
                if (rsMax.next() && rsMax.getString("MaxPid") != null) {
                    String maxPid = rsMax.getString("MaxPid"); // e.g., P103
                    newIdNum = Integer.parseInt(maxPid.replaceAll("[^0-9]", "")) + 1;
                }
                pid = "P" + newIdNum;

                PreparedStatement psInsert = conn.prepareStatement(
                        "INSERT INTO participant(Pid, Pname, Phone, Email, Did) VALUES (?, ?, ?, ?, 1)"
                );
                psInsert.setString(1, pid);
                psInsert.setString(2, name);
                psInsert.setString(3, phone);
                psInsert.setString(4, email);
                psInsert.executeUpdate();
            }

            // Call stored procedure to register participant
            CallableStatement cs = conn.prepareCall("{CALL Register_Student(?, ?)}");
            cs.setString(1, pid);
            cs.setString(2, eid);
            cs.execute();

            JOptionPane.showMessageDialog(this, "Registration successful! Your ID: " + pid);
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private JPanel createViewPanel(String tableName) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        panel.add(refreshButton, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
                ResultSetMetaData rsmd = rs.getMetaData();

                model.setRowCount(0);
                model.setColumnCount(0);

                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    model.addColumn(rsmd.getColumnName(i));
                }

                while (rs.next()) {
                    Object[] row = new Object[rsmd.getColumnCount()];
                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                        row[i] = rs.getObject(i + 1);
                    }
                    model.addRow(row);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventManagementPortal().setVisible(true));
    }
}
