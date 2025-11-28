# Application Flow & Architecture

## 🏗️ SYSTEM ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────┐
│                    COLLEGE EVENT MANAGEMENT                 │
│                        SYSTEM                               │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                    Java Swing UI Layer                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────┐                                           │
│  │   Login     │                                           │
│  │  Screen     │                                           │
│  └──────┬──────┘                                           │
│         │                                                   │
│    ┌────┴──────┬────────────┐                              │
│    │           │            │                              │
│    ▼           ▼            ▼                              │
│ ┌──────────┐ ┌──────────┐ ┌──────────────┐               │
│ │  Admin   │ │ Student  │ │  Invalid ID  │               │
│ │Dashboard │ │Dashboard │ │  (Rejected)  │               │
│ └─┬────────┘ └─┬────────┘ └──────────────┘               │
│   │            │                                           │
│   │            │ ┌──────────────────────────┐             │
│   │            └─┤ Student Options:         │             │
│   │              ├─ Register for Event      │             │
│   │              ├─ View My Events          │             │
│   │              └─ View My Scores          │             │
│   │              └──────────────────────────┘             │
│   │                                                        │
│   ├─────────────────┬────────────────────┐               │
│   │                 │                    │               │
│   ▼                 ▼                    ▼               │
│ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐     │
│ │Add           │ │View All      │ │View Event-   │     │
│ │Department    │ │Participants  │ │wise          │     │
│ └──────────────┘ └──────────────┘ │Participants  │     │
│                                    │(Stored Proc) │     │
│ ┌──────────────┐ ┌──────────────┐ └──────────────┘     │
│ │Add           │ │Update        │                       │
│ │Organiser     │ │Scores        │                       │
│ └──────────────┘ └──────────────┘                       │
│                                                          │
│ ┌──────────────┐                                        │
│ │Add Event     │                                        │
│ └──────────────┘                                        │
│                                                          │
└─────────────────────────────────────────────────────────┘
         │
         │ JDBC Calls
         ▼
┌─────────────────────────────────────────────────────────┐
│            Database Access Layer (JDBC)                │
├─────────────────────────────────────────────────────────┤
│  DBConnection.java - Connection Management             │
│  PreparedStatements - Secure SQL Execution             │
│  ResultSet Processing - Data Retrieval                 │
└─────────────────────────────────────────────────────────┘
         │
         │ SQL Queries
         ▼
┌─────────────────────────────────────────────────────────┐
│         MySQL Database (eventdb)                        │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  Tables:                                               │
│  • department         Foreign Keys:                    │
│  • organiser  ──┐     • organiser → department        │
│  • participant ├─────• participant → department       │
│  • event       │     • event → organiser              │
│  • participation      • participation → participant    │
│                       • participation → event          │
│                                                         │
│  Advanced Features:                                    │
│  ✓ Trigger: before_participation_insert               │
│  ✓ Procedure: GetEventParticipants()                   │
│  ✓ Cascading Delete                                    │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## 📊 DATA FLOW DIAGRAMS

### Admin Adding Event Flow
```
Admin Dashboard
    │
    └─→ Click "Add Event"
        │
        └─→ AddEvent.java Opens
            │
            ├─→ Load Organisers (Query DB)
            │
            └─→ User Fills Form
                │
                ├─ Event Name: "Tech Talk"
                ├─ Date: 2025-12-20
                ├─ Organiser: Dr. Rajesh
                └─ Max Participants: 100
                │
                └─→ Click "Add" Button
                    │
                    └─→ Validate Input
                        │
                        ├─ Check event name not empty ✓
                        ├─ Check date format (YYYY-MM-DD) ✓
                        ├─ Check organiser selected ✓
                        └─ Check max participants is number ✓
                        │
                        └─→ Execute INSERT Query
                            │
                            └─→ INSERT INTO event (...) VALUES (...)
                                │
                                └─→ Success Message
                                    │
                                    └─→ Close Dialog
```

