package controller;

import entity.CompanyRepresentative;
import entity.Internship;
import entity.Internship.InternshipStatus;

import java.util.*;

/** Controller class to handle all requests that require staff approval. */
public class ApprovalManager {
    /** Reference to the in-memory map of all company representatives. */
    private final Map<String, CompanyRepresentative> allCompanyReps;

    /** Reference to the in-memory map of all internships. */
    private final Map<String, Internship> allInternships;

    /** Constructs the ApprovalManager. 
     * @param allCompanyReps Map of all company reps. 
     * @param allInternships Map of all internships.
    */
    public ApprovalManager(Map<String, CompanyRepresentative> allCompanyReps,
                           Map<String, Internship> allInternships) {
        this.allCompanyReps = allCompanyReps;
        this.allInternships = allInternships;
    }
    
    /** Gets all company reps pending staff approval. 
     * @return A list of company representatives. 
    */
    public List<CompanyRepresentative> getPendingRepresentativeApprovals() {
        List<CompanyRepresentative> pending = new ArrayList<>();
        for(CompanyRepresentative c : allCompanyReps.values()) {
            if(!c.approvedRepresentative()) {
                pending.add(c);
            }
        }
        return pending;
    }

    /** Gets all internships pending staff approval. 
     * @return A list of internships. 
    */
    public List<Internship> getPendingInternshipApprovals() {
        List<Internship> pending = new ArrayList<>();
        for(Internship i : allInternships.values()) {
            if(i.getStatus() == InternshipStatus.PENDING) {
                pending.add(i);
            }
        }
        return pending;
    }

    /** Logic for a Staff member to approve or reject a company rep account. 
     * @param repID The ID of the representative. 
     * @param approve true to approve, false to reject. 
     * @return true on success, false otherwise.
    */
    public boolean approveRepresentative(String repID, boolean approve) {
        CompanyRepresentative rep = allCompanyReps.get(repID);
        if (rep == null) { System.out.println("Error: The representative does not exist. "); return false; }
        rep.approveRepresentative(approve);
        if (approve) {
            System.out.println("Representative " + rep.getName() + " approved. ");
            return true;
        } else {
            allCompanyReps.remove(repID);
            System.out.println("Representative " + rep.getName() + " rejected and removed. ");
        }
        return true;
    }
    
    /** Logic for a Staff member to approve or reject an internship posting. 
     * @param internshipID The ID of the internship. 
     * @param approve true to approve, false to reject. 
     * @return true on success, false otherwise. 
    */
    public boolean approveInternship(String internshipID, boolean approve) {
        Internship internship = allInternships.get(internshipID);
        if (internship == null ) { System.out.println("Error: The internship does not exist. "); return false; }
        if (internship.getStatus() != InternshipStatus.PENDING) {
            System.out.println("Error: This internship is not pending approval. ");
            return false;
        }
        if (approve) {
            internship.setStatus(InternshipStatus.APPROVED);
            
            System.out.println("Internship \"" + internship.getTitle() + "\" (" + internshipID + ") approved. ");
        } else {
            internship.setStatus(InternshipStatus.REJECTED);
            System.out.println("Internship \"" + internship.getTitle() + "\" (" + internshipID + ") rejected. ");
        }
        internship.setVisibility(approve);
        return true;
    }
    
    /** Gets all company representatives. 
     * @return A collection of all company representatives. 
    */
    public Collection<CompanyRepresentative> getAllRepresentatives() {
        return allCompanyReps.values();
    }
}