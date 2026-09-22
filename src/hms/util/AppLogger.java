package hms.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Minimal append-only activity logger for the Hospital Management System.
 *
 * <p>Every significant action performed by a patient (login, booking, cancellation,
 * profile change, feedback submission) is appended to {@code hms_activity.log} together
 * with a timestamp. This provides a simple audit trail without requiring a database,
 * which is prohibited by the coursework specification.</p>
 *
 * <p>The class is deliberately <em>static-only</em>: it holds no per-instance state and
 * therefore its constructor is private to prevent instantiation.</p>
 *
 * @author Patient module
 */
public final class AppLogger {

    /** Name of the plain-text log file, created in the project working directory. */
    private static final String LOG_FILE = "hms_activity.log";

    /** Timestamp format used for every log entry. */
    private static final DateTimeFormatter STAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Utility class: never instantiated. */
    private AppLogger() {
    }

    /**
     * Appends an informational entry to the activity log.
     *
     * @param source short name of the component raising the entry, e.g. {@code "AppointmentService"}
     * @param message human-readable description of what happened
     */
    public static void info(String source, String message) {
        write("INFO", source, message);
    }

    /**
     * Appends an error entry to the activity log.
     *
     * @param source short name of the component raising the entry
     * @param message human-readable description of the failure
     */
    public static void error(String source, String message) {
        write("ERROR", source, message);
    }

    /**
     * Writes a single line to the log file.
     *
     * <p>Logging must never crash the application, so any {@link IOException} raised while
     * writing is reported on the standard error stream and then swallowed.</p>
     *
     * @param level   severity label
     * @param source  component name
     * @param message description of the event
     */
    private static void write(String level, String source, String message) {
        String line = LocalDateTime.now().format(STAMP) + " [" + level + "] " + source + " - " + message;
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            out.println(line);
        } catch (IOException e) {
            System.err.println("Unable to write to activity log: " + e.getMessage());
        }
    }
}
