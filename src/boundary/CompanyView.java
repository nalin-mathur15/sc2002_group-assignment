package boundary;

import utility.InputService;

public class CompanyView {
    // menu and options for company rep after logging in
	public CompanyView() {
		int choice;
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: Create Internship (max 5)");
			System.out.println("2: View Internships Created");
			System.out.println("3: Logout");
			choice = InputService.readInt();
		
			switch (choice) {
				case 1:
					CreateInternship();
					break;
				case 2:
					ViewInternship();
					break;
				case 3: 
					System.out.println("Logging out ...");
				}
		} while (choice < 3 && choice > 0);
	}
	
	public void CreateInternship() {
		// collect inputs of internship
		// call approval manager method to store into list of internships to be approved
	}
	
	public void ViewInternship() {
		// call internship manager for list of internships to be displayed
		// if null list, no action
		// if list exists, display and call PerformAction()
	}
	
	public void PerformAction() {
		// ask to select which internship to act on
		// approval/rejection
		// select student
		// call InternshipManager method to approve/reject
		// call back to ViewInternship for updated list
	}
}
