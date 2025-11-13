package boundary;

import controller.*;
import entity.*;
import entity.Internship.InternshipStatus;
import entity.Internship.InternshipLevel;
import entity.InternshipApplication.ApplicationStatus;
import utility.InputService;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StaffView extends AbstractView {

    private final ApplicationManager applicationManager;
    private final ApprovalManager approvalManager;
    private final InternshipManager internshipManager;
    private final Staff loggedInStaff;

    private InternshipStatus internshipFilterStatus = null;
    private String internshipFilterMajor = null;
    private String internshipFilterCompany = null;
    private InternshipLevel internshipFilterLevel = null;
    private Integer studentFilterYear = null;
    private String studentFilterMajor = null;
    private Boolean repFilterApproved = null;
    private String repFilterCompany = null;
    private ApplicationStatus appFilterStatus = null;


    public StaffView(ApplicationManager applicationManager, ApprovalManager approvalManager,
                     AuthManager authManager, InternshipManager internshipManager, Staff staff) {
        super(authManager, staff);
        this.applicationManager = applicationManager;
        this.approvalManager = approvalManager;
        this.internshipManager = internshipManager;
        this.loggedInStaff = staff;
    }

    @Override
    public void Menu() {
        int choice;
        do {
            System.out.println("\n--- Staff Menu ---");
            System.out.println("Logged in as: " + loggedInStaff.getName());
            System.out.println("1. View Company Representative Account Requests");
            System.out.println("2. View Internship Posting Requests");
            System.out.println("3. View Student Withdrawal Requests");
            System.out.println("4. View & Filter All Internships");
            System.out.println("5. View & Filter All Students");
            System.out.println("6. View & Filter All Company Representatives");
            System.out.println("7. View & Filter All Applications");
            System.out.println("8. Change Password");
            System.out.println("9. Logout");
            System.out.print("Enter choice: ");

            choice = InputService.readInt();

            switch (choice) {
                case 1 -> viewAccountRequests();
                case 2 -> viewInternshipRequests();
                case 3 -> viewWithdrawalRequests();
                case 4 -> viewAndFilterInternships();
                case 5 -> viewAndFilterStudents();
                case 6 -> viewAndFilterCompanyReps();
                case 7 -> viewAndFilterApplications();
                case 8 -> changePassword();
                case 9 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);
    }

    // account requests

    private void viewAccountRequests() {
        List<CompanyRepresentative> pendingReps = approvalManager.getPendingRepresentativeApprovals();
        if (pendingReps.isEmpty()) {
            System.out.println("\nNo company representative accounts are pending approval.");
            return;
        }
        System.out.println("\n--- Pending Company Representative Accounts ---");
        displayCompanyReps(pendingReps);
        approveCompany(pendingReps);
    }

    private void approveCompany(List<CompanyRepresentative> pendingReps) {
        System.out.println("\nSelect a representative to approve/reject (1-" + pendingReps.size() + ").");
        System.out.println("Enter '0' to go back.");
        int choice = InputService.readIntRange(0, pendingReps.size());

        if (choice == 0) return;

        CompanyRepresentative rep = pendingReps.get(choice - 1);
        System.out.printf("Action for %s (%s): 1. Approve, 2. Reject, 0. Cancel\n", rep.getName(), rep.getCompanyName());
        int action = InputService.readIntRange(0, 2);

        switch (action) {
            case 1 -> {
                approvalManager.approveRepresentative(rep.getUserID(), true);
                System.out.printf("** Successfully approved %s! **\n", rep.getName());
            }
            case 2 -> {
                approvalManager.approveRepresentative(rep.getUserID(), false);
                System.out.printf("** Successfully rejected %s! **\n", rep.getName());
            }
            default -> System.out.println("Action cancelled.");
        }
    }


    // internship requests

    private void viewInternshipRequests() {
        List<Internship> pendingInternships = approvalManager.getPendingInternshipApprovals();
        if (pendingInternships.isEmpty()) {
            System.out.println("\nNo internships are pending approval.");
            return;
        }
        System.out.println("\n--- Pending Internship Postings ---");
        displayInternships(pendingInternships);
        approveInternship(pendingInternships);
    }
    
    private void approveInternship(List<Internship> pendingInternships) {
         System.out.println("\nSelect an internship to approve/reject (1-" + pendingInternships.size() + ").");
        System.out.println("Enter '0' to go back.");
        int choice = InputService.readIntRange(0, pendingInternships.size());

        if (choice == 0) return;

        Internship intern = pendingInternships.get(choice - 1);
        System.out.printf("Action for %s: 1. Approve, 2. Reject, 0. Cancel\n", intern.getTitle());
        int action = InputService.readIntRange(0, 2);
        
        switch (action) {
            case 1 -> {
                approvalManager.approveInternship(intern.getInternshipID(), true);
                System.out.printf("** Successfully approved %s! **\n", intern.getTitle());
            }
            case 2 -> {
                approvalManager.approveInternship(intern.getInternshipID(), false);
                System.out.printf("** Successfully rejected %s! **\n", intern.getTitle());
            }
            default -> System.out.println("Action cancelled.");
        }
    }

    // withdrawal requests

    private void viewWithdrawalRequests() {
        System.out.println("\n--- Student Withdrawal Requests ---");
        
        List<InternshipApplication> pendingWithdrawals = applicationManager.getPendingWithdrawals();

        if (pendingWithdrawals.isEmpty()) {
            System.out.println("There are no pending withdrawal requests.");
            return;
        }

        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("%-3s | %-15s | %-20s | %-25s\n", "#", "App ID", "Student Name", "Internship Title");
        System.out.println("---------------------------------------------------------------------------------");

        int i = 1;
        for (InternshipApplication app : pendingWithdrawals) {
            Student student = applicationManager.getStudentbyID(app.getStudentID());
            Internship internship = internshipManager.getInternshipById(app.getInternshipID());

            if (student != null && internship != null) {
                 System.out.printf("%-3d | %-15s | %-20s | %-25s\n",
                    i++,
                    app.getApplicationID(),
                    student.getName(),
                    internship.getTitle()
                );
            }
        }
        System.out.println("---------------------------------------------------------------------------------");

        System.out.println("Select a request to process (1-" + pendingWithdrawals.size() + ").");
        System.out.println("Enter '0' to go back.");
        int choice = InputService.readIntRange(0, pendingWithdrawals.size());

        if (choice == 0) return;

        InternshipApplication selectedApp = pendingWithdrawals.get(choice - 1);

        System.out.printf("Action for %s: 1. Approve Withdrawal, 2. Reject Withdrawal, 0. Cancel\n", selectedApp.getApplicationID());
        int action = InputService.readIntRange(0, 2);

        switch (action) {
            case 1 -> applicationManager.approveWithdrawalRequest(selectedApp.getApplicationID(), true);
            case 2 -> applicationManager.approveWithdrawalRequest(selectedApp.getApplicationID(), false);
            default -> System.out.println("Action cancelled.");
        }
    }

    // filter internships

    private void viewAndFilterInternships() {
        int choice;
        do {
            Collection<Internship> allInternships = internshipManager.getAllInternships();
            Collection<Internship> filteredList = allInternships.stream()
                    .filter(i -> internshipFilterStatus == null || i.getStatus() == internshipFilterStatus)
                    .filter(i -> internshipFilterMajor == null || i.getPreferredMajor().equalsIgnoreCase(internshipFilterMajor))
                    .filter(i -> internshipFilterCompany == null || i.getCompanyName().equalsIgnoreCase(internshipFilterCompany))
                    .filter(i -> internshipFilterLevel == null || i.getLevel() == internshipFilterLevel)
                    .sorted(Comparator.comparing(i -> i.getTitle()))
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
    }

    // filter students

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

    // filter company reps

    private void viewAndFilterCompanyReps() {
        int choice;
        do {
            Collection<CompanyRepresentative> allReps = approvalManager.getAllRepresentatives();
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

    // filter applications

    private void viewAndFilterApplications() {
        int choice;
        do {
            Collection<InternshipApplication> allApps = applicationManager.getAllApplications();
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
    
    
    // change pwd
    
    // helpers

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
}