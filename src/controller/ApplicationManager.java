package controller;

import java.util.*;
import entity.*;
import entity.InternshipApplication.ApplicationStatus;

public class ApplicationManager {
    // Manages student's applications and internship placements

    private Map<String, InternshipApplication> applications;

    public ApplicationManager() {
        applications = new HashMap<>();
    }

    // Student submits an application for an internship
    public boolean submitApplication(Student student, Internship internship) {
        // Check if student already applied
        for (InternshipApplication a : applications.values()) {
            if (a.getStudent().equals(student) && a.getInternship().equals(internship)) {
                System.out.println("You have already applied for this internship.");
                return false;
            }
        }

        // Check if student reached max applications
        if (student.getSubmittedApplicationIDs().size() >= 3) {
            System.out.println("You have reached the maximum number of applications.");
            return false;
        }

        // Create new application
        InternshipApplication app = new InternshipApplication(student, internship);
        applications.put(app.getApplicationID(), app);

        student.addApplication(app);
        internship.addApplicant(student);

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
        System.out.println("Application status updated to " + newStatus + " for " + app.getStudent().getName());
        return true;
    }

    // List all applications for a specific internship
    public List<InternshipApplication> getApplicationsForInternship(String internshipId) {
        List<InternshipApplication> result = new ArrayList<>();
        for (InternshipApplication a : applications.values()) {
            if (a.getInternship().getInternshipID().equals(internshipId)) {
                result.add(a);
            }
        }
        return result;
    }

    // List all applications by a student
    public List<InternshipApplication> getApplicationsByStudent(String studentId) {
        List<InternshipApplication> result = new ArrayList<>();
        for (InternshipApplication a : applications.values()) {
            if (a.getStudent().getUserID().equals(studentId)) {
                result.add(a);
            }
        }
        return result;
    }

    // Check placement outcome for a student
    public void checkPlacementStatus(Student student) {
        for (InternshipApplication a : applications.values()) {
            if (a.getStudent().equals(student) && a.getStatus() == ApplicationStatus.ACCEPTED) {
                System.out.println("You have been placed at: " + a.getInternship().getTitle());
                return;
            }
        }
        System.out.println("You have not been placed yet.");
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
