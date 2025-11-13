import boundary.MainMenu;
import utility.DataHandler;
import utility.InputService;
import controller.*;
import entity.*;
//import entity.Internship.InternshipStatus;

import java.util.Map;
import java.util.HashMap;

public class Main {
	private static final String STUDENT_FILE = "internship_management\\src\\data\\students.csv";
    private static final String STAFF_FILE = "internship_management\\src\\data\\staff.csv";
    private static final String REP_FILE = "internship_management\\src\\data\\companyReps.csv";
    private static final String INTERNSHIP_FILE = "internship_management\\src\\data\\internships.csv";
    private static final String APPLICATION_FILE = "internship_management\\src\\data\\applications.csv";

    public static void main(String[] args) {
		System.out.println("[Main] Loading data from files...");
		
		// load data
		Map<String, Student> allStudents = DataHandler.loadStudents(STUDENT_FILE);
		Map<String, Staff> allStaff = DataHandler.loadStaff(STAFF_FILE);
		Map<String, Internship> allInternships = DataHandler.loadInternships(INTERNSHIP_FILE);
        Map<String, InternshipApplication> allApplications = DataHandler.loadApplications(APPLICATION_FILE);
        Map<String, CompanyRepresentative> allCompanyReps = DataHandler.loadCompanyReps(REP_FILE);
        
		Map<String, User> allUsers = new HashMap<>();
		allUsers.putAll(allStudents);
		allUsers.putAll(allStaff);
		allUsers.putAll(allCompanyReps);

		// instantiate managers
    	ApplicationManager applicationManager = new ApplicationManager(allApplications, allInternships, allStudents);
    	ApprovalManager approvalManager = new ApprovalManager(allCompanyReps, allInternships);
    	AuthManager authManager = new AuthManager(allUsers, allCompanyReps);
    	InternshipManager internshipManager = new InternshipManager(allInternships);
    	
    	System.out.println("---Internship Management System---");
    	// main menu
    	MainMenu mainMenu = new MainMenu(applicationManager, approvalManager, authManager, internshipManager);
    	mainMenu.start();
    	
		// save when closing
		System.out.println("[Main] Saving data to files...");
        DataHandler.saveStudents(STUDENT_FILE, allStudents);
        DataHandler.saveStaff(STAFF_FILE, allStaff);
        DataHandler.saveCompanyReps(REP_FILE, allCompanyReps);
        DataHandler.saveInternships(INTERNSHIP_FILE, allInternships);
        DataHandler.saveApplications(APPLICATION_FILE, allApplications);

    	InputService.closeScanner();
		System.out.println("[Main] System Shut down. ");
    }
}
