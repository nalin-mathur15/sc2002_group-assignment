package boundary;

import utility.InputService;

public class StaffView {
    // menu and options for staff after logging in
	private String filterSettings; //maybe enum better
	public StaffView() {
		int choice;
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: View Account Requests from Company Representatives");
			System.out.println("2: View Internship Requests from Company Representatives");
			System.out.println("3: View Student Withdrawal Requests");
			System.out.println("4: View Existing Internships");
			System.out.println("5: Logout");
			choice = InputService.readInt();
		
			switch (choice) {
				case 1:
					ViewAccount();
					break;
				case 2:
					ViewInternshipRequests();
					break;
				case 3:
					ViewWithdrawal();
					break;
				case 4:
					ViewExistingInternships();
					break;
				case 5: 
					System.out.println("Logging out ...");
				}
		} while (choice < 5 && choice > 0);
	}
	
	public void ViewAccount() {
		// call approval manager for list of account requests to be approved/rejected
		// if null list, no action
		// if list exists, display and call PerformAction()
	}
	
	public void ViewInternshipRequests() {
		// call internship manager for list of internships to be displayed
		// if null list, no action
		// if list exists, display and call PerformAction()
	}
	
	public void ViewWithdrawal() {
		// ask to select which internship to act on
		// approval/rejection
		// select student
		// call InternshipManager method to approve/reject
		// call back to ViewWithdrawal for updated list
	}
	
	public void ViewExistingInternships() {
		// call internship manager for existing list of internships to be displayed
		// display according to filterSetting
		// ask to apply different filter settings or back to staff view
	}
	
	public void PerformAction() {
		// ask to select which account/internship to act on
		// approval/rejection
		// call respective manager method to approve/reject
		// call back to View method for updated list
	}
}
