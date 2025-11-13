package controller;

import java.time.LocalDate;
import java.util.*;
import entity.*;
import entity.Internship.InternshipLevel;
import entity.Internship.InternshipStatus;
import entity.InternshipApplication.ApplicationStatus;

/** Controller class to manage student applications and internship placements. */
public class ApplicationManager {
    /** Maximum number of applications a student can make. */
    public final int MAX_APPS = 3;

    /** In-memory map of all applications, keyed by Application ID. */
    private final Map<String, InternshipApplication> allApplications;

    /** In-memory map of all internships, keyed by Internship ID. */
    private final Map<String, Internship> allInternships;

    /** In-memory map of all students, keyed by Student ID. */
    private final Map<String, Student> allStudents;
    
    /** Constructs the ApplicationManager. 
     * @param allApplications Map of all applications. 
     * @param allInternships Map of all internships. 
     * @param allStudents Map of all students. 
    */
    public ApplicationManager(Map<String, InternshipApplication> allApplications,
                              Map<String, Internship> allInternships,
                              Map<String, Student> allStudents) {
        this.allApplications = allApplications;
        this.allInternships = allInternships;
        this.allStudents = allStudents;
    }

    /** Logic to submit a new application for a student. 
     * @param student The student applying. 
     * @param internship The internship being applied for. 
     * @return true on success, false otherwise. 
    */
    public String submitApplication(Student student, Internship internship) {
        for (String appID : student.getSubmittedApplicationIDs()) {
            InternshipApplication app = allApplications.get(appID);
            if(app != null && app.getInternshipID().equals(internship.getInternshipID())) {
                return "You have already applied for this internship.";
            }
        }

        // Check if student reached max applications
        if (student.getSubmittedApplicationIDs().size() >= MAX_APPS) {
            return "Error: You have reached the maximum number of applications.";
        }

        if (internship.getStatus() != InternshipStatus.APPROVED) { return "Error: This internship is not approved for applications."; }
        if (!internship.isVisible()) { return "Error: This internship is not currently visible."; }
        if (internship.getStatus() == InternshipStatus.FULL) { return "Error: This internship is already filled."; }

        LocalDate today = LocalDate.now();
        if (today.isAfter(internship.getApplicationCloseDate())) { return "Error: The application deadline for this internship has passed."; }

        // eligibility.
        int year = student.getYear();
        InternshipLevel level = internship.getLevel();

        if ((year == 1 || year == 2) &&
            (level == InternshipLevel.INTERMEDIATE || level == InternshipLevel.ADVANCED)) {
            return "Error: Year 1 and 2 students can only apply for BASIC level internships.";
        }

        // create application
        String applicationID = "APP_" + UUID.randomUUID().toString().substring(0, 8);
        InternshipApplication app = new InternshipApplication(
            applicationID,
            internship.getInternshipID(),
            student.getUserID());
        allApplications.put(app.getApplicationID(), app);

        student.addApplication(app.getApplicationID());
        internship.addApplication(app.getApplicationID());

        return null;
    }

    /** Logic for a student to accept a SUCCESSFUL application. 
     * @param applicationID The ID of the application to accept. 
     * @return true on success, false otherwise. 
    */
    public boolean acceptPlacement(String applicationID) {
        InternshipApplication app = allApplications.get(applicationID);
        if (app == null) { 
            System.out.println("Error: Application not found. ");
            return false;
        }
        Student s = allStudents.get(app.getStudentID());
        if (s == null) {
            System.out.println("Error: Student not found. ");
            return false;
        }

        // only accept successful offers
        if (app.getStatus() != ApplicationStatus.SUCCESSFUL) {
            System.out.println("Error: This offer cannot be accepted (Status: " + app.getStatus() + ")");
            return false;
        }
        // cannot already have a placement
        if (getConfirmedPlacement(s) != null) {
            System.out.println("Error: You have already accepted another placement. ");
            return false;
        }
        Internship i = allInternships.get(app.getInternshipID());
        if (i == null) {
            System.out.println("Error: Internship not found. ");
            return false;
        }

        app.studentConfirmation(true);
        i.incrementSlotsFilled();
        if (i.getSlotsFilled() == i.getNumberOfSlots()) {
            i.setStatus(InternshipStatus.FULL);
        }
        for (String otherAppID : s.getSubmittedApplicationIDs()) {
            if(!otherAppID.equals(applicationID)) {
                InternshipApplication other = allApplications.get(otherAppID);
                if (other != null && other.getStatus() != ApplicationStatus.WITHDRAWN) {
                    other.setStatus(ApplicationStatus.WITHDRAWN);
                }
            }
        }

        System.out.println("Placement at " + i.getTitle() + " confirmed!");
        System.out.println("All other applications have been withdrawn. ");
        return true;
    }

