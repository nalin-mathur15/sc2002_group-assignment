package entity;

/** Entity class representing a Staff user. */
public class Staff extends User {
    /** The staff member's department. */
    private String department;

    /** Constructs a new Staff member. 
     * @param staffID User ID. 
     * @param name Full name. 
     * @param email Email. 
     * @param pwd Password. 
     * @param dep Department. 
     * @param role Role. 
    */
    public Staff(String staffID, String name, String email, String pwd, String dep, String role) {
        super(staffID, name, email, pwd, role);
        this.department = dep;
    }

    /** Get the department of the Staff member.
     * @return Department.
     */
    public String getDepartment() { return this.department; }

    /** Set the department of the Staff member. */
    public void setDepartment(String dep) { this.department = dep; }
}

