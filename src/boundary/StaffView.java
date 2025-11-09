package boundary;

import controller.*;
import entity.*;
import entity.Internship.InternshipLevel;
import entity.Internship.InternshipStatus;
import entity.InternshipApplication.ApplicationStatus;
import utility.InputService;
import entity.InternshipApplication.ApplicationStatus;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StaffView implements ChangePasswordInt, LogoutInt {
    // menu and options for staff after logging in
	private String filterSetting; //maybe enum better
	private final ApplicationManager applicationManager;
	private final ApprovalManager approvalManager;
	private final InternshipManager internshipManager;
	private final AuthManager authManager;
	
    // filter fields
    private InternshipStatus internshipFilterStatus = null;
    private String internshipFilterMajor = null;
    private String internshipFilterCompany = null;
    private InternshipLevel internshipFilterLevel = null;
    private Integer studentFilterYear = null;
    private String studentFilterMajor = null;
    private Boolean repFilterApproved = null;
    private String repFilterCompany = null;
    private ApplicationStatus appFilterStatus = null;

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
			System.out.println("2: View Internship Posting Requests");
			System.out.println("3: View Student Withdrawal Requests");
			System.out.println("4: View and Filter Internships");
            System.out.println("5: View and Filter Students");
            System.out.println("6: View and Filter Company Representatives");
            System.out.println("7: View and Filter Internship Applications");
            System.out.println("8: Change Password");
			System.out.println("9: Logout");
			System.out.println("Enter choice: ");
			choice = InputService.readInt();
			switch (choice) {
			case 1 -> ViewAccountReqs();
			case 2 -> ViewInternshipRequests();
			case 3 -> ViewWithdrawalReqs();
			case 4 -> viewAndFilterInternships();
			case 5 -> viewAndFilterStudents();
            case 6 -> viewAndFilterCompanyReps();
            case 7 -> viewAndFilterApplications();
            case 8 -> ChangePassword();
			case 9 -> Logout();
			default -> System.out.println("Invalid choice. Please try again. ");
			}
		} while (choice != 9);
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

<<<<<<< HEAD
	public void ViewWithdrawalReqs() {
		List<InternshipApplication> pendingWithdrawals = applicationManager.getPendingWithdrawals();
		if (pendingWithdrawals.isEmpty()) {
			System.out.println("No Withdrawal Requests To View.");
			return;
		} else {
			System.out.println("\n--- Pending Withdrawal Requests ---");
			for (int i = 0; i < pendingWithdrawals.size(); i++) {
				InternshipApplication app = pendingWithdrawals.get(i);
				Internship intern = internshipManager.getInternshipById(app.getInternshipID());
				System.out.printf("%d. Student ID: %-10s | Internship: %-25s | Company: %-15s\n",
					(i + 1), app.getStudentID(), intern.getTitle(), intern.getCompanyName());
			}
			System.out.println("-------------------------------------------------");
		}
		ApproveWithdrawal(pendingWithdrawals);
	}

	public void ApproveWithdrawal(List<InternshipApplication> pendingWithdrawals) {
		System.out.println("Select a Withdrawal Request to approve or reject (1-" + pendingWithdrawals.size() + ")");
		System.out.println("Enter '0' to go back to menu.");
		int choice = InputService.readInt();
		while (choice < 0 || choice > pendingWithdrawals.size()) {
			System.out.println("Invalid input. Please retry. ");
			choice = InputService.readInt();
		}
		if (choice == 0) {
			System.out.println("Back to menu ...");
			return;
		}
		InternshipApplication app = pendingWithdrawals.get(choice-1);
		Internship intern = internshipManager.getInternshipById(app.getInternshipID());
		System.out.printf("Withdrawal Request for StudentID (%s) on Internship (%s): \n0. Go back \n1. Approve \n2. Reject", app.getStudentID(), intern.getTitle());
		int action = InputService.readInt();
		while (choice < 0 || choice > 2) {
			System.out.println("Invalid input. Please retry. ");
			action = InputService.readInt();
		}
		if (action == 0) {
			System.out.println("Back to menu ...");
		}
		else if (action == 1) {
			applicationManager.updateApplicationStatus(app.getApplicationID(), ApplicationStatus.WITHDRAWN);
			System.out.printf("** Successfully approved withdrawal for for StudentID (%s) on Internship (%s)! **", app.getStudentID(), intern.getTitle());
		} else {
			applicationManager.updateApplicationStatus(app.getApplicationID(), ApplicationStatus.SUCCESSFUL);
			System.out.printf("** Successfully rejected withdrawal for for StudentID (%s) on Internship (%s)! **", app.getStudentID(), intern.getTitle());
		}
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
	}