    /** Logic for a student to request withdrawal from an application. 
     * @param applicationID The ID of the application to withdraw from. 
     * @return true on success, false otherwise. 
    */
    public boolean requestWithdrawal(String applicationID) {
        InternshipApplication app = allApplications.get(applicationID);
        if (app == null) { System.out.println("Error: Application not found. "); return false; }
        ApplicationStatus status = app.getStatus();
        if (status == ApplicationStatus.PENDING || status == ApplicationStatus.SUCCESSFUL) {
            app.setStatus(ApplicationStatus.PENDING_WITHDRAWAL);
            System.out.println("Withdrawal request submitted. A staff member will review soon. ");
            return true;
        }
        System.out.println("Error: An application of status " + status + " cannot be withdrawn. ");
        return false;
    }

    /** Logic for a Company Rep to update an application status (e.g., to SUCCESSFUL). 
     * @param applicationId The ID of the application. 
     * @param newStatus The new status. 
     * @return true on success, false otherwise. 
    */
    public boolean updateApplicationStatus(String applicationId, ApplicationStatus newStatus) {
        InternshipApplication app = allApplications.get(applicationId);
        if (app == null) {
            System.out.println("Application not found.");
            return false;
        }
        if (app.getStatus() != ApplicationStatus.PENDING) {
            System.out.println("Error: This application is not pending (Status: " + app.getStatus() + ").");
            return false;
        }

        Internship internship = allInternships.get(app.getInternshipID());

        // Check on max slots for accepting applications
        if (newStatus == ApplicationStatus.SUCCESSFUL || newStatus == ApplicationStatus.ACCEPTED) {
            if (internship.getNumberOfSlotsAvailable() == 0) {
                System.out.println("Internship Slots are already full.");
                return false;
            }
        }
        // Handle slot adjustments
        else if (newStatus == ApplicationStatus.ACCEPTED) {
            internship.incrementSlotsFilled();
            if (internship.getNumberOfSlotsAvailable() == 0) { internship.setStatus(InternshipStatus.FULL); }
        }
        else if (newStatus == ApplicationStatus.WITHDRAWN) {
            internship.decrementSlotsFilled();
            if (internship.getStatus() == InternshipStatus.FULL) { internship.setStatus(InternshipStatus.APPROVED); }
        }
        app.setStatus(newStatus);
        Student student = allStudents.get(app.getStudentID());
        String studentName = (student != null) ? student.getName() : "Unknown Student";
        System.out.println("Application status updated to " + newStatus + " for " + studentName);
        return true;
    }

    /** Logic for a Staff member to approve or reject a withdrawal request. 
     * @param applicationID The ID of the application. 
     * @param approve true to approve, false to reject. 
     * @return true on success, false otherwise. 
    */
    public boolean approveWithdrawalRequest(String applicationID, boolean approve) {
        InternshipApplication app = allApplications.get(applicationID);
        if (app == null) { System.out.println("Error: Application not found. "); return false; }
        ApplicationStatus status = app.getStatus();
        Student student = allStudents.get(app.getStudentID());
        Internship internship = allInternships.get(app.getInternshipID());
        if (student == null || internship == null) {
            System.out.println("Error: Could not find student or internship. ");
            return false;
        }
        if (status == ApplicationStatus.PENDING_WITHDRAWAL) {
            boolean conf = app.isConfirmedByStudent();
            if (approve) { 
                app.setStatus(ApplicationStatus.WITHDRAWN); 
                app.studentConfirmation(false);
                if (conf) {
                    internship.decrementSlotsFilled();
                    if (internship.getStatus() == InternshipStatus.FULL) {
                        internship.setStatus(InternshipStatus.APPROVED);
                    }
                }
                System.out.println("Application " + applicationID + "successfully withdrawn by student " + student.getName() + ".");
                return true;
            } else {
                if (conf) {
                    app.setStatus(ApplicationStatus.SUCCESSFUL);
                } else {
                    app.setStatus(ApplicationStatus.PENDING);
                }
                System.out.println("Withdrawal request rejected for student " + student.getName() + ".");
                return true;
            }
        }
        System.out.println("Error: This application is not pending withdrawal. ");
        return false;
    }

