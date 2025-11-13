package entity;

import java.util.ArrayList;
import java.util.List;

/** Entity class representing a Student user. */
public class Student extends User {
    /** The student's year of study (1-4). */
    private int year;
    
    /** The student's major (e.g., CSC). */
    private String major;
    
    /** A list of IDs for applications submitted by this student. */
    private final List<String> submittedApplicationIDs;
    
    /** The ID of the application this student has accepted, or null. */
    private String acceptedInternship;

    /** Constructs a new Student. 
     * @param userID User ID. 
     * @param name Full name. 
     * @param email Email. 
     * @param pwd Password. 
     * @param year Year of study. 
     * @param major Major. 
    */
    public Student(String userID, String name, String email, String pwd, int year, String major) {
        super(userID, name, email, pwd, "Student");
        this.year = year;
        this.major = major;
        this.submittedApplicationIDs = new ArrayList<>();
        this.acceptedInternship = null;
    }

    //getters
    
    /**
     * Gets the student's year of study.
     * @return The student's year (1-4).
     */
    public int getYear() { return year; }
    
    /**
     * Gets the student's major.
     * @return The student's major (e.g., "CS").
     */
    public String getMajor() { return major; }
    
    /**
     * Gets the list of application IDs submitted by this student.
     * @return The list of application IDs.
     */
    public List<String> getSubmittedApplicationIDs() { return submittedApplicationIDs; }
    
    /**
     * Gets the ID of the application the student has accepted.
     * @return The accepted application ID, or null if none.
     */
    public String getAcceptedInternshipID() { return acceptedInternship; }
    
    /**
     * Checks if the student has accepted any internship placement.
     * @return true if an internship is accepted, false otherwise.
     */
    public boolean hasAcceptedInternship() { return this.acceptedInternship != null; }
    
    //setters
    
    /**
     * Sets the student's year of study.
     * @param y The new year of study.
     */
    public void setYear(int y) { this.year = y; }
    
    /**
     * Sets the student's major.
     * @param m The new major.
     */
    public void setMajor(String m) { this.major = m; }
    
    /**
     * Sets the student's accepted placement.
     * @param appID The application ID of the accepted placement.
     */
    public void setAcceptedPlacement(String appID) { this.acceptedInternship = appID; }

    /** Adds an application ID to this student's list. 
     * @param appID The ID to add. 
     * @return true if added, false if already present. 
    */
    public boolean addApplication(String appID) {
        if(!this.submittedApplicationIDs.contains(appID)) {
            this.submittedApplicationIDs.add(appID);
            return true;
        } else { return false; }
    }
    
    /** Removes an application ID from this student's list. 
     * @param appID The ID to remove. 
     * @return true if removed, false if not found. 
    */
    public boolean removeApplication(String appID) {
        if(this.submittedApplicationIDs.contains(appID)) {
            this.submittedApplicationIDs.remove(appID);
            return true;
        } else { return false; }
    }

}
