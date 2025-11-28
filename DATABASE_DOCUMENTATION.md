# Database Documentation - Triggers and Stored Procedures

## 📋 Database: eventdb

---

## 🔔 TRIGGERS

### 1. before_participation_insert
**Type:** BEFORE INSERT  
**Table:** participation  
**Purpose:** Automatically set score to 0 if NULL value is inserted

**SQL Code:**
```sql
DELIMITER $$
CREATE TRIGGER before_participation_insert
BEFORE INSERT ON participation
FOR EACH ROW
BEGIN
    IF NEW.score IS NULL THEN
        SET NEW.score = 0;
    END IF;
END$$
DELIMITER ;
```

**When it fires:**
- Every time a new participation record is inserted
- If score field is NULL, automatically sets it to 0
- This ensures no NULL scores in the database

**Example:**
```sql
INSERT INTO participation (participant_id, event_id) 
VALUES (1, 2);
-- Score is automatically set to 0 by the trigger
```

---

## 📦 STORED PROCEDURES

### 1. GetEventParticipants(event_id_param)
**Type:** SELECT Procedure  
**Purpose:** Retrieve all participants for a specific event with their scores

**SQL Code:**
```sql
DELIMITER $$
CREATE PROCEDURE GetEventParticipants(IN event_id_param INT)
BEGIN
    SELECT 
        p.participant_id,
        p.participant_name,
        p.email,
        par.score,
        par.registration_time
    FROM participation par
    JOIN participant p ON par.participant_id = p.participant_id
    WHERE par.event_id = event_id_param
    ORDER BY p.participant_name;
END$$
DELIMITER ;
```

**Parameters:**
- `event_id_param` (IN) - The event ID to get participants for

**Returns:**
- participant_id - Student ID
- participant_name - Student name
- email - Student email
- score - Current score in event
- registration_time - When student registered

**How to call from Java:**
```java
String sql = "CALL GetEventParticipants(?)";
CallableStatement cstmt = conn.prepareCall(sql);
cstmt.setInt(1, eventId);
ResultSet rs = cstmt.executeQuery();

while (rs.next()) {
    int participantId = rs.getInt("participant_id");
    String name = rs.getString("participant_name");
    int score = rs.getInt("score");
    // Process data...
}
```

**Example Usage:**
```sql
CALL GetEventParticipants(1);
-- Returns all participants registered for event_id = 1
```

**Output Sample:**
```
participant_id | participant_name | email              | score | registration_time
1              | Aarav Gupta      | aarav@student.edu  | 85    | 2025-11-28 10:15:00
3              | Chetan Verma     | chetan@student.edu | 92    | 2025-11-28 10:20:00
9              | Ishita Prabhu    | ishita@student.edu | 81    | 2025-11-28 10:25:00
10             | Jaideep Nair     | jaideep@student.edu| 88    | 2025-11-28 10:30:00
```

---

## 📊 DATABASE SCHEMA

### Table: department
```
dept_id       INT (Primary Key, Auto Increment)
dept_name     VARCHAR(100) UNIQUE
created_at    TIMESTAMP
```

### Table: organiser
```
organiser_id  INT (Primary Key, Auto Increment)
organiser_name VARCHAR(100)
dept_id       INT (Foreign Key -> department.dept_id)
email         VARCHAR(100)
phone         VARCHAR(15)
created_at    TIMESTAMP
```

### Table: participant
```
participant_id INT (Primary Key, Auto Increment)
participant_name VARCHAR(100)
department_id  INT (Foreign Key -> department.dept_id)
email          VARCHAR(100)
enrollment_no  VARCHAR(50) UNIQUE
created_at     TIMESTAMP
```

### Table: event
```
event_id       INT (Primary Key, Auto Increment)
event_name     VARCHAR(150)
event_date     DATE
event_description VARCHAR(500)
organiser_id   INT (Foreign Key -> organiser.organiser_id)
max_participants INT DEFAULT 100
created_at     TIMESTAMP
```

### Table: participation
```
participation_id INT (Primary Key, Auto Increment)
participant_id   INT (Foreign Key -> participant.participant_id)
event_id         INT (Foreign Key -> event.event_id)
score            INT DEFAULT 0
registration_time TIMESTAMP
UNIQUE: (participant_id, event_id)
```

---

## 🔗 Foreign Key Relationships

```
department
    ↑
    └── organiser (dept_id FK)
    ↑
    └── participant (department_id FK)

organiser
    ↓
    └── event (organiser_id FK)

participant          event
    ↓                ↓
    └─── participation ──┘
         (participant_id FK)
         (event_id FK)
```

---

## 📝 Key Constraints

1. **Unique Constraints:**
   - department.dept_name - No duplicate departments
   - participant.enrollment_no - Each student has unique enrollment number
   - participation(participant_id, event_id) - Student can't register twice for same event

2. **Default Values:**
   - participation.score = 0 (Also set by trigger)
   - participation.registration_time = CURRENT_TIMESTAMP
   - event.max_participants = 100

3. **Cascading Delete:**
   - All ON DELETE CASCADE - Deleting parent automatically deletes child records

---

## 🔧 Common SQL Queries

### Get all participants for Event ID 1
```sql
CALL GetEventParticipants(1);
```

### Get event participants manually (without stored procedure)
```sql
SELECT 
    p.participant_id,
    p.participant_name,
    p.email,
    par.score
FROM participation par
JOIN participant p ON par.participant_id = p.participant_id
WHERE par.event_id = 1
ORDER BY p.participant_name;
```

### Update a student's score
```sql
UPDATE participation 
SET score = 90 
WHERE participant_id = 1 AND event_id = 2;
```

### Count participants in an event
```sql
SELECT COUNT(*) as participant_count
FROM participation
WHERE event_id = 1;
```

### Get average score for an event
```sql
SELECT AVG(score) as avg_score
FROM participation
WHERE event_id = 1;
```

### Get student's all event scores
```sql
SELECT 
    e.event_name,
    par.score,
    par.registration_time
FROM participation par
JOIN event e ON par.event_id = e.event_id
WHERE par.participant_id = 1
ORDER BY e.event_date;
```

---

## ✅ Validation & Data Integrity

1. **No NULL scores** - Trigger ensures score is always 0 or positive
2. **Unique participations** - Student can't register twice for same event
3. **Foreign key constraints** - Can't reference non-existent records
4. **Cascading deletes** - Maintains referential integrity

---

## 📊 Sample Data Statistics

After running eventdb_schema.sql:
- **Departments:** 5
- **Organisers:** 4
- **Participants:** 10
- **Events:** 5
- **Participations:** 10

---

## 🔐 Security Notes

1. No passwords currently stored (demo version)
2. Implement hashed passwords for production
3. Use parameterized queries (already done in application)
4. Add user authentication/authorization layer
5. Implement audit logging for changes

---

**End of Database Documentation**
