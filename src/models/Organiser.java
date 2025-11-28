package models;

public class Organiser {
    private int organiserId;
    private String organiserName;
    private int deptId;
    private String email;
    private String phone;

    public Organiser() {}

    public Organiser(int organiserId, String organiserName, int deptId, String email, String phone) {
        this.organiserId = organiserId;
        this.organiserName = organiserName;
        this.deptId = deptId;
        this.email = email;
        this.phone = phone;
    }

    public int getOrganiserId() {
        return organiserId;
    }

    public void setOrganiserId(int organiserId) {
        this.organiserId = organiserId;
    }

    public String getOrganiserName() {
        return organiserName;
    }

    public void setOrganiserName(String organiserName) {
        this.organiserName = organiserName;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return organiserName;
    }
}
