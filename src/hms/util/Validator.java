package hms.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Centralised input-validation helpers shared by the Patient module.
 *
 * <p>Collecting validation in one class avoids duplicating the same checks inside every
 * screen and guarantees that the rules applied by the graphical interface are identical to
 * the rules applied by the service layer. All methods are pure functions with no side
 * effects, which makes them straightforward to test.</p>
 *
 * @author Patient module
 */
public final class Validator {

    /** Utility class: never instantiated. */
    private Validator() {
    }

    /**
     * Tests whether a value carries any non-whitespace content.
     *
     * @param value the value to test, may be {@code null}
     * @return {@code true} when the value is {@code null}, empty or whitespace only
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validates a Malaysian-style contact number: 9 to 15 digits, optionally prefixed
     * with a {@code +} country code and allowing spaces or hyphens as separators.
     *
     * @param contact the contact number entered by the user
     * @return {@code true} when the number is acceptable
     */
    public static boolean isValidContactNumber(String contact) {
        if (isBlank(contact)) {
            return false;
        }
        String digitsOnly = contact.replaceAll("[\\s\\-]", "");
        return digitsOnly.matches("\\+?[0-9]{9,15}");
    }

    /**
     * Performs a pragmatic e-mail check: a non-empty local part, an {@code @}, a domain
     * and a top-level domain of at least two letters.
     *
     * @param email the address entered by the user
     * @return {@code true} when the address is plausibly valid
     */
    public static boolean isValidEmail(String email) {
        if (isBlank(email)) {
            return false;
        }
        return email.matches("[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}");
    }

    /**
     * Parses a date written in ISO form ({@code yyyy-MM-dd}).
     *
     * @param value the text to parse
     * @return the parsed date, or {@code null} when the text is not a valid date
     */
    public static LocalDate parseDate(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Parses a time written in 24-hour form ({@code HH:mm}).
     *
     * @param value the text to parse
     * @return the parsed time, or {@code null} when the text is not a valid time
     */
    public static LocalTime parseTime(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            return LocalTime.parse(value.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Validates a date of birth: it must be a real date, must not lie in the future and
     * must not imply an implausible age.
     *
     * @param value the text to validate
     * @return {@code true} when the date of birth is acceptable
     */
    public static boolean isValidDateOfBirth(String value) {
        LocalDate dob = parseDate(value);
        if (dob == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return !dob.isAfter(today) && dob.isAfter(today.minusYears(130));
    }

    /**
     * Validates a star rating supplied as text.
     *
     * @param value the text to validate
     * @return {@code true} when the value is an integer between 1 and 5 inclusive
     */
    public static boolean isValidRating(String value) {
        if (isBlank(value)) {
            return false;
        }
        try {
            int rating = Integer.parseInt(value.trim());
            return rating >= 1 && rating <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Enforces a password policy of at least four characters, matching the rule already
     * present in {@code hms.model.User#setPassword(String)}.
     *
     * @param password the candidate password
     * @return {@code true} when the password satisfies the policy
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 4;
    }
}
