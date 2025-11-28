# College Event Management System - DBMS Project

## 📌 Overview
A complete Java Swing application for managing college events with MySQL database backend. This project demonstrates:
- Java Swing GUI development
- JDBC database connectivity
- SQL triggers and stored procedures
- CRUD operations
- Multi-user authentication (Admin & Student)

---

## 🛠️ Technology Stack
- **Frontend:** Java Swing
- **Backend Database:** MySQL (XAMPP phpMyAdmin)
- **Connectivity:** JDBC (mysql-connector-j-8.x.x.jar)
- **Build Tool:** None (Pure Java + JDBC)

---

## 📋 Prerequisites
1. **Java Development Kit (JDK)** - JDK 8 or higher
2. **XAMPP** - For MySQL server
3. **MySQL JDBC Driver** - mysql-connector-j-8.x.x.jar

---

## 🚀 Setup Instructions

### Step 1: Install XAMPP and Start MySQL
1. Download and install XAMPP from https://www.apachefriends.org/
2. Start XAMPP Control Panel
3. Click "Start" for Apache and MySQL modules

### Step 2: Create Database
1. Open phpMyAdmin: http://localhost/phpmyadmin/
2. Run the SQL script to create database and tables:
   - Go to: `SQL` tab
   - Copy and paste content from `/sql/eventdb_schema.sql`
   - Click "Go"

**Alternative (Command Line):**
```bash
mysql -u root -p < sql/eventdb_schema.sql
```

### Step 3: Download MySQL JDBC Driver
1. Download `mysql-connector-j-8.x.x.jar` from:
   https://dev.mysql.com/downloads/connector/j/
2. Place it in a accessible location (e.g., `lib/` folder)

### Step 4: Compile Java Files
```bash
cd c:\projects\dbms project\src

# Compile with JDBC driver in classpath
javac -cp "path/to/mysql-connector-j-8.x.x.jar" db/DBConnection.java
javac -cp "path/to/mysql-connector-j-x.x.x.jar" models/*.java
javac -cp "path/to/mysql-connector-j-8.x.x.jar" ui/*.java
javac -cp "path/to/mysql-connector-j-8.x.x.jar" Main.java
```

**Windows Example:**
```bash
javac -cp "C:\lib\mysql-connector-j-8.0.33.jar" db/DBConnection.java
javac -cp "C:\lib\mysql-connector-j-8.0.33.jar" models/*.java
javac -cp "C:\lib\mysql-connector-j-8.0.33.jar" ui/*.java
javac -cp "C:\lib\mysql-connector-j-8.0.33.jar" Main.java
```

### Step 5: Run Application
```bash
cd c:\projects\dbms project\src

java -cp ".;path/to/mysql-connector-j-8.x.x.jar" Main
```

**Windows Example:**
```bash
java -cp ".;C:\lib\mysql-connector-j-8.0.33.jar" Main
```

---

## 📚 Database Schema

### Tables Created:
1. **department** - Store department information
2. **organiser** - Event organizers with department reference
3. **participant** - Students participating in events
4. **event** - Event details with organizer reference
5. **participation** - Join table for student event registration

### Trigger:
- **before_participation_insert** - Automatically sets score to 0 if NULL

### Stored Procedure:
- **GetEventParticipants(event_id)** - Retrieves all participants and scores for an event

---

## 🎮 Application Features

### 🔐 Login Screen
- Admin Login
- Student Login (ID-based)

### 👨‍💼 Admin Dashboard
- **Add Department** - Create new departments
- **Add Organiser** - Register event organizers
- **Add Event** - Create new events
- **Update Score** - Modify participant scores
- **View All Participants** - List all students
- **View Event-wise Participants** - Uses stored procedure to fetch participants by event

### 👨‍🎓 Student Dashboard
- **Register for Event** - Enroll in available events
- **View My Events** - See all registered events
- **View My Scores** - Check scores for all events

---

## 📁 Project Structure
```
dbms project/
│
├── src/
│   ├── db/
│   │   └── DBConnection.java         (Database connection utility)
│   │
│   ├── models/
│   │   ├── Department.java
│   │   ├── Organiser.java
│   │   ├── Participant.java
│   │   ├── Event.java
│   │   └── Participation.java
│   │
│   ├── ui/
│   │   ├── Login.java                (Login screen)
│   │   ├── AdminDashboard.java       (Admin main screen)
│   │   ├── StudentDashboard.java     (Student main screen)
│   │   ├── AddDepartment.java        (Add department form)
│   │   ├── AddOrganiser.java         (Add organiser form)
│   │   ├── AddEvent.java             (Add event form)
│   │   ├── ScoreUpdate.java          (Update score screen)
│   │   ├── ViewParticipants.java     (View all participants)
│   │   ├── ViewEventParticipants.java (View event participants - uses SP)
│   │   ├── RegisterEvent.java        (Register for event)
│   │   ├── ViewMyEvents.java         (View student's events)
│   │   └── ViewScores.java           (View student's scores)
│   │
│   └── Main.java                     (Entry point)
│
├── sql/
│   └── eventdb_schema.sql            (Database schema, trigger, SP, sample data)
│
└── README.md                         (This file)
```

---

## 🔑 Login Credentials

### Admin Access
- Click "Admin Login" button (No password required for this demo)

### Student Access
- Enter any valid Student ID from 1-10 (sample data includes IDs 1-10)
- Example IDs: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10

---

## 🗄️ Sample Data
The database includes:
- **5 Departments:** CS, ME, EE, CE, Electronics
- **4 Organisers:** Assigned to different departments
- **10 Participants:** Students from various departments
- **5 Events:** Various college events
- **10 Participations:** Student registrations with scores

---

## 🐛 Troubleshooting

### "Cannot find symbol: class DBConnection"
- Ensure all Java files are in correct folders under `/src`
- Recompile from the correct directory

### "MySQL JDBC Driver not found"
- Download mysql-connector-j-8.x.x.jar
- Add to classpath during compilation and execution
- Verify jar file path is correct

### "Connection refused" / "Access denied"
- Ensure MySQL is running (XAMPP active)
- Check database 'eventdb' exists
- Verify credentials in DBConnection.java (default: root, password="")

### "No database selected"
- Run the SQL script from eventdb_schema.sql
- Ensure all tables are created

---

## 📝 Code Examples

### Database Connection
```java
Connection conn = DBConnection.getConnection();
```

### Insert Data
```java
String sql = "INSERT INTO department (dept_name) VALUES (?)";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, "Computer Science");
pstmt.executeUpdate();
```

### Stored Procedure Call
```java
String sql = "CALL GetEventParticipants(?)";
CallableStatement cstmt = conn.prepareCall(sql);
cstmt.setInt(1, eventId);
ResultSet rs = cstmt.executeQuery();
```

---

## 🔒 Security Notes
- This is a demo project for educational purposes
- Passwords are not implemented for admin login
- In production, implement proper authentication & authorization
- Use encrypted passwords
- Implement input validation and SQL injection prevention

---

## 📄 License
Educational Project - Free to use and modify

---

## 👨‍💻 Author
Created as a complete DBMS Mini-Project demonstrating:
- Database design with relationships
- Java Swing GUI development
- JDBC connectivity
- SQL triggers and stored procedures
- CRUD operations

---

## 📞 Support
For issues or questions:
1. Check database connection
2. Verify JDBC driver is in classpath
3. Ensure all files are compiled
4. Review console output for error messages

---

**Happy Coding! 🎉**
