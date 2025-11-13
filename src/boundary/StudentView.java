package boundary;

import utility.InputService;
import controller.ApplicationManager;
import controller.InternshipManager;
import controller.AuthManager;
import entity.Student;
import entity.Internship;
import entity.Internship.InternshipLevel;
import entity.Internship.InternshipStatus;
import entity.InternshipApplication;
import entity.InternshipApplication.ApplicationStatus;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StudentView extends AbstractView {

    private final ApplicationManager applicationManager;
    private final InternshipManager internshipManager;
    private InternshipStatus internshipFilterStatus = null;
    private String internshipFilterMajor = null;
    private String internshipFilterCompany = null;
    private InternshipLevel internshipFilterLevel = null;
	private final Student loggedInStudent;

    public StudentView(ApplicationManager applicationManager, AuthManager authManager,
                       InternshipManager internshipManager, Student student) {
        super(authManager, student);
        this.applicationManager = applicationManager;
        this.internshipManager = internshipManager;
        this.loggedInStudent = student;
    }

    @Override
    public void Menu() {
        int choice;
        do {
            System.out.println("\n--- Student Menu ---");
            System.out.println("Logged in as: " + loggedInStudent.getName());
            System.out.println("1. View and Filter Available Internships");
            System.out.println("2. View My Internship Applications");
            System.out.println("3. Request Withdrawal");
            System.out.println("4. Change Password");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            choice = InputService.readIntRange(1, 5);

            switch (choice) {
                case 1 -> viewAndFilterInternships();
                case 2 -> viewMyApplications();
                case 3 -> handleWithdrawalRequest();
                case 4 -> changePassword();
                case 5 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private void viewAndFilterInternships() {
        int choice;
		List<Internship> internshipList = internshipManager.getInternshipsForStudent(loggedInStudent);
		if(internshipList.isEmpty()) { System.out.println("No internships available to view."); return; }
		Collection<Internship> filteredList = null;
        do {
            filteredList = internshipList.stream()
                    .filter(i -> internshipFilterStatus == null || i.getStatus() == internshipFilterStatus)
                    .filter(i -> internshipFilterMajor == null || i.getPreferredMajor().equalsIgnoreCase(internshipFilterMajor))
                    .filter(i -> internshipFilterCompany == null || i.getCompanyName().equalsIgnoreCase(internshipFilterCompany))
                    .filter(i -> internshipFilterLevel == null || i.getLevel() == internshipFilterLevel)
                    .collect(Collectors.toList());

            System.out.println("\n--- Filter Internships ---");
            System.out.println("Current Filters:");
            System.out.println("  Status:  " + (internshipFilterStatus == null ? "Any" : internshipFilterStatus));
            System.out.println("  Major:   " + (internshipFilterMajor == null ? "Any" : internshipFilterMajor));
            System.out.println("  Company: " + (internshipFilterCompany == null ? "Any" : internshipFilterCompany));
            System.out.println("  Level:   " + (internshipFilterLevel == null ? "Any" : internshipFilterLevel));
            
            System.out.println("\n--- Internship Results (" + filteredList.size() + " found) ---");
            displayInternships(filteredList);

            System.out.println("\n--- Set/Change Filter ---");
            System.out.println("1. Set Status");
            System.out.println("2. Set Preferred Major");
            System.out.println("3. Set Company Name");
            System.out.println("4. Set Level");
            System.out.println("5. Clear All Internship Filters");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = InputService.readIntRange(0, 5);

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
	

        applyForInternship((List<Internship>)filteredList);
    }

    private void applyForInternship(List<Internship> availableInternships) {
        System.out.printf("Select an Internship to apply for (1-%d), or 0 to go back.\n", availableInternships.size());
        System.out.printf("** Reminder: You can apply for a max of 3 internships. Current: %d **\n",
                loggedInStudent.getSubmittedApplicationIDs().size());
        System.out.print("Enter choice: ");

        int choice = InputService.readIntRange(0, availableInternships.size());

        if (choice == 0) {
            System.out.println("Returning to menu...");
            return;
        }
        Internship selectedInternship = availableInternships.get(choice - 1);

        System.out.printf("Do you wish to apply for %s? (1. Yes, 2. No): ", selectedInternship.getTitle());
        int confirm = InputService.readIntRange(1, 2);

        if (confirm == 1) {
            boolean success = applicationManager.submitApplication(loggedInStudent, selectedInternship);
            if (!success) {
                System.out.println("Application failed. Please check the error message above.");
            }
        } else {
            System.out.println("Application cancelled.");
        }
    }

    private void viewMyApplications() {
        List<InternshipApplication> myApplications = applicationManager.getApplicationsByStudent(loggedInStudent.getUserID());

        if (myApplications.isEmpty()) {
            System.out.println("\nYou have not submitted any applications.");
            return;
        }

        System.out.println("\n--- My Internship Applications ---");
        for (int i = 0; i < myApplications.size(); i++) {
            InternshipApplication app = myApplications.get(i);
            Internship intern = internshipManager.getInternshipById(app.getInternshipID());

            if (intern != null) {
                System.out.printf("%d. %s (%s) | Status: %s\n",
                        (i + 1),
                        intern.getTitle(),
                        intern.getCompanyName(),
                        app.getStatus());
            }
        }
        System.out.println("-----------------------------------------------------------------");

        applicationActions(myApplications);
    }


    private void applicationActions(List<InternshipApplication> myApplications) {
        System.out.printf("Select an Application to manage (1-%d), or 0 to go back.\n", myApplications.size());
        System.out.print("Enter choice: ");
        int choice = InputService.readIntRange(0, myApplications.size());

        if (choice == 0) {
            System.out.println("Returning to menu...");
            return;
        }

        InternshipApplication selectedApp = myApplications.get(choice - 1);
        Internship selectedInternship = internshipManager.getInternshipById(selectedApp.getInternshipID());

        if (selectedInternship == null) {
            System.out.println("Error: The associated internship could not be found.");
            return;
        }

        System.out.printf("\nAction for %s (%s):\n", selectedInternship.getTitle(), selectedApp.getStatus());
        System.out.println("1. Accept Offer");
        System.out.println("0. Go Back");
        System.out.print("Enter choice: ");
        int action = InputService.readIntRange(0, 1);

        switch (action) {
            case 1 -> {
                applicationManager.acceptPlacement(selectedApp.getApplicationID());
            }
            default -> System.out.println("Action cancelled.");
        }
    }

    private void handleWithdrawalRequest() {
        System.out.println("\n--- Request Internship Withdrawal ---");
        List<InternshipApplication> myApplications = applicationManager.getApplicationsByStudent(loggedInStudent.getUserID());

        List<InternshipApplication> withdrawableApps = myApplications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING ||
                               app.getStatus() == ApplicationStatus.SUCCESSFUL ||
                               app.getStatus() == ApplicationStatus.ACCEPTED)
                .collect(Collectors.toList());

        if (withdrawableApps.isEmpty()) {
            System.out.println("You have no applications that can be withdrawn.");
            return;
        }

        System.out.println("Applications eligible for withdrawal request:");
        for (int i = 0; i < withdrawableApps.size(); i++) {
            InternshipApplication app = withdrawableApps.get(i);
            Internship intern = internshipManager.getInternshipById(app.getInternshipID());

            if (intern != null) {
                System.out.printf("%d. %s (%s) | Status: %s\n",
                        (i + 1),
                        intern.getTitle(),
                        intern.getCompanyName(),
                        app.getStatus());
            }
        }
        System.out.println("-----------------------------------------------------------------");

        System.out.printf("Select an Application to withdraw from (1-%d), or 0 to go back.\n", withdrawableApps.size());
        System.out.print("Enter choice: ");
        int choice = InputService.readIntRange(0, withdrawableApps.size());

        if (choice == 0) {
            System.out.println("Returning to menu...");
            return;
        }

        InternshipApplication selectedApp = withdrawableApps.get(choice - 1);
        
        System.out.printf("Are you sure you want to request withdrawal for %s? (1. Yes, 2. No): ",
            internshipManager.getInternshipById(selectedApp.getInternshipID()).getTitle());
        
        int confirm = InputService.readIntRange(1, 2);
        
        if (confirm == 1) {
            applicationManager.requestWithdrawal(selectedApp.getApplicationID());
        } else {
            System.out.println("Withdrawal cancelled.");
        }
    }

	// helpers

	private void displayInternships(Collection<Internship> internships) {
        if (internships == null || internships.isEmpty()) {
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
}