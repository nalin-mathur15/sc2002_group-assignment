package boundary;

import utility.InputService;

public class StudentView {
    // menu and options for a student user after logging in
	public StudentView() {
		int choice;
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: View Internships Created/Applied");
			System.out.println("2: Logout");
			choice = InputService.readInt();
		
			switch (choice) {
				case 1:
					ViewInternship();
					break;
				case 2: 
					System.out.println("Logging out ...");
				}
		} while (choice < 3 && choice > 0);
	}
	
	public void ViewInternship() {
		// call internship manager for list of internships to be displayed and filter by student profile and if applied must display
		// if null list, no action
		// if list exists, display and call PerformAction()
	}
	
	public void PerformAction() {
		// ask to select which internship to apply for (max 3) or withdraw from
		// confirm apply/withdraw/accept or back to StudentView
		// call InternshipManager/ApprovalManager method to approve/reject
		// call back to ViewInternship for updated list
	}
}
