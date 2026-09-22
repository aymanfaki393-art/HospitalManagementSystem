package hms.service;

/**
 * The outcome of a service operation: whether it succeeded, and a message explaining why.
 *
 * <p>Returning a result object rather than a bare {@code boolean} keeps business rules in the
 * service layer instead of leaking them into the user interface. A screen only has to display
 * {@link #getMessage()}; it never has to know which rule was broken. Keeping the rules in one
 * place also guarantees that the same checks apply no matter which screen calls them.</p>
 *
 * @author Patient module
 */
public class ServiceResult {

    /** Whether the operation completed successfully. */
    private final boolean success;

    /** Message suitable for display to the user. */
    private final String message;

    /**
     * Creates a result.
     *
     * @param success whether the operation succeeded
     * @param message explanation suitable for display
     */
    private ServiceResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Creates a successful result.
     *
     * @param message confirmation to show the user
     * @return a result marked as successful
     */
    public static ServiceResult ok(String message) {
        return new ServiceResult(true, message);
    }

    /**
     * Creates a failed result.
     *
     * @param message explanation of why the operation was refused
     * @return a result marked as failed
     */
    public static ServiceResult fail(String message) {
        return new ServiceResult(false, message);
    }

    /** @return {@code true} when the operation succeeded */
    public boolean isSuccess() {
        return success;
    }

    /** @return the message describing the outcome */
    public String getMessage() {
        return message;
    }
}
