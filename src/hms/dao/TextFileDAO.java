package hms.dao;

import hms.util.AppLogger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reusable base class for every data-access object that persists records as delimited
 * lines in a plain-text file.
 *
 * <p>The coursework forbids the use of a database, so each entity type is stored in its own
 * {@code .txt} file. Reading a file, writing a file, skipping blank lines, recovering from a
 * malformed line and generating the next identifier are identical tasks for every entity,
 * so they are implemented once here. A subclass supplies only the three pieces that genuinely
 * differ: how one record is turned into a line, how a line is turned back into a record, and
 * how the identifier is read from a record.</p>
 *
 * <p>This is the <em>template method</em> pattern and an illustration of <em>abstraction</em>:
 * {@link #loadAll()} defines the fixed algorithm, while {@link #fromLine(String)} and
 * {@link #toLine(Object)} are the abstract steps deferred to subclasses. The class is also
 * <em>generic</em> in {@code T}, so the compiler enforces type safety at every call site
 * rather than requiring casts.</p>
 *
 * @param <T> the entity type managed by the concrete data-access object
 * @author Patient module
 */
public abstract class TextFileDAO<T> {

    /** Field separator shared by every data file in the project. */
    protected static final String DELIMITER = ",";

    /** Name of the text file backing this data-access object. */
    private final String fileName;

    /**
     * Creates a data-access object bound to a text file.
     *
     * @param fileName name of the file, resolved against the working directory
     */
    protected TextFileDAO(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Converts one record into the single line that represents it on disk.
     *
     * @param item the record to serialise
     * @return the line to write, without a trailing line separator
     */
    protected abstract String toLine(T item);

    /**
     * Converts one line read from disk back into a record.
     *
     * @param line the line read from the file, never blank
     * @return the reconstructed record, or {@code null} when the line is malformed
     */
    protected abstract T fromLine(String line);

    /**
     * Reads the identifier of a record, used when generating the next free identifier.
     *
     * @param item the record to inspect
     * @return the record's identifier
     */
    protected abstract String getId(T item);

    /**
     * Returns the name of the backing file.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Loads every record held in the backing file.
     *
     * <p>A file that does not yet exist is treated as an empty collection rather than an
     * error, so the system runs correctly on a fresh checkout. A line that cannot be parsed
     * is skipped and recorded in the activity log rather than aborting the whole load, so a
     * single corrupt row cannot make the application unusable.</p>
     *
     * @return every record successfully read, in file order; never {@code null}
     */
    public List<T> loadAll() {
        List<T> items = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) {
            return items;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                T item = fromLine(line);
                if (item == null) {
                    AppLogger.error(getClass().getSimpleName(),
                            "Skipped malformed line " + lineNumber + " in " + fileName);
                    continue;
                }
                items.add(item);
            }
        } catch (IOException e) {
            AppLogger.error(getClass().getSimpleName(),
                    "Unable to read " + fileName + ": " + e.getMessage());
        }
        return items;
    }

    /**
     * Appends a single record to the end of the backing file.
     *
     * @param item the record to append
     * @return {@code true} when the record was written successfully
     */
    public boolean append(T item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(toLine(item));
            writer.newLine();
            return true;
        } catch (IOException e) {
            AppLogger.error(getClass().getSimpleName(),
                    "Unable to append to " + fileName + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Rewrites the backing file so that it contains exactly the supplied records.
     *
     * <p>Used for updates and deletions, which a purely append-based file cannot express.</p>
     *
     * @param items the complete set of records the file should hold afterwards
     * @return {@code true} when the file was rewritten successfully
     */
    public boolean saveAll(List<T> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (T item : items) {
                writer.write(toLine(item));
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            AppLogger.error(getClass().getSimpleName(),
                    "Unable to write " + fileName + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Generates the next unused identifier for this file.
     *
     * <p>Identifiers follow the convention {@code PREFIX} plus a zero-padded number, for
     * example {@code APT007}. The method scans the existing records, finds the highest
     * numeric suffix currently in use and returns the next one, so identifiers stay unique
     * even after records have been deleted.</p>
     *
     * @param prefix the alphabetic prefix, e.g. {@code "APT"}
     * @param width  how many digits the numeric part should occupy
     * @return the next free identifier
     */
    public String nextId(String prefix, int width) {
        int highest = 0;
        for (T item : loadAll()) {
            String id = getId(item);
            if (id == null || !id.startsWith(prefix)) {
                continue;
            }
            try {
                highest = Math.max(highest, Integer.parseInt(id.substring(prefix.length())));
            } catch (NumberFormatException ignored) {
                // An identifier that does not end in digits simply does not take part.
            }
        }
        return prefix + String.format("%0" + width + "d", highest + 1);
    }

    /**
     * Removes characters that would break the one-record-per-line file format.
     *
     * <p>Line breaks are replaced with spaces in every field. Commas are additionally
     * replaced in fields that are <em>not</em> written last on the line, because only the
     * final field can safely contain the delimiter.</p>
     *
     * @param value        the raw value supplied by the user
     * @param allowCommas  {@code true} for the final field on a line, which may keep commas
     * @return a value that is safe to write
     */
    protected static String clean(String value, boolean allowCommas) {
        if (value == null) {
            return "";
        }
        String cleaned = value.replace("\r", " ").replace("\n", " ").trim();
        if (!allowCommas) {
            cleaned = cleaned.replace(DELIMITER, " ");
        }
        return cleaned;
    }

    /**
     * Convenience form of {@link #clean(String, boolean)} for fields that must not contain
     * the delimiter.
     *
     * @param value the raw value supplied by the user
     * @return a value that is safe to write in any position on the line
     */
    protected static String clean(String value) {
        return clean(value, false);
    }
}
