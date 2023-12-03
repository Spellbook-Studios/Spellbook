package dk.sebsa.spellbook.platform;

/**
 * Indicates a platform specific Runtime error has occurred
 *
 * @author sebs
 * @since 1.0.0
 */
public class PlatformException extends RuntimeException {
    /**
     * A platform exception
     */
    public PlatformException() {
        super();
    }

    /**
     * A platform exception with a message
     *
     * @param message Error message
     */
    public PlatformException(String message) {
        super(message);
    }

    /**
     * A platform exception with a message and a cause
     *
     * @param message Error message
     * @param cause   Reason for error
     */
    public PlatformException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * A platform exception with a cause
     *
     * @param cause Reason for error
     */
    public PlatformException(Throwable cause) {
        super(cause);
    }


    /**
     * A platform exception with a message and a cause
     *
     * @param message            the detail message.
     * @param cause              the cause.  (A {@code null} value is permitted,
     *                           and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public PlatformException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
