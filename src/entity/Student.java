package entity;

import java.util.ArrayList;
import java.util.List;
public class Student extends User {
    private int year;
    private String major;
    private final List<String> submittedApplicationIDs;
    private String acceptedInternship;

    public Student(String userID, String name, String pwd, int year, String major) {
        super(userID, name, pwd, "Student");
        this.year = year;
        this.major = major;
        this.submittedApplicationIDs = new ArrayList<>();
        this.acceptedInternship = null;
    }

    //getters
    public int getYear() { return year; }
    public String getMajor() { return major; }
    public List<String> getSubmittedApplicationIDs() { return submittedApplicationIDs; }
    public String getAcceptedInternshipID() { return acceptedInternship; }
    public boolean hasAcceptedInternship() { return this.acceptedInternship != null; }
    public String getStudentID() { return this.getUserID(); }
    //setters
    public void setYear(int y) { this.year = y; }
    public void setMajor(String m) { this.major = m; }
    public void setAcceptedPlacement(String appID) { this.acceptedInternship = appID; }

    public boolean addApplication(String appID) {
        if(!this.submittedApplicationIDs.contains(appID)) {
            this.submittedApplicationIDs.add(appID);
            return true;
        } else { return false; }
    }
    public boolean removeApplication(String appID) {
        if(this.submittedApplicationIDs.contains(appID)) {
            this.submittedApplicationIDs.remove(appID);
            return true;
        } else { return false; }
    }

}
