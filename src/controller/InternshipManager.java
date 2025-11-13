package controller;

import java.util.*;
import java.util.stream.Collectors;
import entity.*;
import entity.Internship.InternshipLevel;
import entity.Internship.InternshipStatus;
import java.time.LocalDate;

/** Controller class to manage the creation, filtering, and status of internships. */
public class InternshipManager {
    /** In-memory map of all internships, keyed by Internship ID. */
    private Map<String, Internship> internships;
    
    /** Constructs the InternshipManager. 
     * @param internships Map of all internships. 
    */
    public InternshipManager(Map<String, Internship> internships) {
        this.internships = internships;
    }

    /** Creates a new internship and adds it to the map. 
     * @param title Title. 
     * @param description Description. 
     * @param level Level. 
     * @param major Major. 
     * @param openDate Open date. 
     * @param closeDate Close date. 
     * @param rep The representative posting the job. 
     * @param slots Number of slots. 
    */
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


    /** Toggles the visibility of an internship. 
     * @param internshipId The internship ID. 
     * @return true on success, false if internship not found. 
    */
    public boolean toggleVisibility(String internshipId) {
        Internship i = internships.get(internshipId);
        if (i != null) {
            i.setVisibility(!i.isVisible());
            return true;
        }
        return false;
    }

    /** Gets a filtered list of internships available to a specific student. 
     * @param s The student. 
     * @return A list of eligible and visible internships. 
    */
    public List<Internship> getInternshipsForStudent(Student s) {
        return internships.values().stream()
                .filter(i -> i.getStatus() == InternshipStatus.APPROVED)
                .filter(i -> i.isVisible())
                .filter(i -> i.getPreferredMajor().equalsIgnoreCase(s.getMajor()))
                .filter(i -> s.getYear() >= 3 || i.getLevel() == InternshipLevel.BASIC)
                .collect(Collectors.toList());
    }

    /** Gets a filtered list of internships based on criteria (for staff reports). 
     * @param major The major to filter by (or null). 
     * @param level The level to filter by (or null). 
     * @param status The status to filter by (or null). 
     * @return A list of matching internships. 
    */
    public List<Internship> filterInternships(String major, InternshipLevel level, InternshipStatus status) {
        return internships.values().stream()
                .filter(i -> (major == null || i.getPreferredMajor().equalsIgnoreCase(major)))
                .filter(i -> (level == null || i.getLevel() == level))
                .filter(i -> (status == null || i.getStatus() == status))
                .collect(Collectors.toList());
    }

    /** Gets a single internship by ID. 
     * @param internshipId The internship ID. 
     * @return The Internship object, or null. 
    */
    public Internship getInternshipById(String internshipId) {
        return internships.get(internshipId);
    }
    
    /** Gets all internships posted by a specific company. 
     * @param compName The company name. 
     * @return A list of internships. 
    */
    public List<Internship> getInternshipsByCompany(String compName){
    	return internships.values().stream()
                .filter(i -> i.getCompanyName().equalsIgnoreCase(compName))
                .collect(Collectors.toList());
    }

    /** Gets all internships in the system, after updating expired ones. 
     * @return A collection of all internships. 
    */
    public Collection<Internship> getAllInternships() {
        updateExpiredInternships();
        return internships.values();
    }
    
    /** Private helper to check for and close internships past their deadline. */
    private void updateExpiredInternships() {
        LocalDate today = LocalDate.now();
        for(Internship i : internships.values()) {
            if(i.getStatus() == InternshipStatus.APPROVED) {
                if(today.isAfter(i.getApplicationCloseDate())) {
                    i.setStatus(InternshipStatus.CLOSED);
                }
            }
        }
    }
}
