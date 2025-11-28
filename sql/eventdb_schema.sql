-- College Event Management System - Database Schema
-- Database: eventdb

-- Drop database if exists
DROP DATABASE IF EXISTS eventdb;

-- Create database
CREATE DATABASE eventdb;
USE eventdb;

-- Table 1: Department
CREATE TABLE department (
    dept_id INT PRIMARY KEY AUTO_INCREMENT,
    dept_name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table 2: Student
CREATE TABLE student (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    enrollment_no VARCHAR(50) UNIQUE,
    department_id INT NOT NULL,
    year INT DEFAULT 1,
    semester INT DEFAULT 1,
    phone VARCHAR(15),
    is_active BOOLEAN DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES department(dept_id) ON DELETE CASCADE
);

-- Table 3: Organiser
CREATE TABLE organiser (
    organiser_id INT PRIMARY KEY AUTO_INCREMENT,
    organiser_name VARCHAR(100) NOT NULL,
    dept_id INT NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES department(dept_id) ON DELETE CASCADE
);

-- Table 4: Participant
CREATE TABLE participant (
    participant_id INT PRIMARY KEY AUTO_INCREMENT,
    participant_name VARCHAR(100) NOT NULL,
    department_id INT NOT NULL,
    email VARCHAR(100),
    enrollment_no VARCHAR(50) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES department(dept_id) ON DELETE CASCADE
);

-- Table 5: Event
CREATE TABLE event (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    event_name VARCHAR(150) NOT NULL,
    event_date DATE NOT NULL,
    event_description VARCHAR(500),
    organiser_id INT NOT NULL,
    max_participants INT DEFAULT 100,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organiser_id) REFERENCES organiser(organiser_id) ON DELETE CASCADE
);

-- Table 6: Participation
CREATE TABLE participation (
    participation_id INT PRIMARY KEY AUTO_INCREMENT,
    participant_id INT NOT NULL,
    event_id INT NOT NULL,
    score INT DEFAULT 0,
    registration_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (participant_id) REFERENCES participant(participant_id),
    FOREIGN KEY (event_id) REFERENCES event(event_id) ON DELETE CASCADE,
    UNIQUE KEY unique_participation (participant_id, event_id)
);

-- Trigger: Before inserting into participation, set score = 0 if NULL
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

-- Stored Procedure: GetEventParticipants
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

-- Sample Data Insertion

-- Insert Departments
INSERT INTO department (dept_name) VALUES 
('Computer Science'),
('Mechanical Engineering'),
('Electrical Engineering'),
('Civil Engineering'),
('Electronics');

-- Insert Students (New Table)
INSERT INTO student (email, password, full_name, enrollment_no, department_id, year, semester, phone) VALUES 
('aarav@student.edu', 'pass123', 'Aarav Gupta', 'CS001', 1, 2, 3, '9876543220'),
('bhavna@student.edu', 'pass123', 'Bhavna Singh', 'CS002', 1, 2, 3, '9876543221'),
('chetan@student.edu', 'pass123', 'Chetan Verma', 'CS003', 1, 2, 3, '9876543222'),
('diana@student.edu', 'pass123', 'Diana Kapoor', 'ME001', 2, 3, 5, '9876543223'),
('eshan@student.edu', 'pass123', 'Eshan Malhotra', 'ME002', 2, 3, 5, '9876543224'),
('farhan@student.edu', 'pass123', 'Farhan Khan', 'EE001', 3, 1, 1, '9876543225'),
('gitanjali@student.edu', 'pass123', 'Gitanjali Roy', 'EE002', 3, 1, 1, '9876543226'),
('harsh@student.edu', 'pass123', 'Harsh Saxena', 'CE001', 4, 4, 7, '9876543227'),
('ishita@student.edu', 'pass123', 'Ishita Prabhu', 'CS004', 1, 2, 3, '9876543228'),
('jaideep@student.edu', 'pass123', 'Jaideep Nair', 'ME003', 2, 3, 5, '9876543229'),
('student1@college.edu', 'student123', 'Demo Student', 'DEMO001', 1, 2, 4, '9999999999');

-- Insert Organisers
INSERT INTO organiser (organiser_name, dept_id, email, phone) VALUES 
('Dr. Rajesh Kumar', 1, 'rajesh@college.edu', '9876543210'),
('Prof. Amit Singh', 2, 'amit@college.edu', '9876543211'),
('Ms. Priya Sharma', 3, 'priya@college.edu', '9876543212'),
('Dr. Vikram Patel', 4, 'vikram@college.edu', '9876543213');

-- Insert Participants (Students)
INSERT INTO participant (participant_name, department_id, email, enrollment_no) VALUES 
('Aarav Gupta', 1, 'aarav@student.edu', 'CS001'),
('Bhavna Singh', 1, 'bhavna@student.edu', 'CS002'),
('Chetan Verma', 1, 'chetan@student.edu', 'CS003'),
('Diana Kapoor', 2, 'diana@student.edu', 'ME001'),
('Eshan Malhotra', 2, 'eshan@student.edu', 'ME002'),
('Farhan Khan', 3, 'farhan@student.edu', 'EE001'),
('Gitanjali Roy', 3, 'gitanjali@student.edu', 'EE002'),
('Harsh Saxena', 4, 'harsh@student.edu', 'CE001'),
('Ishita Prabhu', 1, 'ishita@student.edu', 'CS004'),
('Jaideep Nair', 2, 'jaideep@student.edu', 'ME003');

-- Insert Events
INSERT INTO event (event_name, event_date, event_description, organiser_id, max_participants) VALUES 
('Code Marathon 2025', '2025-12-15', 'Programming competition for all students', 1, 50),
('Tech Talk - AI & ML', '2025-12-20', 'Interactive session on Artificial Intelligence', 1, 100),
('Robotics Workshop', '2025-12-22', 'Hands-on robotics workshop', 2, 30),
('Debate Competition', '2025-12-25', 'Inter-departmental debate', 3, 40),
('Quiz Master', '2025-12-28', 'General knowledge quiz', 4, 60);

-- Insert Participation (Students registering for events)
INSERT INTO participation (participant_id, event_id, score) VALUES 
(1, 1, 85),
(2, 1, 78),
(3, 1, 92),
(4, 3, 88),
(5, 3, 76),
(6, 4, 0),
(7, 4, 0),
(8, 5, 95),
(9, 2, 0),
(10, 1, 81);
