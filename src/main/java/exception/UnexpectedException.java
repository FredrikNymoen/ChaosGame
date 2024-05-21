package exception;

/**
 * Exception thrown when an unexpected error occurs.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class UnexpectedException extends RuntimeException {

  /**
   * Constructor for UnexpectedException.
   *
   * @param message the message to be displayed
   */
  public UnexpectedException(String message) {
    super(message);
  }
}