### Student Registering for Event Flow
```
Student Dashboard (Participant ID 1)
    │
    └─→ Click "Register for Event"
        │
        └─→ RegisterEvent.java Opens
            │
            ├─→ Load Available Events
            │   │
            │   └─→ SELECT e.* FROM event e
            │       WHERE e.event_id NOT IN (
            │           SELECT event_id 
            │           FROM participation 
            │           WHERE participant_id = 1)
            │
            └─→ Display Event List in ComboBox
                │
                └─→ User Selects Event
                    │
                    └─→ Click "Register" Button
                        │
                        └─→ Execute INSERT Query
                            │
                            ├─→ INSERT INTO participation 
                            │   (participant_id, event_id, score)
                            │   VALUES (1, 2, 0)
                            │
                            └─→ TRIGGER: before_participation_insert
                                │
                                └─→ IF score IS NULL THEN score = 0
                                    │
                                    └─→ Registration Success Message
```

### View Stored Procedure Results Flow
```
Admin Dashboard
    │
    └─→ Click "View Event-wise Participants"
        │
        └─→ ViewEventParticipants.java Opens
            │
            ├─→ Load All Events
            │
            └─→ User Selects Event (ID 1)
                │
                └─→ Click "View Participants" Button
                    │
                    └─→ Call Stored Procedure
                        │
                        └─→ CALL GetEventParticipants(1)
                            │
                            └─→ SQL PROCEDURE executes:
                                │
                                ├─→ JOIN participation par
                                ├─→ JOIN participant p
                                ├─→ WHERE par.event_id = 1
                                └─→ ORDER BY p.participant_name
                                    │
                                    └─→ Returns:
                                        ├─ Participant ID
                                        ├─ Participant Name
                                        ├─ Email
                                        ├─ Score
                                        └─ Registration Time
                                        │
                                        └─→ Display in Table
```

---

## 🔄 Application Lifecycle

```
1. START APPLICATION
   └─→ main() in Main.java
       │
       ├─→ Test Database Connection
       │   └─→ DBConnection.testConnection()
       │
       ├─→ Connection Success? YES ──┐
       │                              │
       │                              ▼
       │                      Launch Login UI
       │                              │
       └─→ Connection Failed? ────────┴─→ Show Error Dialog
                                          │
                                          └─→ EXIT(1)

2. LOGIN PHASE
   Login Screen Shows
   │
   ├─→ User clicks "Admin Login"
   │   └─→ Open AdminDashboard
   │
   └─→ User clicks "Student Login"
       │
       └─→ Input Student ID
           │
           ├─→ Valid ID? YES ──→ Open StudentDashboard(ID)
           │
           └─→ Invalid ID? ───→ Show Error & Retry

3. ADMIN OPERATIONS
   AdminDashboard Shows Menu
   │
   ├─→ Add Department ──→ Form ──→ INSERT department
   ├─→ Add Organiser ──→ Form ──→ INSERT organiser
   ├─→ Add Event ──→ Form ──→ INSERT event
   ├─→ Update Score ──→ Select & Update ──→ UPDATE participation
   ├─→ View Participants ──→ SELECT all participants
   └─→ View Event Participants ──→ CALL GetEventParticipants()

4. STUDENT OPERATIONS
   StudentDashboard Shows Menu
   │
   ├─→ Register Event ──→ SELECT available ──→ INSERT participation
   ├─→ View My Events ──→ SELECT my events
   └─→ View My Scores ──→ SELECT my scores

5. LOGOUT
   └─→ Return to Login Screen
```

---

## 🗂️ Package Structure

```
CLASSPATH:
    │
    ├─ db/
    │   └─ DBConnection (Database connection singleton)
    │
    ├─ models/
    │   ├─ Department (Entity)
    │   ├─ Organiser (Entity)
    │   ├─ Participant (Entity)
    │   ├─ Event (Entity)
    │   └─ Participation (Entity)
    │
    ├─ ui/
    │   ├─ Login (Entry UI)
    │   ├─ AdminDashboard (Admin menu)
    │   ├─ StudentDashboard (Student menu)
    │   ├─ AddDepartment (Form)
    │   ├─ AddOrganiser (Form)
    │   ├─ AddEvent (Form)
    │   ├─ ScoreUpdate (Table + update)
    │   ├─ ViewParticipants (Table)
    │   ├─ ViewEventParticipants (Table + stored proc)
    │   ├─ RegisterEvent (Dropdown + insert)
    │   ├─ ViewMyEvents (Table)
    │   └─ ViewScores (Table)
    │
    └─ Main (Entry point)
```

