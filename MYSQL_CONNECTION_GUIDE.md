# MySQL Connection Configuration Guide

## Current DBConnection.java Settings

The project uses the following MySQL connection settings:

```
Host: localhost
Port: 3306
Database: eventdb
Username: root
Password: (empty/blank)
```

---

## If Your Teammate Needs Different Settings

Edit: `src/db/DBConnection.java`

### Find this line (around line 15):
```java
private static final String URL = "jdbc:mysql://localhost:3306/eventdb";
private static final String USER = "root";
private static final String PASSWORD = "";
```

### Change to their settings:
```java
// Example 1: If they have a password
private static final String PASSWORD = "their_password";

// Example 2: Different host
private static final String URL = "jdbc:mysql://192.168.1.100:3306/eventdb";

// Example 3: Different username
private static final String USER = "admin";
```

---

## Common Configuration Scenarios

### Scenario 1: XAMPP with Empty Password (DEFAULT)
```java
private static final String URL = "jdbc:mysql://localhost:3306/eventdb";
private static final String USER = "root";
private static final String PASSWORD = "";
```
✅ **This is the current setup - No changes needed**

---

### Scenario 2: XAMPP with Custom Password
If they set a password during XAMPP installation:

```java
private static final String PASSWORD = "their_xampp_password";
```

Then recompile:
```powershell
cd EVENT-MANAGER
.\compile.bat
```

---

### Scenario 3: Remote MySQL Server
If MySQL is on a different machine:

```java
private static final String URL = "jdbc:mysql://192.168.1.50:3306/eventdb";
private static final String USER = "root";
private static final String PASSWORD = "remote_password";
```

---

### Scenario 4: Different Port
If MySQL is on a non-standard port (e.g., 3307):

```java
private static final String URL = "jdbc:mysql://localhost:3307/eventdb";
```

---

## Testing the Connection

Run this command to test if connection works:

```powershell
cd EVENT-MANAGER\src
java -cp ".;C:\lib\mysql-connector-j-8.0.33.jar" Main
```

If it shows: **"Database connection successful!"**
✅ Connection is working!

If it shows: **Connection error**
❌ Check your DBConnection.java settings

---

## How to Find MySQL Password

### On Same Machine (XAMPP):
1. Open XAMPP Control Panel
2. Click MySQL "Config" button
3. Look for: `password =` in my.ini file

### On Remote Server:
Ask your database administrator for:
- MySQL host/IP address
- Port number
- Username
- Password

---

## Port Troubleshooting

### Check if MySQL is using port 3306:
```powershell
netstat -ano | findstr :3306
```

### If port is in use by something else:
Either:
1. Stop the other service using that port
2. Or configure MySQL to use a different port
3. And update DBConnection.java accordingly

---

## Database Name

**Important**: The database MUST be named: **eventdb**

This is created by running:
```powershell
$mysqlPath = "C:\xampp\mysql\bin\mysql.exe"
Get-Content sql/eventdb_schema.sql | & $mysqlPath -u root
```

If your teammate needs a different database name:
1. Edit `sql/eventdb_schema.sql` - change line 8
2. Edit `src/db/DBConnection.java` - change the URL
3. Re-run the SQL script
4. Recompile

---

## JDBC Driver Version

**Current**: mysql-connector-j-8.0.33

If they need to use a different version:
1. Download from: https://dev.mysql.com/downloads/connector/j/
2. Copy to: `C:\lib\`
3. Update the path in `compile.bat` if filename differs

---

## Connection Pool Settings

Current connection pool in `DBConnection.java`:

```java
private static final int MAX_CONNECTIONS = 10;
private static final int CONNECTION_TIMEOUT = 5000; // 5 seconds
```

These can be adjusted in DBConnection.java if needed for high-load scenarios.

---

## Verifying Connection Works

**Test Step 1**: Check MySQL is running
```powershell
netstat -ano | findstr :3306
```

**Test Step 2**: Try connecting via MySQL CLI
```powershell
C:\xampp\mysql\bin\mysql.exe -u root -p
```
(Press Enter if no password)

**Test Step 3**: Check database exists
```powershell
mysql> USE eventdb;
mysql> SHOW TABLES;
```

**Test Step 4**: Run the application
```powershell
cd EVENT-MANAGER\src
java -cp ".;C:\lib\mysql-connector-j-8.0.33.jar" Main
```

If all 4 steps work, connection is configured correctly! ✅

---

## For Quick Debugging

Add this to see connection details (temporary):

In `src/db/DBConnection.java`, after line 30:
```java
System.out.println("Connecting to: " + URL);
System.out.println("User: " + USER);
System.out.println("Password: [" + (PASSWORD.isEmpty() ? "EMPTY" : "SET") + "]");
```

Then recompile and run to see what settings are being used.

---

**Last Updated**: November 28, 2025
