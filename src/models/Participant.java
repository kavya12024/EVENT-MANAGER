package models;

public class Participant {
    private int participantId;
    private String participantName;
    private int departmentId;
    private String email;
    private String enrollmentNo;

    public Participant() {}

    public Participant(int participantId, String participantName, int departmentId, String email, String enrollmentNo) {
        this.participantId = participantId;
        this.participantName = participantName;
        this.departmentId = departmentId;
        this.email = email;
        this.enrollmentNo = enrollmentNo;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnrollmentNo() {
        return enrollmentNo;
    }

    public void setEnrollmentNo(String enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
    }

    @Override
    public String toString() {
        return participantName;
    }
}
