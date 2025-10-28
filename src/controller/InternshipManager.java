package controller;

import java.util.*;
import java.util.stream.Collectors;
import model.*;

public class InternshipManager {
    // Manages listing of internships, creating, approving, and status of internship

    private Map<String, Internship> internships;

    public InternshipManager() {
        internships = new HashMap<>();
    }

    // Add internship (by company rep)
    public void addInternship(Internship internship) {
        internships.put(internship.getInternshipId(), internship);
        System.out.println("Internship \"" + internship.getTitle() + "\" added successfully.");
    }

    // Approve internship (career staff)
    public boolean approveInternship(String internshipId) {
        Internship i = internships.get(internshipId);
        if (i != null && i.getStatus().equalsIgnoreCase("Pending")) {
            i.setStatus("Approved");
            System.out.println("Internship \"" + i.getTitle() + "\" approved.");
            return true;
        }
        System.out.println("Internship not found or already processed.");
        return false;
    }

    // Toggle visibility (company rep)
    public boolean toggleVisibility(String internshipId, boolean visible) {
        Internship i = internships.get(internshipId);
        if (i != null) {
            i.setVisible(visible);
            System.out.println("Visibility for \"" + i.getTitle() + "\" set to " + visible);
            return true;
        }
        return false;
    }

    // List internships available to a student
    public List<Internship> getInternshipsForStudent(Student s) {
        return internships.values().stream()
                .filter(i -> i.isVisible())
                .filter(i -> i.getPreferredMajor().equalsIgnoreCase(s.getMajor()))
                .filter(i -> s.getYearOfStudy() >= 3 || i.getLevel().equalsIgnoreCase("Basic"))
                .collect(Collectors.toList());
    }

    // Student applies for internship
    public boolean applyInternship(Student s, String internshipId) {
        Internship i = internships.get(internshipId);
        if (i == null || !i.isVisible()) {
            System.out.println("Internship not available.");
            return false;
        }

        if (s.getApplications().size() >= 3) {
            System.out.println("You have reached the maximum number of applications.");
            return false;
        }

        s.apply(i);
        i.addApplicant(s);
        System.out.println("Applied for \"" + i.getTitle() + "\" successfully!");
        return true;
    }

    // Update application status (company rep)
    public void updateApplicationStatus(String internshipId, String studentId, String status) {
        Internship i = internships.get(internshipId);
        if (i != null) {
            i.updateApplicationStatus(studentId, status);
            System.out.println("Application status updated for student " + studentId);
        }
    }

    // Filter internships (career staff report)
    public List<Internship> filterInternships(String major, String level, String status) {
        return internships.values().stream()
                .filter(i -> (major == null || i.getPreferredMajor().equalsIgnoreCase(major)))
                .filter(i -> (level == null || i.getLevel().equalsIgnoreCase(level)))
                .filter(i -> (status == null || i.getStatus().equalsIgnoreCase(status)))
                .collect(Collectors.toList());
    }

    // View applications for an internship
    public void viewApplications(String internshipId) {
        Internship i = internships.get(internshipId);
        if (i != null) {
            i.printApplications();
        } else {
            System.out.println("Internship not found.");
        }
    }
}
