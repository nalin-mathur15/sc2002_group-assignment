import boundary.MainMenu;
import utility.DataHandler;
import utility.InputService;
import controller.*;
import entity.*;
import java.util.Map;
import java.util.HashMap;

/**
 * Main entry point for the Internship Placement Management System.
 * This class handles the high-level application lifecycle:
 * 1. Loading all data from CSV files.
 * 2. Initializing all controller (manager) classes.
 * 3. Starting the main menu (boundary).
 * 4. Saving all data back to CSV files on exit.
 */
public class Main {
	/** File path for the student data CSV. */
	private static final String STUDENT_FILE = "src\\data\\students.csv";
	/** File path for the staff data CSV. */
	private static final String STAFF_FILE = "src\\data\\staff.csv";
	/** File path for the company representative data CSV. */
	private static final String REP_FILE = "src\\data\\companyReps.csv";
	/** File path for the internship data CSV. */
	private static final String INTERNSHIP_FILE = "src\\data\\internships.csv";
	/** File path for the application data CSV. */
	private static final String APPLICATION_FILE = "src\\data\\applications.csv";

	/**
     * The main method to run the application.
     * @param args Command line arguments (not used).
     */
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