=======
	private void displayApplications(Collection<InternshipApplication> applications) {
         if (applications.isEmpty()) {
            System.out.println("No applications found matching this filter.");
            return;
        }
        int i = 1;
        System.out.println("-------------------------------------------------------------------");
        System.out.printf("%-3s | %-15s | %-15s | %-15s\n", "#", "Application ID", "Student ID", "Status");
        System.out.println("-------------------------------------------------------------------");
        for (InternshipApplication a : applications) {
            System.out.printf("%-3d | %-15s | %-15s | %-15s\n",
                    i++,
                    a.getApplicationID(),
                    a.getStudentID(),
                    a.getStatus());
        }
        System.out.println("-------------------------------------------------------------------");
    }

	private void displayCompanyReps(Collection<CompanyRepresentative> reps) {
         if (reps.isEmpty()) {
            System.out.println("No representatives found matching this filter.");
            return;
        }
        int i = 1;
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.printf("%-3s | %-20s | %-20s | %-10s | %-25s\n", "#", "Name", "Company", "Status", "Email (ID)");
        System.out.println("--------------------------------------------------------------------------------------");
        for (CompanyRepresentative r : reps) {
            System.out.printf("%-3d | %-20s | %-20s | %-10s | %-25s\n",
                    i++,
                    r.getName(),
                    r.getCompanyName(),
                    r.approvedRepresentative() ? "Approved" : "Pending",
                    r.getEmail());
        }
        System.out.println("--------------------------------------------------------------------------------------");
    }

	private void displayStudents(Collection<Student> students) {
         if (students.isEmpty()) {
            System.out.println("No students found matching this filter.");
            return;
        }
        int i = 1;
        System.out.println("-------------------------------------------------------------------");
        System.out.printf("%-3s | %-20s | %-10s | %-5s | %-25s\n", "#", "Name", "Major", "Year", "Email");
        System.out.println("-------------------------------------------------------------------");
        for (Student s : students) {
            System.out.printf("%-3d | %-20s | %-10s | %-5d | %-25s\n",
                    i++,
                    s.getName(),
                    s.getMajor(),
                    s.getYear(),
                    s.getEmail());
        }
        System.out.println("-------------------------------------------------------------------");
    }

	private void displayInternships(Collection<Internship> internships) {
        if (internships.isEmpty()) {
            System.out.println("No internships found matching this filter.");
            return;
        }
        int i = 1;
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.printf("%-3s | %-25s | %-20s | %-12s | %-12s | %-10s\n", 
            "#", "Title", "Company", "Status", "Level", "Major");
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (Internship intern : internships) {
            System.out.printf("%-3d | %-25s | %-20s | %-12s | %-12s | %-10s\n",
                    i++,
                    intern.getTitle(),
                    intern.getCompanyName(),
                    intern.getStatus(),
                    intern.getLevel(),
                    intern.getPreferredMajor());
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

	private void viewAndFilterInternships() {
        int choice;
        do {
            // current filters
            Collection<Internship> allInternships = internshipManager.getAllInternships();
            Collection<Internship> filteredList = allInternships.stream()
                    .filter(i -> internshipFilterStatus == null || i.getStatus() == internshipFilterStatus)
                    .filter(i -> internshipFilterMajor == null || i.getPreferredMajor().equalsIgnoreCase(internshipFilterMajor))
                    .filter(i -> internshipFilterCompany == null || i.getCompanyName().equalsIgnoreCase(internshipFilterCompany))
                    .filter(i -> internshipFilterLevel == null || i.getLevel() == internshipFilterLevel)
                    .collect(Collectors.toList());

            // current state
            System.out.println("\n--- Filter Internships ---");
            System.out.println("Current Filters:");
            System.out.println("  Status:  " + (internshipFilterStatus == null ? "Any" : internshipFilterStatus));
            System.out.println("  Major:   " + (internshipFilterMajor == null ? "Any" : internshipFilterMajor));
            System.out.println("  Company: " + (internshipFilterCompany == null ? "Any" : internshipFilterCompany));
            System.out.println("  Level:   " + (internshipFilterLevel == null ? "Any" : internshipFilterLevel));
            
            System.out.println("\n--- Internship Results (" + filteredList.size() + " found) ---");
            displayInternships(filteredList);

            // menu to change filters
            System.out.println("\n--- Set/Change Filter ---");
            System.out.println("1. Set Status");
            System.out.println("2. Set Preferred Major");
            System.out.println("3. Set Company Name");
            System.out.println("4. Set Level");
            System.out.println("5. Clear All Internship Filters");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = InputService.readIntRange(0, 5);

            // update state
            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Status (PENDING, APPROVED, FILLED, REJECTED) or 'clear': ");
                        String s = InputService.readString().toUpperCase();
                        internshipFilterStatus = s.equals("CLEAR") ? null : InternshipStatus.valueOf(s);
                    }
                    case 2 -> {
                        System.out.print("Enter Preferred Major or 'clear': ");
                        String s = InputService.readString();
                        internshipFilterMajor = (s.equalsIgnoreCase("clear") || s.isEmpty()) ? null : s;
                    }
                    case 3 -> {
                        System.out.print("Enter Company Name or 'clear': ");
                        String s = InputService.readString();
                        internshipFilterCompany = (s.equalsIgnoreCase("clear") || s.isEmpty()) ? null : s;
                    }
                    case 4 -> {
                        System.out.print("Enter Level (BASIC, INTERMEDIATE, ADVANCED) or 'clear': ");
                        String s = InputService.readString().toUpperCase();
                        internshipFilterLevel = s.equals("CLEAR") ? null : InternshipLevel.valueOf(s);
                    }
                    case 5 -> {
                        internshipFilterStatus = null;
                        internshipFilterMajor = null;
                        internshipFilterCompany = null;
                        internshipFilterLevel = null;
                        System.out.println("All internship filters cleared.");
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid filter value. Try again.");
            }
        } while (choice != 0);
    }

    private void viewAndFilterStudents() {
        int choice;
        do {
            Collection<Student> allStudents = applicationManager.getAllStudents();
            Collection<Student> filteredList = allStudents.stream()
                    .filter(s -> studentFilterYear == null || s.getYear() == studentFilterYear)
                    .filter(s -> studentFilterMajor == null || s.getMajor().equalsIgnoreCase(studentFilterMajor))
                    .collect(Collectors.toList());
            
            System.out.println("\n--- Filter Students ---");
            System.out.println("Current Filters:");
            System.out.println("  Year:  " + (studentFilterYear == null ? "Any" : studentFilterYear));
            System.out.println("  Major: " + (studentFilterMajor == null ? "Any" : studentFilterMajor));

            System.out.println("\n--- Student Results (" + filteredList.size() + " found) ---");
            displayStudents(filteredList);

            System.out.println("\n--- Set/Change Filter ---");
            System.out.println("1. Set Year of Study");
            System.out.println("2. Set Major");
            System.out.println("3. Clear All Student Filters");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = InputService.readIntRange(0, 3);

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Year of Study (1-4) or 0 to clear: ");
                    int year = InputService.readIntRange(0, 4);
                    studentFilterYear = (year == 0) ? null : year;
                }
                case 2 -> {
                    System.out.print("Enter Major or 'clear': ");
                    String s = InputService.readString();
                    studentFilterMajor = (s.equalsIgnoreCase("clear") || s.isEmpty()) ? null : s;
                }
                case 3 -> {
                    studentFilterYear = null;
                    studentFilterMajor = null;
                    System.out.println("All student filters cleared.");
                }
            }
        } while (choice != 0);
    }

    private void viewAndFilterCompanyReps() {
        int choice;
        do {
            Collection<CompanyRepresentative> allReps = approvalManager.getAllRepresentatives(); // Assumes this exists
            Collection<CompanyRepresentative> filteredList = allReps.stream()
                .filter(r -> repFilterApproved == null || r.approvedRepresentative() == repFilterApproved)
                .filter(r -> repFilterCompany == null || r.getCompanyName().equalsIgnoreCase(repFilterCompany))
                .collect(Collectors.toList());

            System.out.println("\n--- Filter Company Representatives ---");
            System.out.println("Current Filters:");
            String approvedStatus = (repFilterApproved == null) ? "Any" : (repFilterApproved ? "Approved" : "Pending");
            System.out.println("  Status:  " + approvedStatus);
            System.out.println("  Company: " + (repFilterCompany == null ? "Any" : repFilterCompany));

            System.out.println("\n--- Company Representative Results (" + filteredList.size() + " found) ---");
            displayCompanyReps(filteredList);

            System.out.println("\n--- Set/Change Filter ---");
            System.out.println("1. Set Approval Status");
            System.out.println("2. Set Company Name");
            System.out.println("3. Clear All Rep Filters");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = InputService.readIntRange(0, 3);

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Status (1. Approved, 2. Pending, 0. Any): ");
                    int statusChoice = InputService.readIntRange(0, 2);
                    repFilterApproved = (statusChoice == 0) ? null : (statusChoice == 1);
                }
                case 2 -> {
                    System.out.print("Enter Company Name or 'clear': ");
                    String s = InputService.readString();
                    repFilterCompany = (s.equalsIgnoreCase("clear") || s.isEmpty()) ? null : s;
                }
                case 3 -> {
                    repFilterApproved = null;
                    repFilterCompany = null;
                    System.out.println("All representative filters cleared.");
                }
            }
        } while (choice != 0);
    }

    private void viewAndFilterApplications() {
        int choice;
        do {
            Collection<InternshipApplication> allApps = applicationManager.getAllApplications(); // Assumes this exists
            Collection<InternshipApplication> filteredList = allApps.stream()
                .filter(a -> appFilterStatus == null || a.getStatus() == appFilterStatus)
                .collect(Collectors.toList());
            System.out.println("\n--- Filter Applications ---");
            System.out.println("Current Filters:");
            System.out.println("  Status: " + (appFilterStatus == null ? "Any" : appFilterStatus));

            System.out.println("\n--- Application Results (" + filteredList.size() + " found) ---");
            displayApplications(filteredList);

            System.out.println("\n--- Set/Change Filter ---");
            System.out.println("1. Set Status");
            System.out.println("2. Clear All Application Filters");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = InputService.readIntRange(0, 2);

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Status (PENDING, SUCCESSFUL, UNSUCCESSFUL) or 'clear': ");
                        String s = InputService.readString().toUpperCase();
                        appFilterStatus = s.equals("CLEAR") ? null : ApplicationStatus.valueOf(s);
                    }
                    case 2 -> {
                        appFilterStatus = null;
                        System.out.println("Application filter cleared.");
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid filter value. Try again.");
            }
        } while (choice != 0);
    }
>>>>>>> origin/main
	
	public void ChangePassword() {
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

	public void Logout() {
        System.out.println("Logging out ...");
		authManager.logout(s);
	}	
}
