package controller;

import entity.CompanyRepresentative;
import entity.Internship;
import entity.Internship.InternshipStatus;

import java.util.*;
public class ApprovalManager {
    // handle all requests that require staff approval (such as opening a new internship position)
    private final Map<String, CompanyRepresentative> allCompanyReps;
    private final Map<String, Internship> allInternships;

    public ApprovalManager(Map<String, CompanyRepresentative> allCompanyReps,
                           Map<String, Internship> allInternships) {
        this.allCompanyReps = allCompanyReps;
        this.allInternships = allInternships;
    }
    
    public List<CompanyRepresentative> getPendingRepresentativeApprovals() {
        List<CompanyRepresentative> pending = new ArrayList<>();
        for(CompanyRepresentative c : allCompanyReps.values()) {
            if(!c.approvedRepresentative()) {
                pending.add(c);
            }
        }
        return pending;
    }

    public List<Internship> getPendingInternshipApprovals() {
        List<Internship> pending = new ArrayList<>();
        for(Internship i : allInternships.values()) {
            if(i.getStatus() == InternshipStatus.PENDING) {
                pending.add(i);
            }
        }
        return pending;
    }

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
        return true;
    }
    public Collection<CompanyRepresentative> getAllRepresentatives() {
        return allCompanyReps.values();
    }
}