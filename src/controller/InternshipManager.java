package controller;

import java.util.*;
import java.util.stream.Collectors;
import entity.*;
import entity.Internship.InternshipLevel;
import entity.Internship.InternshipStatus;
import java.time.LocalDate;

public class InternshipManager {
    // Manages listing of internships, creating, approving, and status of internship
    private Map<String, Internship> internships;
    
    public InternshipManager(Map<String, Internship> internships) {
        this.internships = internships;
    }

    public void createInternship(String title, String description, InternshipLevel level, String major,
                                 LocalDate openDate, LocalDate closeDate, CompanyRepresentative rep, int slots)  {
        
        String internID = "INT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Internship newIntern = new Internship(
            internID, title, description, level, major,
            openDate, closeDate, rep.getCompanyName(), rep.getUserID(), slots
        );
        internships.put(internID, newIntern);
        rep.addInternship(internID);
        System.out.println("Internship \"" + newIntern.getTitle() + "\" added successfully. Pending Staff approval.");
    
    }


    // Toggle visibility (company rep)
    public boolean toggleVisibility(String internshipId, boolean visible) {
        Internship i = internships.get(internshipId);
        if (i != null) {
            i.setVisibility(visible);
            System.out.println("Visibility for \"" + i.getTitle() + "\" set to " + visible);
            return true;
        }
        return false;
    }

    // List internships available to a student
    public List<Internship> getInternshipsForStudent(Student s) {
        return internships.values().stream()
                .filter(i -> i.getStatus() == InternshipStatus.APPROVED)
                .filter(i -> i.isVisible())
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
    
    public List<Internship> getInternshipsByCompany(String compName){
    	return internships.values().stream()
                .filter(i -> i.getCompanyName().equalsIgnoreCase(compName))
                .collect(Collectors.toList());
    }

    // Get all internships
    public Collection<Internship> getAllInternships() {
        updateExpiredInternships();
        return internships.values();
    }
    
    private void updateExpiredInternships() {
        LocalDate today = LocalDate.now();
        for(Internship i : internships.values()) {
            if(i.getStatus() == InternshipStatus.APPROVED) {
                if(today.isAfter(i.getApplicationCloseDate())) {
                    i.setStatus(InternshipStatus.CLOSED);
                    // for debugging
                    // System.out.println("[InternshipManager] Internship '" + internship.getTitle() + "' has expired and is closed.");
                }
            }
        }
    }
}
