package entity;

public class InternshipApplication {
    // class for a student's application to a specific internship
    public enum ApplicationStatus {
        PENDING, UNSUCCESSFUL, SUCCESSFUL, PENDING_WITHDRAWAL, WITHDRAWN
    }
    private final String applicationID;
    private final String internshipID;
    private final String studentID;
    private ApplicationStatus status;
    private boolean studentAccepted;

    public InternshipApplication(String applicationID, String internshipID, String studentID) {
        this.applicationID = applicationID;
        this.internshipID = internshipID;
        this.studentID = studentID;
        this.status = ApplicationStatus.PENDING;
        this.studentAccepted = false;
    }

    //getters
    public String getApplicationID() { return applicationID; }
    public String getInternshipID() { return internshipID; }
    public String getStudentID() { return studentID; }
    public ApplicationStatus getStatus() { return status; }
    public boolean isConfirmedByStudent() { return studentAccepted; }

    //setters
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    public void studentConfirmation(boolean isConfirmed) {
        this.studentAccepted = isConfirmed;
    }

}
