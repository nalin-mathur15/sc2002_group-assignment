package boundary;

import utility.InputService;
import controller.*;
import entity.*;
import java.util.List;
import java.util.Map;

public class MainMenu {
	// initial command-line interface
	private ApplicationManager applicationManager;
	private ApprovalManager approvalManager;
	private AuthManager authManager;
	private InternshipManager internshipManager;
	private Map<String, CompanyRepresentative> allCompanyReps;
    private Map<String, Student> allStudents;
    private Map<String, Staff> allStaffs;
	
	public MainMenu(ApplicationManager applicationManager, ApprovalManager approvalManager, 
			AuthManager authManager, InternshipManager internshipManager) {
		this.applicationManager = applicationManager;
		this.approvalManager = approvalManager;
		this.authManager = authManager;
		this.internshipManager = internshipManager;
	}
	
	public void Start() {
		int choice;
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: Login");
			System.out.println("2: Register as Company Representative");
			choice = InputService.readInt();
		
			switch (choice) {
				case 1:
					// call login, which returns student
					Login login = new Login(authManager);
					List<String> loginDetails = login.Start();
					if (loginDetails != null) {
						switch (loginDetails.get(0)) {
							case "Student":
								Student s = allStudents.get(loginDetails.get(1));
								StudentView studentView = new StudentView(applicationManager, authManager, internshipManager, s);
								studentView.Menu();
								break;
							case "Staff":
								Staff staff = allStaffs.get(loginDetails.get(1));
								StaffView staffView = new StaffView(applicationManager, approvalManager, authManager, internshipManager, staff);
								staffView.Menu();
								break;
							case "CompanyRepresentative":
								CompanyRepresentative compRep = allCompanyReps.get(loginDetails.get(1));
								CompanyView companyView = new CompanyView(applicationManager, authManager, internshipManager, compRep);
								companyView.Menu();
								break;
						}
					}
					break;
				case 2:
					// call register
					break;
				case 3: 
					System.out.println("Program terminating ….");
				}
		} while (choice < 3 && choice > 0);
	}
}
