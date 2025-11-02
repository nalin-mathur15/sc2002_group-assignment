package boundary;

import controller.*;
import entity.*;
import utility.InputService;

import java.util.Collection;
import java.util.List;

public class StaffView {
    // menu and options for staff after logging in
	private String filterSetting; //maybe enum better
	private ApplicationManager applicationManager;
	private ApprovalManager approvalManager;
	private InternshipManager internshipManager;
	private AuthManager authManager;
	private Staff s;
	public StaffView(ApplicationManager applicationManager, ApprovalManager approvalManager,
			AuthManager authManager, InternshipManager internshipManager, Staff s) {
		this.applicationManager = applicationManager;
		this.internshipManager = internshipManager;
		this.authManager = authManager;
		this.s = s;
	}
	
	public void Menu() {
		int choice;
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: View Account Requests from Company Representatives");
			System.out.println("2: View Internship Requests from Company Representatives");
			System.out.println("3: View Student Withdrawal Requests");
			System.out.println("4: View Existing Internships");
			System.out.println("5: Change Password");
			System.out.println("6: Logout");
			choice = InputService.readInt();
			switch (choice) {
			case 1 -> ViewAccount();
			case 2 -> ViewInternshipRequests();
			case 3 -> ViewWithdrawal();
			case 4 -> ViewExistingInternships();
			case 5 -> ChangePassword();
			default -> System.out.println("Logging out ...");
			}
		} while (choice < 6 && choice > 0);
	}
	
	public void ViewAccount() {
		List<CompanyRepresentative> listCompRep = approvalManager.getPendingRepresentativeApprovals();
		if (listCompRep.isEmpty()) {
			System.out.println("No Accounts Pending For Approval.");
			Menu();
		}
		else {
			for (CompanyRepresentative compRep : listCompRep) {
				System.out.println(compRep.getCompanyName()); // not sure how to display this
			}
			ApproveCompany(listCompRep);
		}
	}
	
	public void ViewInternshipRequests() {
		List<Internship> listIntern = approvalManager.getPendingInternshipApprovals();
		if (listIntern.isEmpty()) {
			System.out.println("No Internships Pending For Approval.");
			Menu();
		}
		else {
			for (Internship intern : listIntern) {
				System.out.println(intern.getTitle()); // not sure how to display this
				ApproveIntern(listIntern);
			}
		}
	}
	
	public void ViewWithdrawal() {
		// ask to select which student to act on
		// approval/rejection
		// call InternshipManager method to approve/reject
		// call back to ViewWithdrawal for updated list
	}
	
	public void ViewExistingInternships() {
		Collection<Internship> colIntern = internshipManager.getAllInternships();
		if (colIntern.isEmpty()) {
			System.out.println("No Internships To View.");
			Menu();
		}
		else {
			for (Internship intern : colIntern) {
				System.out.println(intern.getTitle()); // not sure how to display this
				// ChangeFilter();
			}
		}
		// display according to filterSetting
		// ask to apply different filter settings or back to staff view
	}
	
	public void ApproveCompany(List<CompanyRepresentative> listCompRep) {
		System.out.println("Select a Company Representative to approve or reject.");
		System.out.println("Otherwise, enter '0' to go back to menu.");
		int choice = InputService.readInt();
		while (choice < 0 && choice > listCompRep.size()) {
			System.out.println("Invalid input");
			choice = InputService.readInt();
		}
		
		if (choice == 0) {
			System.out.println("Back to menu ...");
		}
		else {
			CompanyRepresentative accCompRep = listCompRep.get(choice-1);
			String accID = accCompRep.getUserID();
			System.out.printf("%s: \n1. Approve \n2. Reject", accCompRep.getCompanyName());
			choice = InputService.readInt();
			if (choice == 1) {
				approvalManager.approveRepresentative(accID, true);
				System.out.printf("** Successfully approved %s! **", accCompRep.getCompanyName());
			}
			else {
				approvalManager.approveRepresentative(accID, false);
				System.out.printf("** Successfully rejected %s! **", accCompRep.getCompanyName());
			}
			
		}
		
		ViewAccount();
	}
	
	public void ApproveIntern(List<Internship> listIntern) {
		System.out.println("Select an Internship to approve or reject.");
		System.out.println("Otherwise, enter '0' to go back to menu.");
		int choice = InputService.readInt();
		while (choice < 0 && choice > listIntern.size()) {
			System.out.println("Invalid input");
			choice = InputService.readInt();
		}
		
		if (choice == 0) {
			System.out.println("Back to menu ...");
		}
		else {
			Internship intern = listIntern.get(choice-1);
			String internID = intern.getInternshipID();
			System.out.printf("%s: \n1. Approve \n2. Reject", intern.getTitle());
			choice = InputService.readInt();
			if (choice == 1) {
				approvalManager.approveInternship(internID, true);
				System.out.printf("** Successfully approved %s! **", intern.getTitle());
			}
			else {
				approvalManager.approveInternship(internID, false);
				System.out.printf("** Successfully rejected %s! **", intern.getTitle());
			}
			
		}
		
		ViewInternshipRequests();
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
	
	public void ChangePassword() {
		System.out.println("Please enter old password: ");
		String oldPword = InputService.readString();
		System.out.println("Please enter new password: ");
		String newPword = InputService.readString();
		authManager.changePassword(s, oldPword, newPword);
		Menu();
	}
	
}
