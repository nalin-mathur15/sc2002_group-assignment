package entity;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

/** Entity class for an internship opportunity. */
public class Internship {
    /** Represents the various states of an internship (PENDING, APPROVED, REJECTED, FULL, CLOSED). */
    public enum InternshipStatus {
        PENDING,    /** pending staff approval */ 
        APPROVED,   /** approved (will be listed for students view now) */
        REJECTED,   /** rejected by staff */
        FULL,       /** no more spaces left (will become hidden from student view) */
        CLOSED      /** past the deadline */
    }

    /** Represents the difficulty level of an internship (BASIC, INTERMEDIATE, ADVANCED). */
    public enum InternshipLevel {
        BASIC,          /** Basic Internships, accessibly by Year 1-4 */
        INTERMEDIATE,   /** Intermediate Internships, accessible by Year 3-4 */
        ADVANCED        /** Advanced Internships, accessible by Year 3-4 */
    }

    /** The unique ID for the internship. */
    private final String internshipID;
    
    /** The title of the internship. */
    private String title;
    
    /** A description of the internship. */
    private String description;
    
    /** The level (e.g., BASIC) of the internship. */
    private InternshipLevel level;
    
    /** The preferred major for applicants. */
    private String preferredMajor;
    
    /** The date applications open. */
    private LocalDate openDate;
    
    /** The date applications close. */
    private LocalDate closeDate;
    
    /** The current status (e.g., PENDING) of the internship. */
    private InternshipStatus status;
    
    /** The name of the company. */
    private final String employer;
    
    /** The ID of the rep who posted this. */
    private final String companyRepresentativeID;
    
    /** The total number of available slots. */
    private int slots;
    
    /** The number of slots that have been accepted by students. */
    private int filledSlots;
    
    /** Flag indicating if students can see this internship. */
    private boolean visible;
    
    /** A list of IDs for applications submitted to this internship. */
    private final List<String> applicationIDs;

    /** Constructs a new Internship. 
     * @param internshipID Unique ID. 
     * @param title Title. 
     * @param description Description. 
     * @param level Level. 
     * @param preferredMajor Preferred major. 
     * @param openDate Open date.
     * @param closeDate Close date. 
     * @param companyName Company name. 
     * @param companyRepID Rep's ID. 
     * @param numberOfSlots Total slots. 
    */
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
        this.visible = false;
        this.filledSlots = 0;
        this.applicationIDs = new ArrayList<>();
    }


