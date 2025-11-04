package boundary;

import controller.*;
import entity.*;
import utility.InputService;

import java.util.Collection;
import java.util.List;

public class StaffView {
    // menu and options for staff after logging in
	private String filterSetting; //maybe enum better
	private final ApplicationManager applicationManager;
	private final ApprovalManager approvalManager;
	private final InternshipManager internshipManager;
	private final AuthManager authManager;
	private Staff s;

	public StaffView(ApplicationManager applicationManager, ApprovalManager approvalManager,
			AuthManager authManager, InternshipManager internshipManager, Staff st) {
		this.applicationManager = applicationManager;
		this.approvalManager = approvalManager;
		this.internshipManager = internshipManager;
		this.authManager = authManager;
		this.s = st;
	}
	
	public void Menu() {
		int choice;
		do {
			System.out.println("\n ------ Staff Menu ------");
			System.out.println("Currently logged in as: " + s.getName());
			System.out.println("1: View Company Representative Account Requests");
			System.out.println("2: View Company Representative Internship Requests");
			System.out.println("3: View Student Withdrawal Requests");
			System.out.println("4: View Existing Internships");
			System.out.println("5: Change Password");
			System.out.println("6: Logout");
			System.out.println("Enter choice: ");
			choice = InputService.readInt();
			switch (choice) {
			case 1 -> ViewAccountReqs();
			case 2 -> ViewInternshipRequests();
			case 3 -> ViewWithdrawalReqs();
			case 4 -> ViewExistingInternships();
			case 5 -> ChangePassword();
			case 6 -> System.out.println("Logging out ...");
			default -> System.out.println("Invalid choice. Please try again. ");
			}
		} while (choice != 6);
	}
	
	public void ViewAccountReqs() {
		List<CompanyRepresentative> pendingReps = approvalManager.getPendingRepresentativeApprovals();
		if (pendingReps.isEmpty()) {
			System.out.println("No Accounts Pending For Approval.");
			return;
		}
		else {
			System.out.println("\n--- Pending Company Representative Accounts ---");
			for (int i = 0; i < pendingReps.size(); i++) {
            	CompanyRepresentative rep = pendingReps.get(i);
            	System.out.printf("%d. %-15s | %-20s | %s\n",
                    (i + 1), rep.getName(), rep.getCompanyName(), rep.getEmail());
        	}
        	System.out.println("-------------------------------------------------");
			ApproveCompany(pendingReps);
		}
	}
	
	public void ViewInternshipRequests() {
		List<Internship> pendingInternships = approvalManager.getPendingInternshipApprovals();
		if (pendingInternships.isEmpty()) {
			System.out.println("No Internships Pending For Approval.");
			return;
		} else {
			System.out.println("\n--- Pending Internship Postings ---");
        	for (int i = 0; i < pendingInternships.size(); i++) {
            	Internship intern = pendingInternships.get(i);
            	System.out.printf("%d. %-25s | %-15s | Rep: %s\n",
                    (i + 1), intern.getTitle(), intern.getCompanyName(), intern.getCompanyRepID());
        	}
        	System.out.println("-------------------------------------------------");
        	ApproveIntern(pendingInternships);
		}
	}
	
	public void ViewWithdrawalReqs() {
		// ask to select which student to act on
		// approval/rejection
		// call InternshipManager method to approve/reject
		// call back to ViewWithdrawal for updated list
	}
	
	public void ViewExistingInternships() {
		Collection<Internship> allInternships = internshipManager.getAllInternships();
		if (allInternships.isEmpty()) {
			System.out.println("No Internships To View.");
			return;
		} else {
			System.out.println("\n--- All Internships ---");
			for (Internship intern : allInternships) {
				System.out.printf("%-25s | %-15s | Status: %s | Visible: %s\n",
					intern.getTitle(), intern.getCompanyName(), intern.getStatus(), intern.isVisible());
				// ChangeFilter();
			}
		}
		// display according to filterSetting
		// ask to apply different filter settings or back to staff view
	}
	
	public void ApproveCompany(List<CompanyRepresentative> pendingReps) {
		System.out.println("Select a Company Representative to approve or reject. (1-" + pendingReps.size() + ")");
		System.out.println("Enter '0' to go back to menu.");
		int choice = InputService.readInt();
		while (choice < 0 || choice > pendingReps.size()) {
			System.out.println("Invalid input. Please retry. ");
			choice = InputService.readInt();
		}
		
		if (choice == 0) {
			System.out.println("Back to menu ...");
		} else {
			CompanyRepresentative rep = pendingReps.get(choice-1);
			String repID = rep.getUserID();
			System.out.printf("%s (%s): \n1. Approve \n2. Reject", rep.getName(), rep.getCompanyName());
			int action = InputService.readInt();
			if (action == 1) {
				approvalManager.approveRepresentative(repID, true);
				System.out.printf("** Successfully approved %s! **", rep.getName());
			}
			else if (action == 2) {
				approvalManager.approveRepresentative(repID, false);
				System.out.printf("** Successfully rejected %s! **", rep.getName());
			} else { System.out.println("Invalid action. Returning to menu..."); }	
		}		
	}
	
	public void ApproveIntern(List<Internship> pendingInternships) {
		System.out.println("Select an Internship to approve or reject (1-" + pendingInternships.size() + ")");
		System.out.println("Enter '0' to go back to menu.");
		int choice = InputService.readInt();
		while (choice < 0 || choice > pendingInternships.size()) {
			System.out.println("Invalid input. Please retry. ");
			choice = InputService.readInt();
		}
		if (choice == 0) {
			System.out.println("Back to menu ...");
			return;
		}
		Internship intern = pendingInternships.get(choice-1);
		String internID = intern.getInternshipID();
		System.out.printf("%s: \n1. Approve \n2. Reject", intern.getTitle());
		int action = InputService.readInt();
		if (action == 1) {
			approvalManager.approveInternship(internID, true);
			System.out.printf("** Successfully approved %s! **", intern.getTitle());
		} else if (action == 2) {
			approvalManager.approveInternship(internID, false);
			System.out.printf("** Successfully rejected %s! **", intern.getTitle());
		} else { System.out.println("Invalid choice. Returning to menu..."); }
	}
	
	public void ChangeFilter() {
		System.out.println("Select a filter.");
		System.out.println("Otherwise, enter '0' to go back to menu.");
		System.out.println("1. Status \n2. Preferred Majors \n3. Internship Level");
		int choice = InputService.readInt();
		while (choice < 0 && choice > 3) {
			System.out.println("Invalid input");
			choice = InputService.readInt();
		}
		
		if (choice == 0) {
			System.out.println("Back to menu ...");
		}
		else {
			// not sure how to compound filters together
			
		}
		
		Menu();
	}
	
	private void ChangePassword() {
        System.out.println("\n--- Change Password ---");
        System.out.print("Please enter your old password: ");
        String old = InputService.readString();
        System.out.print("Please enter your new password: ");
        String newp = InputService.readString();        
        boolean success = authManager.changePassword(s, old, newp);
        
        if (success) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password. Old password may be incorrect.");
        }
    }	
}
