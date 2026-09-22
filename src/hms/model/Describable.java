package hms.model;

/**
 * Contract implemented by every Patient-module record that can be presented to the user
 * as a single line of text.
 *
 * <p>Declaring this interface allows the graphical layer to treat appointments,
 * prescriptions, medical records and feedback entries uniformly: a list of
 * {@code Describable} objects can be rendered without the user interface needing to know
 * the concrete type of each element. This is a direct application of <em>polymorphism</em>
 * through an interface, complementing the inheritance hierarchy rooted at {@link User}.</p>
 *
 * @author Patient module
 */
public interface Describable {

    /**
     * Produces a short, human-readable summary of this record.
     *
     * @return a one-line description suitable for display in a list or table
     */
    String getSummary();
}
