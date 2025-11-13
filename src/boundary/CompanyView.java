package boundary;

import utility.InputService;
import utility.InputValidator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import controller.*;
import entity.*;
import entity.Internship.InternshipLevel;
import entity.Internship.InternshipStatus;
import entity.InternshipApplication.ApplicationStatus;
import java.time.LocalDate;

/**
 * Boundary class (CLI View) for a logged-in Company Representative.
 * Handles all menu options and I/O for their functions.
 */
public class CompanyView extends AbstractView {

    private final ApplicationManager applicationManager;
    private final InternshipManager internshipManager;
    private final CompanyRepresentative loggedInRep; // Use the specific type

    // --- Filter State Fields ---
    private InternshipStatus internshipFilterStatus = null;
    private String internshipFilterMajor = null;
    private InternshipLevel internshipFilterLevel = null;

    public CompanyView(ApplicationManager applicationManager, AuthManager authManager,
                       InternshipManager internshipManager, CompanyRepresentative cr) {
        super(authManager, cr);
        this.applicationManager = applicationManager;
        this.internshipManager = internshipManager;
        this.loggedInRep = cr;
    }

    @Override
    public void Menu() {
        int choice;
        do {
            System.out.println("\n--- Company Menu (" + loggedInRep.getCompanyName() + ") ---");
            System.out.println("Logged in as: " + loggedInRep.getName());
            System.out.println("1. Create New Internship");
            System.out.println("2. View & Manage My Internships");
            System.out.println("3. Change Password");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            choice = InputService.readIntRange(1, 4);

            switch (choice) {
                case 1 -> createInternship();
                case 2 -> viewAndFilterMyInternships();
                case 3 -> changePassword(); // Inherited from AbstractView
                case 4 -> System.out.println("Logging out ...");
                default -> System.out.println("Invalid choice. Please try again. ");
            }
        } while (choice != 4);
    }

    /**
     * Handles the logic for creating a new internship.
     */
    private void createInternship() {
        if (loggedInRep.getPostedInternshipIDs().size() >= 5) {
            System.out.println("Error: You have already posted the maximum of 5 internships.");
            return;
        }

        System.out.println("\n--- Create New Internship ---");
        System.out.print("Enter Internship Title: ");
        String internTitle = InputService.readString(); // Now loops until non-empty

        System.out.print("Enter Description: ");
        String internDescription = InputService.readString();

        System.out.println("Enter Internship Level (1-3): \n1. BASIC \n2. INTERMEDIATE \n3. ADVANCED");
        System.out.print("Enter choice: ");
        int levelChoice = InputService.readIntRange(1, 3);
        InternshipLevel internLevel = switch (levelChoice) {
            case 1 -> InternshipLevel.BASIC;
            case 2 -> InternshipLevel.INTERMEDIATE;
            case 3 -> InternshipLevel.ADVANCED;
            default -> InternshipLevel.BASIC; // Should not happen with readIntRange
        };

        System.out.print("Enter Preferred Major: ");
        String internMajor = InputService.readString();
        LocalDate internOpenDate = null;
        do {
            internOpenDate = InputService.readDate("Enter opening date (dd/MM/yyyy): ");
        } while (internOpenDate == null);

        LocalDate internCloseDate = null;
        do {
            internCloseDate = InputService.readDate("Enter closing date (dd/MM/yyyy): ");
            if (internCloseDate != null && internCloseDate.isBefore(internOpenDate)) {
                System.out.println("Closing date must be on or after the opening date (" + internOpenDate + ")");
                internCloseDate = null; // Force loop to repeat
            }
        } while (internCloseDate == null);

        System.out.print("Enter Number of Slots (1-10): ");
        int internSlots = InputService.readIntRange(1, 10);

        // Call the manager to handle creation
        internshipManager.createInternship(
                internTitle, internDescription, internLevel, internMajor,
                internOpenDate, internCloseDate, loggedInRep, internSlots
        );
        // No recursive call to Menu()
    }