---

## 🔗 Class Dependencies

```
Main
  ├─→ Login (invokes)
      ├─→ AdminDashboard
      │   ├─→ AddDepartment (uses DBConnection)
      │   ├─→ AddOrganiser (uses DBConnection + Department model)
      │   ├─→ AddEvent (uses DBConnection + Organiser model)
      │   ├─→ ScoreUpdate (uses DBConnection)
      │   ├─→ ViewParticipants (uses DBConnection)
      │   └─→ ViewEventParticipants (uses DBConnection + stored proc)
      │
      └─→ StudentDashboard (uses DBConnection + Participant)
          ├─→ RegisterEvent (uses DBConnection + Event model)
          ├─→ ViewMyEvents (uses DBConnection + Event model)
          └─→ ViewScores (uses DBConnection)

DBConnection
  └─→ Used by all UI classes
      └─→ For database operations

Models
  └─→ Used by UI classes
      └─→ For data representation
```

---

## 📈 Database Query Patterns Used

### INSERT (Create)
```java
String sql = "INSERT INTO table (col1, col2) VALUES (?, ?)";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, value1);
pstmt.setInt(2, value2);
pstmt.executeUpdate();
```

### SELECT (Read)
```java
String sql = "SELECT * FROM table WHERE condition = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, conditionValue);
ResultSet rs = pstmt.executeQuery();
while (rs.next()) {
    // Process data
}
```

### UPDATE (Update)
```java
String sql = "UPDATE table SET column = ? WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, newValue);
pstmt.setInt(2, id);
pstmt.executeUpdate();
```

### Stored Procedure (Call)
```java
String sql = "CALL GetEventParticipants(?)";
CallableStatement cstmt = conn.prepareCall(sql);
cstmt.setInt(1, eventId);
ResultSet rs = cstmt.executeQuery();
```

---

## ✅ Error Handling Flow

```
Try Block
    │
    ├─→ Execute DB Operation
    │
    ├─→ Success ──→ Process Results
    │
    └─→ Exception ──→ Catch Block
                      │
                      ├─→ Check Exception Type
                      │
                      ├─→ Duplicate Entry? ──→ Show "Already Exists"
                      ├─→ Invalid Format? ──→ Show "Invalid Input"
                      ├─→ Connection Error? ──→ Show "DB Error"
                      └─→ Other Error? ──→ Show "Error: " + message
                      │
                      └─→ Log to Console

Finally Block
    └─→ Close Connections & Resources
```

---

## 🎯 UI Component Patterns

### Form Components
```
┌─────────────────┐
│   Header Panel  │  (Blue background, title label)
├─────────────────┤
│   Form Panel    │  (GridLayout for input fields)
│  ┌───────────┐  │
│  │ Label     │  │
│  │ [TextField]│  │
│  ├───────────┤  │
│  │ Label     │  │
│  │ [ComboBox]│  │
│  └───────────┘  │
├─────────────────┤
│ Button Panel    │  (Add, Cancel buttons)
└─────────────────┘
```

### Table Components
```
┌─────────────────┐
│   Header Panel  │  (Colored header)
├─────────────────┤
│  [JTable]       │  (Non-editable, scroll enabled)
│  ┌───────────┐  │
│  │Col1│Col2  │  │
│  ├────┼───────┤  │
│  │Data│Data  │  │
│  │Data│Data  │  │
│  └───────────┘  │
└─────────────────┘
```

---

## 🚀 Deployment Architecture

```
Development Machine
    │
    ├─ XAMPP
    │   └─ MySQL Server (localhost:3306)
    │       └─ eventdb
    │           └─ 5 Tables + Trigger + Procedure
    │
    ├─ JDK
    │   └─ javac (compiler)
    │       └─ Compiles .java to .class
    │
    ├─ MySQL JDBC Driver
    │   └─ mysql-connector-j-8.0.33.jar
    │       └─ Enables Java-MySQL communication
    │
    └─ IDE or Terminal
        └─ Compiles & Runs Application
```

---

**This architecture ensures a clean separation of concerns, proper data flow, and maintainable code structure.**
