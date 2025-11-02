package entity;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class Internship {
    // class for an internship opportunity
    public enum InternshipStatus {
        PENDING,    //pending staff approval 
        APPROVED,   //approved (will be listed for students view now) 
        REJECTED,   //rejected by staff
        FULL        // no more spaces left (will become hidden from student view)
    }
    public enum InternshipLevel {
        BASIC, INTERMEDIATE, ADVANCED
    }

    private final String internshipID;
    private String title;
    private String description;
    private InternshipLevel level;
    private String preferredMajor;
    private LocalDate openDate;
    private LocalDate closeDate;
    private InternshipStatus status;
    private final String employer;
    private final String companyRepresentativeID;
    private int slots;
    private int filledSlots;
    private boolean visible;
    private final List<String> applicationIDs;

    public Internship(String internshipID, String title, String description,
                      InternshipLevel level, String preferredMajor,
                      LocalDate openDate, LocalDate closeDate, String companyName,
                      String companyRepID, int numberOfSlots) {
        this.internshipID = internshipID;
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.employer = companyName;
        this.companyRepresentativeID = companyRepID;
        this.slots = (numberOfSlots > 10) ? 10 : numberOfSlots;
        this.status = InternshipStatus.PENDING;
        this.visible = true;
        this.filledSlots = 0;
        this.applicationIDs = new ArrayList<>();
    }

    //getters
    public String getInternshipID() { return internshipID; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public InternshipLevel getLevel() { return level; }
    public String getPreferredMajor() { return preferredMajor; }
    public LocalDate getApplicationOpenDate() { return openDate; }
    public LocalDate getApplicationCloseDate() { return closeDate; }
    public InternshipStatus getStatus() { return status; }
    public String getCompanyName() { return employer; }
    public String getCompanyRepID() { return companyRepresentativeID; }
    public int getNumberOfSlots() { return slots; }
    public int getSlotsFilled() { return filledSlots; }
    public boolean isVisible() { return visible; }
    public List<String> getApplicationIDs() { return applicationIDs; }

    //setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLevel(InternshipLevel level) { this.level = level; }
    public void setPreferredMajor(String preferredMajor) { this.preferredMajor = preferredMajor; }
    public void setApplicationOpenDate(LocalDate date) { this.openDate = date; }
    public void setApplicationCloseDate(LocalDate date) { this.closeDate = date; }
    public void setNumberOfSlots(int numberOfSlots) { this.slots = (numberOfSlots > 10) ? 10 : numberOfSlots; }
    public void setStatus(InternshipStatus status) { this.status = status; }
    public void setVisibility(boolean hide) { this.visible = hide; }
    public void fillOneSlot() {
        if (this.filledSlots < this.slots) {
            this.filledSlots++;
        }
        // Controller should check if (slotsFilled == numberOfSlots) and set status to FILLED.
    }
    public void clearOneSlot() {
        if (this.filledSlots > 0) {
            this.filledSlots--;
        }
    }
    public void addApplication(String applicationID) {
        if (!this.applicationIDs.contains(applicationID)) {
            this.applicationIDs.add(applicationID);
        }
    }
    public void removeApplication(String applicationID) {
        this.applicationIDs.remove(applicationID);
    }
}