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

    // Student submits an application for an internship
    public boolean submitApplication(Student student, Internship internship) {
        // Check if student already applied
        for (String appID : student.getSubmittedApplicationIDs()) {
            InternshipApplication app = applications.get(appID);
            if(app != null && app.getInternshipID().equals(internship.getInternshipID())) {
                System.out.println("You have already applied for this internship.");
                return false;
            }
        }

        // Check if student reached max applications
        if (student.getSubmittedApplicationIDs().size() > 3) {
            System.out.println("Error: You have reached the maximum number of applications.");
            return false;
        }

        // Check that internship is valid
        if (internship.getStatus() != InternshipStatus.APPROVED) { System.out.println("Error: This internship is not approved for applications."); return false; }
        if (!internship.isVisible()) { System.out.println("Error: This internship is not currently visible.");return false; }
        if (internship.getStatus() == InternshipStatus.FULL) { System.out.println("Error: This internship is already filled.");return false; }

        // Check if the deadline has passed.
        LocalDate today = LocalDate.now();
        if (today.isAfter(internship.getApplicationCloseDate())) { System.out.println("Error: The application deadline for this internship has passed.");return false; }

        // Check eligibility.
        int year = student.getYear();
        InternshipLevel level = internship.getLevel();

        if ((year == 1 || year == 2) &&
            (level == InternshipLevel.INTERMEDIATE || level == InternshipLevel.ADVANCED)) {
            System.out.println("Error: Year 1 and 2 students can only apply for BASIC level internships.");
            return false;
        }

        // Create new application
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

    // Company rep reviews and updates application status
    public boolean updateApplicationStatus(String applicationId, ApplicationStatus newStatus) {
        InternshipApplication app = applications.get(applicationId);
        if (app == null) {
            System.out.println("Application not found.");
            return false;
        }
        app.setStatus(newStatus);
        Student student = allStudents.get(app.getStudentID());
        String studentName = (student != null) ? student.getName() : "Unknown Student";
        System.out.println("Application status updated to " + newStatus + " for " + studentName);
        return true;
    }

    // List all applications for a specific internship
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

    // List all applications by a student
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

    // Check placement outcome for a student
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
}
