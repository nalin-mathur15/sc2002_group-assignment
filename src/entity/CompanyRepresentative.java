package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/** Entity class representing a Company Representative user. */
public class CompanyRepresentative extends User {
    /** The name of the company the representative works for. */
    private String company;
    
    /** The representative's department. */
    private String department;
    
    /** The representative's position or job title. */
    private String position;
    
    /** Flag indicating if a Staff member has approved this account. */
    private boolean staffApproved;
    
    /** A list of IDs for internships posted by this representative. */
    private final List<String> postedInternshipIDs;

    /** Constructs a new CompanyRepresentative. 
     * @param userID User ID (email). 
     * @param name Full name. 
     * @param email Email. 
     * @param pwd Password. 
     * @param companyname Company name. 
     * @param dep Department. 
     * @param position Position. 
    */
    public CompanyRepresentative(String userID, String name, String email, String pwd, String companyname, String dep, String position) {
        super(userID, name, email, pwd, "CompanyRepresentative");
        this.company = companyname;
        this.department = dep;
        this.position = position;
        this.postedInternshipIDs = new ArrayList<>();
    }

    /** Get the company of the representative.
     * @return Company name.
    */
    public String getCompanyName() { return this.company; }
    
    /** Get the department of the representative.
     * @return Department.
     */
    public String getDepartment() { return this.department; }

    /** Get the position of the representative in their company.
     * @return Position.
    */
    public String getPosition() { return this.position; }
    
    /** Get a List of the IDs of internships that this representative has posted
     * @return List of posted internships
    */
    public List<String> getPostedInternshipIDs() { 
        return Collections.unmodifiableList(this.postedInternshipIDs); 
    }
    
    /** Find whether the representative has been approved by a staff member.
     * @return true if approved, false if not.
    */
    public boolean approvedRepresentative() { return this.staffApproved; }

    /** Set the company name of the representative.
     * @param company The new company name to set.
     */
    public void setCompanyName(String company) { this.company = company; }
    
    /** Set the department of the representative in their company.
    * @param dep The new department to set.
    */
    public void setDepartment(String dep) { this.department = dep; }
    
    /** Set the position of the representative in their company.
     * @param pos The new position to set.
     */
    public void setPosition(String pos) { this.position = pos; }

    /** For the Staff to approve the representative.
     * @param approve true if approved, false if rejected
     */
    public void approveRepresentative(boolean approve) { this.staffApproved = approve; }
    
    /** Add an internship to the list of posted internships by this representative.
     * @param internshipID The internship ID.
     * @return true if added, false if the internship was already posted.
     */
    public boolean addInternship(String internshipID) { 
        if(!this.postedInternshipIDs.contains(internshipID)) { this.postedInternshipIDs.add(internshipID);return true; }
        return false;
    }

    /** Remove an internship from the list of posted internships by this representative.
     * @param internshipID The internship ID.
     * @return true if removed, false if the internship was not found.
     */
    public boolean removeInternship(String internshipID) {
        if(this.postedInternshipIDs.contains(internshipID)) { this.postedInternshipIDs.remove(internshipID);return true; }
        return false;
    }
}
