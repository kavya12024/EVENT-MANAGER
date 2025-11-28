package models;

import java.time.LocalDateTime;

public class Student {
    private int studentId;
    private String email;
    private String password;
    private String fullName;
    private String enrollmentNo;
    private int departmentId;
    private int year;
    private int semester;
    private String phone;
    private boolean isActive;
    private LocalDateTime createdAt;

    // Constructor with all fields
    public Student(int studentId, String email, String password, String fullName, 
                   String enrollmentNo, int departmentId, int year, int semester, 
                   String phone, boolean isActive, LocalDateTime createdAt) {
        this.studentId = studentId;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.enrollmentNo = enrollmentNo;
        this.departmentId = departmentId;
        this.year = year;
        this.semester = semester;
        this.phone = phone;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Constructor without ID and timestamps (for new students)
    public Student(String email, String password, String fullName, String enrollmentNo, 
                   int departmentId, int year, int semester, String phone) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.enrollmentNo = enrollmentNo;
        this.departmentId = departmentId;
        this.year = year;
        this.semester = semester;
        this.phone = phone;
        this.isActive = true;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEnrollmentNo() {
        return enrollmentNo;
    }

    public void setEnrollmentNo(String enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", enrollmentNo='" + enrollmentNo + '\'' +
                ", departmentId=" + departmentId +
                ", year=" + year +
                ", semester=" + semester +
                ", phone='" + phone + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
