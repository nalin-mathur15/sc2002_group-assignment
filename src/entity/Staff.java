package entity;

public class Staff extends User {
    private String department;
    public Staff(String staffID, String name, String email, String pwd, String dep, String role) {
        super(staffID, name, email, pwd, role);
        this.department = dep;
    }

    //getter
    public String getDepartment() { return this.department; }

    //setter
    public void setDepartment(String dep) { this.department = dep; }
}

