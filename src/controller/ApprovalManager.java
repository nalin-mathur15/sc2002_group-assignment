package controller;

import entity.*;
import java.util.*;

/**
 * Handles all approval-related actions by Career Center Staff.
 * Includes approving company representatives, internship postings,
 * and student withdrawal requests.
 */
public class ApprovalManager {

    private Map<String, CompanyRepresentative> pendingReps;
    private Map<String, Internship> pendingInternships;
    private Map<String, InternshipApplication> withdrawalRequests;

    public ApprovalManager() {
        pendingReps = new HashMap<>();
        pendingInternships = new HashMap<>();
        withdrawalRequests = new HashMap<>();
    }

    // ========================= COMPANY REP APPROVAL =========================

    /**
     * Adds a new company representative request awaiting staff approval.
     */
    public void addCompanyRepRequest(CompanyRepresentative rep) {
        pendingReps.put(rep.getUserID(), rep);
        System.out.println("Company Representative request added for " + rep.getName());
    }

    /**
     * Approves a company representative registration.
     */
    public boolean approveCompanyRep(String repId) {
        CompanyRepresentative rep = pendingReps.get(repId);
        if (rep != null) {
            rep.setApproved(true);
            pendingReps.remove(repId);
            System.out.println("Company Representative " + rep.getName() + " approved.");
            return true;
        }
        System.out.println("No pending request found for representative ID: " + repId);
        return false;
    }

    /**
     * Rejects a company representative registration.
     */
    public boolean rejectCompanyRep(String repId) {
        CompanyRepresentative rep = pendingReps.remove(repId);
        if (rep != null) {
            rep.setApproved(false);
            System.out.println("Company Representative " + rep.getName() + " rejected.");
            return true;
        }
        System.out.println("No pending request found for representative ID: " + repId);
        return false;
    }

    // ========================= INTERNSHIP APPROVAL =========================

    /**
     * Adds a new internship posting awaiting staff approval.
     */
    public void addPendingInternship(Internship internship) {
        pendingInternships.put(internship.getInternshipID(), internship);
        System.out.println("Internship \"" + internship.getTitle() + "\" added to pending list.");
    }

    /**
     * Approves an internship posting.
     */
    public boolean approveInternship(String internshipId) {
        Internship i = pendingInternships.get(internshipId);
        if (i != null && i.getStatus() == Internship.InternshipStatus.PENDING) {
            i.setStatus(Internship.InternshipStatus.APPROVED);
            pendingInternships.remove(internshipId);
            System.out.println("Internship \"" + i.getTitle() + "\" approved.");
            return true;
        }
        System.out.println("Internship not found or already processed.");
        return false;
    }

    /**
     * Rejects an internship posting.
     */
    public boolean rejectInternship(String internshipId) {
        Internship i = pendingInternships.remove(internshipId);
        if (i != null) {
            i.setStatus(Internship.InternshipStatus.REJECTED);
            System.out.println("Internship \"" + i.getTitle() + "\" rejected.");
            return true;
        }
        System.out.println("Internship not found or already processed.");
        return false;
    }

    // ========================= WITHDRAWAL REQUESTS =========================

    /**
     * Adds a new student withdrawal request awaiting staff approval.
     */
    public void addWithdrawalRequest(InternshipApplication app) {
        withdrawalRequests.put(app.getApplicationID(), app);
        System.out.println("Withdrawal request added for student: " + app.getStudent().getName());
    }

    /**
     * Approves a student withdrawal request.
     */
    public boolean approveWithdrawal(String appId) {
        InternshipApplication app = withdrawalRequests.remove(appId);
        if (app != null) {
            app.setStatus(InternshipApplication.ApplicationStatus.WITHDRAWN);
            System.out.println("Withdrawal approved for student: " + app.getStudent().getName());
            return true;
        }
        System.out.println("Withdrawal request not found.");
        return false;
    }

    /**
     * Rejects a student withdrawal request.
     */
    public boolean rejectWithdrawal(String appId) {
        InternshipApplication app = withdrawalRequests.remove(appId);
        if (app != null) {
            app.setStatus(InternshipApplication.ApplicationStatus.ACTIVE);
            System.out.println("Withdrawal rejected for student: " + app.getStudent().getName());
            return true;
        }
        System.out.println("Withdrawal request not found.");
        return false;
    }

    // ========================= UTILITY METHODS =========================

    /**
     * Displays all pending requests (for staff review).
     */
    public void viewPendingApprovals() {
        System.out.println("\n===== Pending Company Representatives =====");
        pendingReps.values().forEach(r -> System.out.println(r.getUserID() + " - " + r.getName()));

        System.out.println("\n===== Pending Internships =====");
        pendingInternships.values().forEach(i -> System.out.println(i.getInternshipID() + " - " + i.getTitle()));

        System.out.println("\n===== Pending Withdrawal Requests =====");
        withdrawalRequests.values().forEach(a -> System.out.println(a.getApplicationID() + " - " + a.getStudent().getName()));
    }
}
