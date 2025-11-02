package entity;

import java.util.ArrayList;
import java.util.List;
public class CompanyRepresentative extends User {
    private String company;
    private String department;
    private String position;
    private boolean staffApproved;
    private final List<String> postedInternshipIDs;

    public CompanyRepresentative(String userID, String name, String email, String pwd, String companyname, String dep, String position) {
        super(userID, name, email, pwd, "CompanyRepresentative");
        this.company = companyname;
        this.department = dep;
        this.position = position;
        this.postedInternshipIDs = new ArrayList<>();
    }

    //getters
    public String getCompanyName() { return this.company; }
    public String getDepartment() { return this.department; }
    public String getPosition() { return this.position; }
    public List<String> getPostedInternshipIDs() { return this.postedInternshipIDs; }
    public boolean approvedRepresentative() { return this.staffApproved; }

    //setters
    public void setCompanyName(String company) { this.company = company; }
    public void setDepartment(String dep) { this.department = dep; }
    public void setPosition(String pos) { this.position = pos; }
    public void approveRepresentative(boolean approve) { this.staffApproved = approve; }
    public boolean addInternship(String internshipID) { 
        if(!this.postedInternshipIDs.contains(internshipID)) { this.postedInternshipIDs.add(internshipID);return true; }
        return false;
    }
    public boolean removeInternship(String internshipID) {
        if(this.postedInternshipIDs.contains(internshipID)) { this.postedInternshipIDs.remove(internshipID);return true; }
        return false;
    }
}