    /**
     * Replaces ViewInternshipsCreated.
     * Provides a looping menu to filter and manage internships.
     */
    private void viewAndFilterMyInternships() {
        int choice;
        do {
            // 1. Apply Filters
            List<Internship> myInternships = internshipManager.getInternshipsByCompany(loggedInRep.getCompanyName());
            List<Internship> filteredList = myInternships.stream()
                    .filter(i -> internshipFilterStatus == null || i.getStatus() == internshipFilterStatus)
                    .filter(i -> internshipFilterMajor == null || i.getPreferredMajor().equalsIgnoreCase(internshipFilterMajor))
                    .filter(i -> internshipFilterLevel == null || i.getLevel() == internshipFilterLevel)
                    .sorted(Comparator.comparing(Internship::getTitle)) // Always sort by title
                    .collect(Collectors.toList());

            // 2. Display Current State
            System.out.println("\n--- My Posted Internships (" + filteredList.size() + " found) ---");
            System.out.println("Current Filters:");
            System.out.println("  Status: " + (internshipFilterStatus == null ? "Any" : internshipFilterStatus));
            System.out.println("  Major:  " + (internshipFilterMajor == null ? "Any" : internshipFilterMajor));
            System.out.println("  Level:  " + (internshipFilterLevel == null ? "Any" : internshipFilterLevel));
            
            displayInternshipList(filteredList); // Use helper to print

            // 3. Show Menu
            System.out.println("\n--- Manage Internships ---");
            System.out.println("Select an Internship (1-" + filteredList.size() + ") to manage.");
            System.out.println("\n--- Filter Options ---");
            System.out.println("91. Set Status Filter");
            System.out.println("92. Set Major Filter");
            System.out.println("93. Set Level Filter");
            System.out.println("94. Clear All Filters");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            choice = InputService.readInt(); // Not readIntRange, as 1-N is allowed

            // 4. Handle Choice
            if (choice > 0 && choice <= filteredList.size()) {
                // User selected an internship
                manageInternshipMenu(filteredList.get(choice - 1));
            } else {
                try {
                    switch (choice) {
                        case 91 -> {
                            System.out.print("Enter Status (PENDING, APPROVED, FILLED, REJECTED) or 'clear': ");
                            String s = InputService.readString().toUpperCase();
                            internshipFilterStatus = s.equals("CLEAR") ? null : InternshipStatus.valueOf(s);
                        }
                        case 92 -> {
                            System.out.print("Enter Preferred Major or 'clear': ");
                            String s = InputService.readString();
                            internshipFilterMajor = (s.equalsIgnoreCase("clear") || s.isEmpty()) ? null : s;
                        }
                        case 93 -> {
                            System.out.print("Enter Level (BASIC, INTERMEDIATE, ADVANCED) or 'clear': ");
                            String s = InputService.readString().toUpperCase();
                            internshipFilterLevel = s.equals("CLEAR") ? null : InternshipLevel.valueOf(s);
                        }
                        case 94 -> {
                            internshipFilterStatus = null;
                            internshipFilterMajor = null;
                            internshipFilterLevel = null;
                            System.out.println("All filters cleared.");
                        }
                        case 0 -> System.out.println("Returning to main menu...");
                        default -> System.out.println("Invalid choice.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid filter value. Try again.");
                }
            }
        } while (choice != 0);
    }
    
    /**
     * Helper to display a list of internships.
     */
    private void displayInternshipList(List<Internship> internships) {
        if (internships.isEmpty()) {
            System.out.println("No internships found matching this filter.");
            return;
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.printf("%-3s | %-25s | %-12s | %-10s | %-12s | %-7s | %s\n", 
            "#", "Title", "Status", "Major", "Level", "Visible", "Slots");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < internships.size(); i++) {
            Internship intern = internships.get(i);
            System.out.printf("%-3d | %-25s | %-12s | %-10s | %-12s | %-7s | %d/%d\n",
                    (i + 1),
                    intern.getTitle(),
                    intern.getStatus(),
                    intern.getPreferredMajor(),
                    intern.getLevel(),
                    intern.isVisible() ? "ON" : "OFF",
                    intern.getSlotsFilled(),
                    intern.getNumberOfSlots());
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    /**
     * A sub-menu for managing a single, selected internship.
     */
    private void manageInternshipMenu(Internship intern) {
        int choice;
        do {
            System.out.println("\n--- Manage Internship: " + intern.getTitle() + " ---");
            System.out.println("Status: " + intern.getStatus() + " | Visible: " + (intern.isVisible() ? "ON" : "OFF"));
            System.out.println("1. View/Manage Applications (" + intern.getApplicationIDs().size() + ")");
            System.out.println("2. Toggle Visibility");
            System.out.println("3. Edit Details (Before Approval)");
            System.out.println("0. Back to Internships List");
            System.out.print("Enter choice: ");
            
            choice = InputService.readIntRange(0, 3);
            
            switch (choice) {
                case 1 -> viewApplicationsForInternship(intern);
                case 2 -> intern.setVisibility(!intern.isVisible());
                case 3 -> editInternship(intern);
                case 0 -> System.out.println("Returning to internships list...");
            }
        } while (choice != 0);
    }
    
    /**
     * Handles editing an internship's details.
     * Per assignment rules, only allowed if status is PENDING.
     */
    private void editInternship(Internship intern) {
        if (intern.getStatus() != InternshipStatus.PENDING) {
            System.out.println("Error: This internship cannot be edited as it has already been processed by Staff.");
            return;
        }
        
        System.out.println("\n--- Editing Internship: " + intern.getTitle() + " ---");
        System.out.println("Leave blank to keep current value.");

        // Edit Title
        System.out.printf("Title (current: %s): ", intern.getTitle());
        String newTitle = InputService.readString();
        if (InputValidator.nonEmpty(newTitle)) {
            intern.setTitle(newTitle);
        }

        // Edit Description
        System.out.printf("Description (current: %s): ", intern.getDescription());
        String newDesc = InputService.readString();
        if (InputValidator.nonEmpty(newDesc)) {
            intern.setDescription(newDesc);
        }
        
        // Edit Slots
        System.out.printf("Number of Slots (current: %d): ", intern.getNumberOfSlots());
        String newSlotsStr = InputService.readString();
        if (InputValidator.nonEmpty(newSlotsStr)) {
            try {
                int newSlots = Integer.parseInt(newSlotsStr);
                if (newSlots > 0 && newSlots <= 10) {
                    intern.setNumberOfSlots(newSlots);
                } else {
                    System.out.println("Invalid slot count (1-10). Keeping original value.");
                }
            } catch (NumberFormatException e) {
                 System.out.println("Invalid number. Keeping original value.");
            }
        }
        System.out.println("Internship details updated.");
    }

    /**
     * Handles viewing and approving/rejecting applications for one internship.
     */
    private void viewApplicationsForInternship(Internship intern) {
        List<InternshipApplication> listApp = applicationManager.getApplicationsForInternship(intern.getInternshipID());

        if (listApp.isEmpty()) {
            System.out.println("\nNo applications for this internship yet.");
            return;
        }

        System.out.println("\n--- Student Applications for: " + intern.getTitle() + " ---");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-3s | %-20s | %-10s | %-5s | %s\n", "#", "Name", "Major", "Year", "Status");
        System.out.println("--------------------------------------------------------------------------------");
        for (int i = 0; i < listApp.size(); i++) {
            InternshipApplication intApp = listApp.get(i);
            Student s = applicationManager.getStudentbyID(intApp.getStudentID());
            if (s != null) {
                System.out.printf("%-3d | %-20s | %-10s | %-5d | %s\n",
                        (i + 1),
                        s.getName(),
                        s.getMajor(),
                        s.getYear(),
                        intApp.getStatus()
                );
            }
        }
        System.out.println("--------------------------------------------------------------------------------");
        handleApplicationApproval(listApp);
    }

    /**
     * Handles the logic for approving or rejecting a single application.
     */
    private void handleApplicationApproval(List<InternshipApplication> listApp) {
        System.out.println("\nSelect an Application to approve or reject (1-" + listApp.size() + ").");
        System.out.println("Otherwise, enter '0' to go back.");
        System.out.print("Enter choice: ");

        int choice = InputService.readIntRange(0, listApp.size());

        if (choice == 0) {
            System.out.println("Returning to internship management...");
            return;
        }

        InternshipApplication intApp = listApp.get(choice - 1);
        Student s = applicationManager.getStudentbyID(intApp.getStudentID());

        if (intApp.getStatus() != ApplicationStatus.PENDING) {
            System.out.println("This application is not pending (Status: " + intApp.getStatus() + "). No action can be taken.");
            return;
        }

        System.out.printf("Action for student %s: \n1. Approve \n2. Reject \n0. Cancel\n", s.getName());
        System.out.print("Enter choice: ");
        int action = InputService.readIntRange(0, 2);

        switch (action) {
            case 1:
                applicationManager.updateApplicationStatus(intApp.getApplicationID(), ApplicationStatus.SUCCESSFUL);
                break;
            case 2:
                applicationManager.updateApplicationStatus(intApp.getApplicationID(), ApplicationStatus.UNSUCCESSFUL);
                break;
            default:
                System.out.println("Action cancelled.");
                break;
        }
    }
}