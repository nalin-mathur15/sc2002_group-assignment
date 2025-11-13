package entity;

/** Entity class for a student's application to a specific internship. */
public class InternshipApplication {
    /** Represents the various states of an application (PENDING, UNSUCCESSFUL, SUCCESSFUL, ACCEPTED, PENDING_WITHDRAWAL, WITHDRAWN). */
    public enum ApplicationStatus {
        PENDING, UNSUCCESSFUL, SUCCESSFUL, ACCEPTED, PENDING_WITHDRAWAL, WITHDRAWN
    }

    /** The unique ID for this application. */
    private final String applicationID;
    
    /** The ID of the internship this application is for. */
    private final String internshipID;
    
    /** The ID of the student who submitted this application. */
    private final String studentID;
    
    /** The current status (e.g., PENDING) of the application. */
    private ApplicationStatus status;
    
    /** Flag indicating if the student has confirmed/accepted this placement. */
    private boolean studentAccepted;

    /** Constructs a new InternshipApplication. 
     * @param applicationID Unique ID. 
     * @param internshipID Internship ID. 
     * @param studentID Student ID. 
    */
    public InternshipApplication(String applicationID, String internshipID, String studentID) {
        this.applicationID = applicationID;
        this.internshipID = internshipID;
        this.studentID = studentID;
        this.status = ApplicationStatus.PENDING;
        this.studentAccepted = false;
    }

    //getters
    
    /**
     * Gets the application's unique ID.
     * @return The application ID.
     */
    public String getApplicationID() { return applicationID; }
    
    /**
     * Gets the ID of the internship this application is for.
     * @return The internship ID.
     */
    public String getInternshipID() { return internshipID; }
    
    /**
     * Gets the ID of the student who submitted this application.
     * @return The student ID.
     */
    public String getStudentID() { return studentID; }
    
    /**
     * Gets the current status of the application.
     * @return The application status.
     */
    public ApplicationStatus getStatus() { return status; }
    
    /**
     * Checks if the student has confirmed and accepted this application.
     * @return true if the student has accepted, false otherwise.
     */
    public boolean isConfirmedByStudent() { return studentAccepted; }

    //setters
    
    /**
     * Sets the status of the application.
     * @param status The new application status.
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    
    /**
     * Sets the student's confirmation status for this application.
     * @param isConfirmed true if the student is accepting, false otherwise.
     */
    public void studentConfirmation(boolean isConfirmed) {
        this.studentAccepted = isConfirmed;
    }
}
