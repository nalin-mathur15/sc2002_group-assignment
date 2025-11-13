package utility;

import java.util.regex.Pattern;

/** Utility class for validating various data formats. */
public final class InputValidator {
    /** Regex pattern for validating student IDs. */
    private static final Pattern STUDENT_ID = Pattern.compile("^U\\d{7}[A-Z]$", Pattern.CASE_INSENSITIVE);
    
    /** Regex pattern for validating email addresses. */
    private static final Pattern EMAIL      = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    /** Private constructor to prevent instantiation. */
    private InputValidator() {}

    /** Checks if a string is not null and not empty. 
     * @param s The string. 
     * @return true if not empty, false otherwise. 
    */
    public static boolean nonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }


    /** Validates a student ID string using regex. 
     * @param id The ID. 
     * @return true if valid, false otherwise. 
    */
    public static boolean isValidStudentId(String id) {
        return nonEmpty(id) && STUDENT_ID.matcher(id.trim()).matches();
    }

    /** Validates an email string using regex. 
     * @param email The email. 
     * @return true if valid, false otherwise. 
    */
    public static boolean isValidEmail(String email) {
        return nonEmpty(email) && EMAIL.matcher(email.trim()).matches();
    }

    /** Checks if a string can be parsed as an integer. 
     * @param input The string. 
     * @return true if valid, false otherwise. 
    */
    public static boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
