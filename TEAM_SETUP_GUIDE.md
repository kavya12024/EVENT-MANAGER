# Setup Guide for Team Members - EVENT-MANAGER Project

## Prerequisites Check

### 1. Verify Java is Installed
```powershell
java -version
```
Expected: Java 21 or higher

### 2. Verify MySQL/XAMPP is Running
```powershell
netstat -ano | findstr :3306
```
Expected: MySQL listening on port 3306

---

## Installation Steps for New Team Members

### Step 1: Clone the Repository
```powershell
git clone https://github.com/kavya12024/EVENT-MANAGER.git
cd EVENT-MANAGER
```

### Step 2: Install XAMPP (if not already installed)

1. Download from: https://www.apachefriends.org/
2. Choose **Latest Version** (with MySQL, Apache, PHP)
3. Install to: `C:\xampp\`
4. Complete the installation

### Step 3: Start MySQL

**Option A: Using XAMPP Control Panel (Recommended)**
- Open: `C:\xampp\xampp-control.exe`
- Click **Start** next to MySQL
- Should show: "Running" (green indicator)

**Option B: Using Command Line**
```powershell
cd C:\xampp\mysql\bin
.\mysqld --console
```

### Step 4: Verify MySQL is Running
```powershell
netstat -ano | findstr :3306
```
Should show: `LISTENING` on port 3306

---

## If MySQL Won't Start

### Solution 1: Clean MySQL Database (Fastest)
```powershell
# Stop XAMPP first

# Navigate to XAMPP folder
cd C:\xampp\data

# Delete these folders
Remove-Item -Recurse -Force mysql
Remove-Item -Recurse -Force phpmyadmin

# Restart XAMPP - MySQL will recreate these folders
```

### Solution 2: Check MySQL Logs
```powershell
# Open MySQL error log
notepad C:\xampp\mysql\data\mysql_error.log
```

### Solution 3: Reinstall XAMPP (Last Resort)
```powershell
# Uninstall XAMPP from Control Panel
# Delete the folder: C:\xampp

# Download and reinstall from: https://www.apachefriends.org/
```

---

## Step 5: Download JDBC Driver

1. Go to: https://dev.mysql.com/downloads/connector/j/
2. Click **Archives** tab
3. Find: **Connector/J 8.0.33**
4. Download: `mysql-connector-j-8.0.33.zip`
5. Extract the ZIP
6. Copy `mysql-connector-j-8.0.33.jar` to: **`C:\lib\`**

**Create folder if missing:**
```powershell
New-Item -ItemType Directory -Path "C:\lib\" -Force
```

---

## Step 6: Create Database

```powershell
cd EVENT-MANAGER
$mysqlPath = "C:\xampp\mysql\bin\mysql.exe"
Get-Content sql/eventdb_schema.sql | & $mysqlPath -u root
```

**Verify database created:**
```powershell
& $mysqlPath -u root -e "USE eventdb; SHOW TABLES;"
```

Should show 6 tables:
- department
- student
- organiser
- participant
- event
- participation

---

## Step 7: Compile Java Project

```powershell
cd EVENT-MANAGER
.\compile.bat
```

Expected output: "Compilation completed successfully!"

---

## Step 8: Run the Application

```powershell
cd EVENT-MANAGER\src
java -cp ".;C:\lib\mysql-connector-j-8.0.33.jar" Main
```

**Login Screen should appear!**

---

## Test Login Credentials

### Admin Login:
- **Email**: `admin@college.edu`
- **Password**: `admin123`

### Student Login (Pre-loaded):
- **Email**: `student1@college.edu`
- **Password**: `student123`

### New Student Registration:
- Click: **New Student Registration**
- Fill all fields including Year (1-4) and Semester (1-8)
- Department ID must be 1-4

---

## Troubleshooting

### Error: "Cannot connect to MySQL"
```
Solution:
1. Verify MySQL is running: netstat -ano | findstr :3306
2. If not running, start from XAMPP Control Panel
3. If still fails, try Solution 1 (Clean MySQL Database)
```

### Error: "MySQL shutdown unexpectedly"
```
Solution:
1. Open XAMPP Control Panel
2. Click "Logs" button
3. Check mysql_error.log for issues
4. Try: Delete C:\xampp\mysql\data\mysql folder
5. Restart XAMPP
```

### Error: "JDBC driver not found"
```
Solution:
1. Verify C:\lib\ folder exists
2. Verify mysql-connector-j-8.0.33.jar is in C:\lib\
3. Re-download from: https://dev.mysql.com/downloads/connector/j/
```

### Error: "Class not found during compilation"
```
Solution:
1. Delete all .class files: cd src && del /S *.class
2. Re-run: .\compile.bat
```

### Error: "Port 3306 already in use"
```
Solution:
1. Find what's using port 3306:
   netstat -ano | findstr :3306
   
2. Kill the process (if it's stuck MySQL):
   taskkill /PID <process_id> /F
   
3. Restart XAMPP
```

---

## Project Structure

```
EVENT-MANAGER/
├── src/
│   ├── db/
│   │   └── DBConnection.java (Database connection)
│   ├── models/ (6 POJO classes)
│   ├── ui/ (12 GUI screens)
│   └── Main.java (Entry point)
├── sql/
│   └── eventdb_schema.sql (Database schema)
├── compile.bat (Compile script)
├── run.bat (Run script)
└── Documentation files
```

---

## Quick Start Checklist

- [ ] Java 21+ installed
- [ ] XAMPP installed and MySQL running
- [ ] JDBC driver in `C:\lib\`
- [ ] Database created (eventdb)
- [ ] Project compiled (`compile.bat` successful)
- [ ] Application running with Login screen visible
- [ ] Can login with demo credentials

---

## Additional Resources

- **GitHub Repository**: https://github.com/kavya12024/EVENT-MANAGER
- **MySQL Documentation**: https://dev.mysql.com/doc/
- **Java JDBC Guide**: https://docs.oracle.com/javase/tutorial/jdbc/
- **XAMPP Download**: https://www.apachefriends.org/

---

## Contact for Issues

If your teammate encounters any issues not listed above:

1. Check the MySQL error log: `C:\xampp\mysql\data\mysql_error.log`
2. Share the exact error message in the GitHub issues
3. Verify all steps in this guide were completed

---

**Last Updated**: November 28, 2025
**Version**: 1.0
