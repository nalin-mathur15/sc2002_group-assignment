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
			AuthManager authManager, InternshipManager internshipManager, Map<String, CompanyRepresentative> allCompanyReps,
			Map<String, Student> allStudents, Map<String, Staff> allStaffs) {
		this.applicationManager = applicationManager;
		this.approvalManager = approvalManager;
		this.authManager = authManager;
		this.internshipManager = internshipManager;
		this.allCompanyReps = allCompanyReps;
		this.allStudents = allStudents;
		this.allStaffs = allStaffs;
	}
	
	public void Start() {
		int choice;
		do {
			System.out.println("1: Login");
			System.out.println("2: Register as Company Representative");
			System.out.println("3: Terminate Program");
			choice = InputService.readInt();
		
			switch (choice) {
				case 1:
					// call login, returns loginDetails(role, userID)
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
								if (compRep.approvedRepresentative()) {
									CompanyView companyView = new CompanyView(applicationManager, authManager, internshipManager, compRep);
									companyView.Menu();
								}
								else {
									System.out.println("Your Company Representative account is still pending approval by Staff. Please wait for approval before logging in.");
								}
								break;
						}
					}
					break;
				case 2:
					Register register = new Register(allCompanyReps);
					register.Start();
					break;
				case 3: 
					System.out.println("Program terminating ...");
					break;
				default:
					System.out.println("Invalid choice. Please try again. ");
					break;
			}
		} while (choice != 3);
	}
}
