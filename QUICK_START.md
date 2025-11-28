# College Event Management System - Quick Setup Guide

## ⚡ Quick Start (5 Steps)

### Step 1: Download & Install XAMPP
1. Go to https://www.apachefriends.org/
2. Download XAMPP for Windows
3. Install and start MySQL from XAMPP Control Panel

### Step 2: Create Database
1. Open http://localhost/phpmyadmin/ in browser
2. Click "SQL" tab at top
3. Open file: `sql/eventdb_schema.sql`
4. Copy all content and paste into SQL tab
5. Click "Go" button
6. Database created with all tables, trigger, stored procedure!

### Step 3: Download MySQL JDBC Driver
1. Go to https://dev.mysql.com/downloads/connector/j/
2. Download mysql-connector-j-8.0.33.jar (or latest 8.x version)
3. Save to your system (e.g., `C:\lib\`)
4. Remember this path!

### Step 4: Update Configuration
1. Open `compile.bat` in text editor
2. Change this line:
   ```
   set JDBC_PATH=C:\lib\mysql-connector-j-8.0.33.jar
   ```
   Replace path with your actual JDBC location
3. Do same for `run.bat`

### Step 5: Compile & Run
1. Double-click `compile.bat` - Compiles all Java files
2. Double-click `run.bat` - Starts the application!

---

## 🔑 Login Information

### Admin Access
- Click "Admin Login" button
- No password required (demo mode)

### Student Access
- Click "Student Login" button
- Enter any ID from: **1, 2, 3, 4, 5, 6, 7, 8, 9, 10**

---

## ✨ Features Walkthrough

### As Admin:
1. Add departments
2. Add event organizers
3. Create events
4. Update student scores
5. View all participants
6. View participants per event (using stored procedure)

### As Student:
1. Register for events
2. View your enrolled events
3. Check your scores

---

## 📊 Sample Data Included

### Departments
- Computer Science
- Mechanical Engineering
- Electrical Engineering
- Civil Engineering
- Electronics

### Students (10 total)
- IDs 1-10 with names and emails

### Events (5 total)
- Code Marathon 2025
- Tech Talk - AI & ML
- Robotics Workshop
- Debate Competition
- Quiz Master

### Scores
- Pre-populated with sample scores

---

## ❌ Troubleshooting

| Problem | Solution |
|---------|----------|
| JDBC Driver not found | Download from mysql.com and update paths in batch files |
| Connection refused | Start MySQL in XAMPP Control Panel |
| No database selected | Run eventdb_schema.sql in phpMyAdmin |
| Compilation errors | Check JDBC path is correct and file exists |

---

## 🎯 Advanced Users (Manual Compilation)

If batch files don't work, use these commands:

```bash
cd c:\projects\dbms project\src

rem Compile database connection
javac -cp "C:\lib\mysql-connector-j-8.0.33.jar" db/DBConnection.java

rem Compile models
javac -cp "C:\lib\mysql-connector-j-8.0.33.jar" models/*.java

rem Compile UI
javac -cp "C:\lib\mysql-connector-j-8.0.33.jar" ui/*.java

rem Compile main
javac -cp "C:\lib\mysql-connector-j-8.0.33.jar" Main.java

rem Run application
java -cp ".;C:\lib\mysql-connector-j-8.0.33.jar" Main
```

---

## 📚 Project Includes

✅ 11 Java files for UI  
✅ 5 Model classes (POJO)  
✅ Database connection utility  
✅ Complete SQL schema with trigger & stored procedure  
✅ Sample data for testing  
✅ Batch scripts for easy compilation & execution  
✅ Comprehensive README documentation  

---

## 🎓 Learning Outcomes

This project demonstrates:
- ✅ Java Swing GUI development
- ✅ JDBC database connectivity
- ✅ SQL triggers in MySQL
- ✅ Stored procedures in MySQL
- ✅ Prepared statements (SQL injection prevention)
- ✅ MVC-like architecture
- ✅ Event handling in Swing
- ✅ Database design with relationships

---

## 💡 Tips

1. **Keep XAMPP running** while using the application
2. **Check phpMyAdmin** to verify database is created
3. **Read console output** for connection errors
4. **Start fresh:** Delete eventdb and re-run SQL script if needed
5. **Port issue?** MySQL typically runs on port 3306

---

**Ready? Let's go! Run compile.bat then run.bat** 🚀
