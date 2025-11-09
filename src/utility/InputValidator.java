package utility;

import java.time.LocalDate;
import java.util.regex.Pattern;

public final class InputValidator {

    private static final Pattern STUDENT_ID = Pattern.compile("^U\\d{7}[A-Z]$", Pattern.CASE_INSENSITIVE);
    private static final Pattern EMAIL      = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    private InputValidator() {}

    public static boolean nonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isPositiveInt(int x) {
        return x > 0;
    }

    public static boolean inRange(int x, int minInclusive, int maxInclusive) {
        return x >= minInclusive && x <= maxInclusive;
    }

    public static boolean isValidStudentId(String id) {
        return nonEmpty(id) && STUDENT_ID.matcher(id.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        return nonEmpty(email) && EMAIL.matcher(email.trim()).matches();
    }

    public static boolean validDateRange(LocalDate open, LocalDate close) {
        return open != null && close != null && !close.isBefore(open);
    }

    public static boolean atMost(int value, int max) {
        return value <= max;
    }

    public static boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNonEmptyString(String input) {
        return input != null && !input.trim().isEmpty();
    }
}
