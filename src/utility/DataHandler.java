package utility;

import entity.*;
import entity.Internship.InternshipLevel;
import entity.Internship.InternshipStatus;
import entity.InternshipApplication.ApplicationStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class DataHandler {

    private static final String DELIMITER = ",";
    private static final String LIST_DELIMITER = ";";
    private DataHandler() {}

    public static Map<String, Student> loadStudents(String path) {
        Map<String, Student> students = new HashMap<>();
        if (!Files.exists(Paths.get(path))) {
            System.err.println("[DataHandler] Student file not found, returning empty map: " + path);
            return students;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(DELIMITER);
                Student student = null;

                try {
                    if (data.length == 5) {
                        // uninitialised student: StudentID, Name, Major, Year, Email
                        String userID = data[0];
                        String name = data[1];
                        String major = data[2];
                        int year = Integer.parseInt(data[3]);
                        String email = data[4];
                        student = new Student(userID, name, email, "password", year, major);
                    } else if (data.length >= 6) {
                        // Saved format: userID,name,password,email,major,yearOfStudy,applicationIDs
                        String userID = data[0];
                        String name = data[1];
                        String password = data[2];
                        String email = data[3];
                        String major = data[4];
                        int year = Integer.parseInt(data[5]);
                        student = new Student(userID, name, email, password, year, major);

                        if (data.length == 7 && !data[6].trim().isEmpty()) {
                            String[] appIDs = data[6].split(LIST_DELIMITER);
                            for (String appID : appIDs) {
                                student.addApplication(appID);
                            }
                        }
                    }

                    if (student != null) {
                        students.put(student.getUserID(), student);
                    } else {
                        System.err.println("[DataHandler] Skipping malformed student line: " + line);
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("[DataHandler] Error parsing student line: " + line + " | Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[DataHandler] Error loading students: " + e.getMessage());
        }
        System.out.println("[DataHandler] Loaded " + students.size() + " students from " + path);
        return students;
    }

    public static void saveStudents(String path, Map<String, Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("userID,name,password,email,major,yearOfStudy,applicationIDs");
            writer.newLine();
            for (Student student : students.values()) {
                String appIDs = String.join(LIST_DELIMITER, student.getSubmittedApplicationIDs());
                String line = String.join(DELIMITER,
                        student.getUserID(),
                        student.getName(),
                        student.getPassword(),
                        student.getEmail(),
                        student.getMajor(),
                        String.valueOf(student.getYear()),
                        appIDs
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("[DataHandler] Error saving students: " + e.getMessage());
        }
        System.out.println("[DataHandler] Saved " + students.size() + " students to " + path);
    }

    public static Map<String, Staff> loadStaff(String path) {
        Map<String, Staff> staff = new HashMap<>();
        if (!Files.exists(Paths.get(path))) {
            System.err.println("[DataHandler] Staff file not found, returning empty map: " + path);
            return staff;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(DELIMITER);
                Staff s = null;
                try {
                    if (data.length == 5) {
                        // uninitialised staff: StaffID, Name, Role, Department, Email
                        String userID = data[0];
                        String name = data[1];
                        String role = data[2];
                        String department = data[3];
                        String email = data[4];
                        s = new Staff(userID, name, email, "password", department, role);
                    } else if (data.length == 6) {
                        // Saved format: userID,name,email,password,staffDepartment,role
                        s = new Staff(data[0], data[1], data[2], data[3], data[4], data[5]);
                    }
                    if (s != null) {
                        staff.put(s.getUserID(), s);
                    } else {
                        System.err.println("[DataHandler] Skipping malformed staff line: " + line);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("[DataHandler] Error parsing staff line: " + line + " | Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[DataHandler] Error loading staff: " + e.getMessage());
        }
        System.out.println("[DataHandler] Loaded " + staff.size() + " staff from " + path);
        return staff;
    }

    public static void saveStaff(String path, Map<String, Staff> staff) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("userID,name,email,password,staffDepartment,role");
            writer.newLine();

            for (Staff s : staff.values()) {
                String line = String.join(DELIMITER,
                        s.getUserID(),
                        s.getName(),
                        s.getEmail(),
                        s.getPassword(),
                        s.getDepartment(),
                        s.getRole()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("[DataHandler] Error saving staff: " + e.getMessage());
        }
        System.out.println("[DataHandler] Saved " + staff.size() + " staff to " + path);
    }

    public static Map<String, CompanyRepresentative> loadCompanyReps(String path) {
        Map<String, CompanyRepresentative> reps = new HashMap<>();
        if (!Files.exists(Paths.get(path))) {
            System.err.println("[DataHandler] CompanyReps file not found, returning empty map: " + path);
            return reps;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(DELIMITER);
                CompanyRepresentative rep = null;

                try {
                    if (data.length == 7) {
                        // uninitialised: CompanyRepID, Name, CompanyName, Department, Position, Email, Status
                        String userID = data[0];
                        String name = data[1];
                        String companyName = data[2];
                        String department = data[3];
                        String position = data[4];
                        String email = data[5];
                        rep = new CompanyRepresentative(userID, name, email, "password", companyName, department, position);
                        rep.approveRepresentative(false);
                    } else if (data.length >= 8) {
                        rep = new CompanyRepresentative(data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
                        rep.approveRepresentative(Boolean.parseBoolean(data[7]));

                        if (data.length == 9 && !data[8].trim().isEmpty()) {
                            String[] internshipIDs = data[8].split(LIST_DELIMITER);
                            for (String id : internshipIDs) {
                                rep.addInternship(id);
                            }
                        }
                    }
                    
                    if (rep != null) {
                        reps.put(rep.getUserID(), rep);
                    } else {
                         System.err.println("[DataHandler] Skipping malformed rep line: " + line);
                    }
                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                     System.err.println("[DataHandler] Error parsing rep line: " + line + " | Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[DataHandler] Error loading company reps: " + e.getMessage());
        }
        System.out.println("[DataHandler] Loaded " + reps.size() + " company reps from " + path);
        return reps;
    }

    public static void saveCompanyReps(String path, Map<String, CompanyRepresentative> reps) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("userID,name,email,password,companyName,department,position,isApproved,internshipIDs");
            writer.newLine();

            for (CompanyRepresentative rep : reps.values()) {
                String internshipIDs = String.join(LIST_DELIMITER, rep.getPostedInternshipIDs());
                String line = String.join(DELIMITER,
                        rep.getUserID(),
                        rep.getName(),
                        rep.getEmail(),
                        rep.getPassword(),
                        rep.getCompanyName(),
                        rep.getDepartment(),
                        rep.getPosition(),
                        String.valueOf(rep.approvedRepresentative()),
                        internshipIDs
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("[DataHandler] Error saving company reps: " + e.getMessage());
        }
        System.out.println("[DataHandler] Saved " + reps.size() + " company reps to " + path);
    }

    public static Map<String, Internship> loadInternships(String path) {
        Map<String, Internship> internships = new HashMap<>();
        if (!Files.exists(Paths.get(path))) {
            System.err.println("[DataHandler] Internships file not found, returning empty map: " + path);
            return internships;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(DELIMITER);

                try {
                    if (data.length == 13) {
                        Internship internship = new Internship(
                                data[0], // internshipID
                                data[1], // title
                                data[2], // description
                                InternshipLevel.valueOf(data[3].toUpperCase()), // level
                                data[4], // preferredMajor
                                LocalDate.parse(data[5]), // openDate
                                LocalDate.parse(data[6]), // closeDate
                                data[8], // companyName
                                data[9], // repID
                                Integer.parseInt(data[10]) // slotsTotal
                        );
                        internship.setStatus(InternshipStatus.valueOf(data[7].toUpperCase()));
                        internship.setNumberOfSlots(Integer.parseInt(data[11]));
                        internship.setVisibility(Boolean.parseBoolean(data[12]));

                        internships.put(internship.getInternshipID(), internship);
                    } else {
                         System.err.println("[DataHandler] Skipping malformed internship line: " + line);
                    }
                } catch (DateTimeParseException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("[DataHandler] Error parsing internship line: " + line + " | Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[DataHandler] Error loading internships: " + e.getMessage());
        }
        System.out.println("[DataHandler] Loaded " + internships.size() + " internships from " + path);
        return internships;
    }

    public static void saveInternships(String path, Map<String, Internship> internships) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("internshipID,title,description,level,preferredMajor,openDate,closeDate,status,companyName,repID,slotsTotal,slotsFilled,isVisible");
            writer.newLine();

            for (Internship i : internships.values()) {
                String line = String.join(DELIMITER,
                        i.getInternshipID(),
                        i.getTitle(),
                        i.getDescription(),
                        i.getLevel().name(),
                        i.getPreferredMajor(),
                        i.getApplicationOpenDate().toString(),
                        i.getApplicationCloseDate().toString(),
                        i.getStatus().name(),
                        i.getCompanyName(),
                        i.getCompanyRepID(),
                        String.valueOf(i.getNumberOfSlots()),
                        String.valueOf(i.getSlotsFilled()),
                        String.valueOf(i.isVisible())
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("[DataHandler] Error saving internships: " + e.getMessage());
        }
        System.out.println("[DataHandler] Saved " + internships.size() + " internships to " + path);
    }

    
    public static Map<String, InternshipApplication> loadApplications(String path) {
        Map<String, InternshipApplication> applications = new HashMap<>();
        if (!Files.exists(Paths.get(path))) {
            System.err.println("[DataHandler] Applications file not found, returning empty map: " + path);
            return applications;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(DELIMITER);

                try {
                     if (data.length == 4) {
                        InternshipApplication app = new InternshipApplication(data[0], data[1], data[2]);
                        app.setStatus(ApplicationStatus.valueOf(data[3].toUpperCase()));
                        applications.put(app.getApplicationID(), app);
                    } else {
                         System.err.println("[DataHandler] Skipping malformed application line: " + line);
                    }
                } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                     System.err.println("[DataHandler] Error parsing application line: " + line + " | Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[DataHandler] Error loading applications: " + e.getMessage());
        }
        System.out.println("[DataHandler] Loaded " + applications.size() + " applications from " + path);
        return applications;
    }

    public static void saveApplications(String path, Map<String, InternshipApplication> applications) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("applicationID,internshipID,studentID,status");
            writer.newLine();

            for (InternshipApplication app : applications.values()) {
                String line = String.join(DELIMITER,
                        app.getApplicationID(),
                        app.getInternshipID(),
                        app.getStudentID(),
                        app.getStatus().name()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("[DataHandler] Error saving applications: " + e.getMessage());
        }
        System.out.println("[DataHandler] Saved " + applications.size() + " applications to " + path);
    }
}

