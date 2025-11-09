package controller;

import java.time.LocalDate;
import java.util.*;
import entity.*;
import entity.Internship.InternshipLevel;
import entity.Internship.InternshipStatus;
import entity.InternshipApplication.ApplicationStatus;

public class ApplicationManager {
    // Manages student's applications and internship placements

    private final Map<String, InternshipApplication> applications;
    private final Map<String, Internship> allInternships;
    private final Map<String, Student> allStudents;
    
    public ApplicationManager(Map<String, InternshipApplication> allApplications,
                              Map<String, Internship> allInternships,
                              Map<String, Student> allStudents) {
        this.applications = allApplications;
        this.allInternships = allInternships;
        this.allStudents = allStudents;
    }

    // student submits an application for an internship
    public boolean submitApplication(Student student, Internship internship) {
        for (String appID : student.getSubmittedApplicationIDs()) {
            InternshipApplication app = applications.get(appID);
            if(app != null && app.getInternshipID().equals(internship.getInternshipID())) {
                System.out.println("You have already applied for this internship.");
                return false;
            }
        }

        // Check if student reached max applications
        if (student.getSubmittedApplicationIDs().size() > 2) {
            System.out.println("Error: You have reached the maximum number of applications.");
            return false;
        }

        if (internship.getStatus() != InternshipStatus.APPROVED) { System.out.println("Error: This internship is not approved for applications."); return false; }
        if (!internship.isVisible()) { System.out.println("Error: This internship is not currently visible.");return false; }
        if (internship.getStatus() == InternshipStatus.FULL) { System.out.println("Error: This internship is already filled.");return false; }

        LocalDate today = LocalDate.now();
        if (today.isAfter(internship.getApplicationCloseDate())) { System.out.println("Error: The application deadline for this internship has passed.");return false; }

        // eligibility.
        int year = student.getYear();
        InternshipLevel level = internship.getLevel();

        if ((year == 1 || year == 2) &&
            (level == InternshipLevel.INTERMEDIATE || level == InternshipLevel.ADVANCED)) {
            System.out.println("Error: Year 1 and 2 students can only apply for BASIC level internships.");
            return false;
        }

        // create application
        String applicationID = "APP_" + UUID.randomUUID().toString().substring(0, 8);
        InternshipApplication app = new InternshipApplication(
            applicationID,
            internship.getInternshipID(),
            student.getStudentID());
        applications.put(app.getApplicationID(), app);

        student.addApplication(app.getApplicationID());
        internship.addApplication(app.getApplicationID());

        System.out.println("Application for \"" + internship.getTitle() + "\" submitted successfully!");
        return true;
    }

    public boolean updateApplicationStatus(String applicationId, ApplicationStatus newStatus) {
        InternshipApplication app = applications.get(applicationId);
        if (app == null) {
            System.out.println("Application not found.");
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
            internship.fillOneSlot();
            if (internship.getNumberOfSlotsAvailable() == 0) { internship.setStatus(InternshipStatus.FULL); }
        }
        else if (newStatus == ApplicationStatus.WITHDRAWN) {
            internship.clearOneSlot();
            if (internship.getStatus() == InternshipStatus.FULL) { internship.setStatus(InternshipStatus.APPROVED); }
        }
        app.setStatus(newStatus);
        Student student = allStudents.get(app.getStudentID());
        String studentName = (student != null) ? student.getName() : "Unknown Student";
        System.out.println("Application status updated to " + newStatus + " for " + studentName);
        return true;
    }

    // all applications for a specific internship
    public List<InternshipApplication> getApplicationsForInternship(String internshipId) {
        List<InternshipApplication> result = new ArrayList<>();
        Internship internship = allInternships.get(internshipId);
        if (internship != null) {
            for (String appID : internship.getApplicationIDs()) {
                InternshipApplication app = applications.get(appID);
                if(app != null) { result.add(app); }
            }
        }
        return result;
    }

    // all applications by a student
    public List<InternshipApplication> getApplicationsByStudent(String studentId) {
        List<InternshipApplication> result = new ArrayList<>();
        Student student = allStudents.get(studentId);
        if(student != null) {
            for(String appID : student.getSubmittedApplicationIDs()) {
                InternshipApplication app = applications.get(appID);
                if (app != null) { result.add(app); }
            }
        }
        return result;
    }

    // placement outcome for a student
    public Internship getConfirmedPlacement(Student student) {
        for (String appID : student.getSubmittedApplicationIDs()) {
            InternshipApplication app = applications.get(appID);
            if (app != null && app.isConfirmedByStudent()) {
                return allInternships.get(app.getInternshipID());                
            }
        }
        return null;
    }

    // Get all pending applications (for admin/rep)
    public List<InternshipApplication> getPendingApplications() {
        List<InternshipApplication> pending = new ArrayList<>();
        for (InternshipApplication a : applications.values()) {
            if (a.getStatus() == ApplicationStatus.PENDING) {
                pending.add(a);
            }
        }
        return pending;
    }
    
    public List<InternshipApplication> getPendingWithdrawals() {
        List<InternshipApplication> pendingWithdrawals = new ArrayList<>();
        for (InternshipApplication a : applications.values()) {
            if (a.getStatus() == ApplicationStatus.PENDING_WITHDRAWAL) {
                pendingWithdrawals.add(a);
            }
        }
        return pendingWithdrawals;
    }

    public Collection<Student> getAllStudents() {
        return allStudents.values();
    }
    public Collection<InternshipApplication> getAllApplications() {
        return applications.values();
    }
    public Collection<Internship> getAllInternships() {
        return allInternships.values();
    }
}