/**
     * Gets the internship's unique ID.
     * @return The internship ID.
     */
    public String getInternshipID() { return internshipID; }
    
    /**
     * Gets the internship title.
     * @return The title.
     */
    public String getTitle() { return title; }
    
    /**
     * Gets the internship description.
     * @return The description.
     */
    public String getDescription() { return description; }
    
    /**
     * Gets the internship level.
     * @return The internship level.
     */
    public InternshipLevel getLevel() { return level; }
    
    /**
     * Gets the preferred major.
     * @return The preferred major.
     */
    public String getPreferredMajor() { return preferredMajor; }
    
    /**
     * Gets the application opening date.
     * @return The open date.
     */
    public LocalDate getApplicationOpenDate() { return openDate; }
    
    /**
     * Gets the application closing date.
     * @return The close date.
     */
    public LocalDate getApplicationCloseDate() { return closeDate; }
    
    /**
     * Gets the current status of the internship.
     * @return The internship status.
     */
    public InternshipStatus getStatus() { return status; }
    
    /**
     * Gets the company name (employer).
     * @return The company name.
     */
    public String getCompanyName() { return employer; }
    
    /**
     * Gets the ID of the company representative who posted this.
     * @return The company representative's ID.
     */
    public String getCompanyRepID() { return companyRepresentativeID; }
    
    /**
     * Gets the total number of slots for this internship.
     * @return The total number of slots.
     */
    public int getNumberOfSlots() { return slots; }
    
    /**
     * Gets the number of slots that have been filled (accepted by students).
     * @return The number of filled slots.
     */
    public int getSlotsFilled() { return filledSlots; }
    
    /**
     * Calculates and gets the number of remaining available slots.
     * @return The number of available slots.
     */
    public int getNumberOfSlotsAvailable() { return slots - filledSlots; }
    
    /**
     * Checks if the internship is currently visible to students.
     * @return true if visible, false otherwise.
     */
    public boolean isVisible() { return visible; }
    
    /**
     * Gets the list of application IDs submitted for this internship.
     * @return The list of application IDs.
     */
    public List<String> getApplicationIDs() { return applicationIDs; }

    //setters
    
    /**
     * Sets the internship title.
     * @param title The new title.
     */
    public void setTitle(String title) { this.title = title; }
    
    /**
     * Sets the internship description.
     * @param description The new description.
     */
    public void setDescription(String description) { this.description = description; }
    
    /**
     * Sets the internship level.
     * @param level The new level.
     */
    public void setLevel(InternshipLevel level) { this.level = level; }
    
    /**
     * Sets the preferred major.
     * @param preferredMajor The new preferred major.
     */
    public void setPreferredMajor(String preferredMajor) { this.preferredMajor = preferredMajor; }
    
    /**
     * Sets the application opening date.
     * @param date The new open date.
     */
    public void setApplicationOpenDate(LocalDate date) { this.openDate = date; }
    
    /**
     * Sets the application closing date.
     * @param date The new close date.
     */
    public void setApplicationCloseDate(LocalDate date) { this.closeDate = date; }
    
    /**
     * Sets the total number of slots (max 10).
     * @param numberOfSlots The total number of slots.
     */
    public void setNumberOfSlots(int numberOfSlots) { this.slots = (numberOfSlots > 10) ? 10 : numberOfSlots; }
    
    /**
     * Sets the internship status.
     * @param status The new status.
     */
    public void setStatus(InternshipStatus status) { this.status = status; }
    
    /**
     * Sets the internship's visibility to students.
     * @param hide true to make visible, false to hide.
     */
    public void setVisibility(boolean hide) { this.visible = hide; }

    /**
     * Sets the number of filled slots, ensuring it's valid.
     * Used by DataHandler when loading from file.
     * @param slotsFilled The number of filled slots.
     */
    public void setSlotsFilled(int slotsFilled) {
        if (slotsFilled >= 0 && slotsFilled <= this.slots) {
            this.filledSlots = slotsFilled;
        } else {
            this.filledSlots = 0;
        }
    }

    /**
     * Increments the filled slot count by one if not already full.
     */
    public void incrementSlotsFilled() {
        if (this.filledSlots < this.slots) {
            this.filledSlots++;
        }
    }

    /**
     * Decrements the filled slot count by one if not already empty.
     */
    public void decrementSlotsFilled() {
        if (this.filledSlots > 0) {
            this.filledSlots--;
        }
    }
    
    /**
     * Adds an application ID to this internship's list.
     * @param applicationID The ID to add.
     */
    public void addApplication(String applicationID) {
        if (!this.applicationIDs.contains(applicationID)) {
            this.applicationIDs.add(applicationID);
        }
    }
    
    /**
     * Removes an application ID from this internship's list.
     * @param applicationID The ID to remove.
     */
    public void removeApplication(String applicationID) {
        this.applicationIDs.remove(applicationID);
    }

    /**
     * Prints a detailed, formatted string of the internship's state to the console.
     */
    public void toStringDetailed() {
        System.out.printf("Internship ID: %s | Title: %s | Description: %s | Level: %s | Preferred Major: %s\nOpen Date: %s | Close Date: %s | Company: %s | Status: %s | Slots Filled: %d/%d | Visible: %b\n",
                internshipID, title, description, level, preferredMajor, openDate, closeDate, employer, status, filledSlots, slots, visible);
    }
}