    /** Gets all applications for a specific internship. 
     * @param internshipId The internship ID. 
     * @return A list of applications. 
    */
    public List<InternshipApplication> getApplicationsForInternship(String internshipId) {
        List<InternshipApplication> result = new ArrayList<>();
        Internship internship = allInternships.get(internshipId);
        if (internship != null) {
            for (String appID : internship.getApplicationIDs()) {
                InternshipApplication app = allApplications.get(appID);
                if(app != null) { result.add(app); }
            }
        }
        return result;
    }

    /** Gets all applications submitted by a specific student. 
     * @param studentId The student ID. 
     * @return A list of applications. 
    */
    public List<InternshipApplication> getApplicationsByStudent(String studentId) {
        List<InternshipApplication> result = new ArrayList<>();
        Student student = allStudents.get(studentId);
        if(student != null) {
            for(String appID : student.getSubmittedApplicationIDs()) {
                InternshipApplication app = allApplications.get(appID);
                if (app != null) { result.add(app); }
            }
        }
        return result;
    }

    /** Checks if a student has an accepted placement. 
     * @param student The student. 
     * @return The confirmed Internship, or null if none. 
    */
    public Internship getConfirmedPlacement(Student student) {
        for (String appID : student.getSubmittedApplicationIDs()) {
            InternshipApplication app = allApplications.get(appID);
            if (app != null && app.isConfirmedByStudent()) {
                return allInternships.get(app.getInternshipID());                
            }
        }
        return null;
    }

    /** Gets all applications with PENDING status. 
     * @return A list of applications. 
    */
    public List<InternshipApplication> getPendingApplications() {
        List<InternshipApplication> pending = new ArrayList<>();
        for (InternshipApplication a : allApplications.values()) {
            if (a.getStatus() == ApplicationStatus.PENDING) {
                pending.add(a);
            }
        }
        return pending;
    }
    
    /** Gets all applications with PENDING_WITHDRAWAL status. 
     * @return A list of applications. 
    */
    public List<InternshipApplication> getPendingWithdrawals() {
        List<InternshipApplication> pendingWithdrawals = new ArrayList<>();
        for (InternshipApplication a : allApplications.values()) {
            if (a.getStatus() == ApplicationStatus.PENDING_WITHDRAWAL) {
                pendingWithdrawals.add(a);
            }
        }
        return pendingWithdrawals;
    }

    /** Gets all students. 
     * @return A collection of all students. 
    */
    public Collection<Student> getAllStudents() {
        return allStudents.values();
    }

    /** Gets a single student by ID. 
     * @param studentID The student ID. 
     * @return The Student object, or null.
    */
    public Student getStudentbyID(String studentID) {
        return allStudents.get(studentID);
    }
    
    /** Gets all applications. 
     * @return A collection of all applications. 
    */
    public Collection<InternshipApplication> getAllApplications() {
        return allApplications.values();
    }

    /** Gets a single application by ID. 
     * @param appID The application ID. 
     * @return The InternshipApplication object, or null. 
    */
    public InternshipApplication getApplicationbyID(String appID) {
        return allApplications.get(appID);
    }
    
    /** Gets all internships. 
     * @return A collection of all internships. 
    */
    public Collection<Internship> getAllInternships() {
        return allInternships.values();
    }
}
