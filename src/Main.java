import boundary.MainMenu;
import utility.InputService;
import controller.*;
import entity.CompanyRepresentative;
import entity.Internship;
import entity.InternshipApplication;
import entity.Staff;
import entity.Student;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
    	Map<String, InternshipApplication> allApplications = new HashMap<>();
        Map<String, Internship> allInternships = new HashMap<>();
        Map<String, Student> allStudents = new HashMap<>();
        Map<String, CompanyRepresentative> allCompanyReps = new HashMap<>();
        Map<String, Staff> allStaffs = new HashMap<>();
    	
    	ApplicationManager applicationManager = new ApplicationManager(allApplications, allInternships, allStudents);
    	ApprovalManager approvalManager = new ApprovalManager(allCompanyReps, allInternships);
    	AuthManager authManager = new AuthManager();
    	InternshipManager internshipManager = new InternshipManager();
    	
    	System.out.println("---Internship Management System---");
    	
    	MainMenu mainMenu = new MainMenu(applicationManager, approvalManager, authManager, internshipManager);
    	mainMenu.Start();
    	
    	InputService.closeScanner();
    }
}
