package controller;

import java.util.*;
import java.util.stream.Collectors;
import entity.*;
import entity.Internship.InternshipLevel;
import entity.Internship.InternshipStatus;

public class InternshipManager {
    // Manages listing of internships, creating, approving, and status of internship

    private Map<String, Internship> internships;

    public InternshipManager() {
        internships = new HashMap<>();
    }

    // Add internship (by company rep)
    public void addInternship(Internship internship) {
        internships.put(internship.getInternshipID(), internship);
        System.out.println("Internship \"" + internship.getTitle() + "\" added successfully.");
    }

    // Approve internship (career staff)
    public boolean approveInternship(String internshipId) {
        Internship i = internships.get(internshipId);
        if (i != null && i.getStatus() == InternshipStatus.PENDING) {
            i.setStatus(InternshipStatus.APPROVED);
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
            i.setHidden(!visible);
            System.out.println("Visibility for \"" + i.getTitle() + "\" set to " + visible);
            return true;
        }
        return false;
    }

    // List internships available to a student
    public List<Internship> getInternshipsForStudent(Student s) {
        return internships.values().stream()
                .filter(i -> !i.hidden())
                .filter(i -> i.getPreferredMajor().equalsIgnoreCase(s.getMajor()))
                .filter(i -> s.getYear() >= 3 || i.getLevel() == InternshipLevel.BASIC)
                .collect(Collectors.toList());
    }

    // Filter internships (for career staff report)
    public List<Internship> filterInternships(String major, InternshipLevel level, InternshipStatus status) {
        return internships.values().stream()
                .filter(i -> (major == null || i.getPreferredMajor().equalsIgnoreCase(major)))
                .filter(i -> (level == null || i.getLevel() == level))
                .filter(i -> (status == null || i.getStatus() == status))
                .collect(Collectors.toList());
    }

    // Get a specific internship by ID
    public Internship getInternshipById(String internshipId) {
        return internships.get(internshipId);
    }

    // Get all internships
    public Collection<Internship> getAllInternships() {
        return internships.values();
    }
}
