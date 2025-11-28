import ui.Login;
import db.DBConnection;

public class Main {
    public static void main(String[] args) {
        // Test database connection before starting the application
        if (DBConnection.testConnection()) {
            System.out.println("Database connection successful!");
            System.out.println("Starting College Event Management System...");
            
            // Launch the Login UI
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Login();
                }
            });
        } else {
            System.err.println("Failed to connect to database!");
            System.err.println("Please ensure:");
            System.err.println("1. MySQL is running (XAMPP started)");
            System.err.println("2. Database 'eventdb' exists");
            System.err.println("3. Connection details are correct in DBConnection.java");
            
            // Show error dialog
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Database Connection Failed!\n\nPlease ensure:\n" +
                "1. MySQL is running\n" +
                "2. Database 'eventdb' exists\n" +
                "3. MySQL JDBC driver is in classpath",
                "Connection Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            
            System.exit(1);
        }
    }
}
