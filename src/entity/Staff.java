package entity;

public class Staff extends User {
    private String department;
    private String role;
    public Staff(String staffID, String name, String email, String pwd, String dep, String role) {
        super(staffID, name, email, pwd, "Staff");
        this.department = dep;
        this.role = role;
    }

    //getter
    public String getDepartment() { return this.department; }
    public String getRole() { return this.role; }

    //setter
    public void setDepartment(String dep) { this.department = dep; }
    public void setRole(String role) { this.role = role; }
}

