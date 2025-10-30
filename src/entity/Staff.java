package entity;

public class Staff extends User {
    private String department;
    public Staff(String staffID, String name, String pwd, String dep) {
        super(staffID, name, pwd, "Staff");
        this.department = dep;
    }

    //getter
    public String getDepartment() { return this.department; }

    //setter
    public void setDepartment(String dep) { this.department = dep; }
}

