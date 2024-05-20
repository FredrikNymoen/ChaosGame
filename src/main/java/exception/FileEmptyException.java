package exception;

/**
 * Exception thrown when a file is empty.
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class FileEmptyException extends Exception{

  /**
   * Constructor for FileEmptyException.
   * @param message the message to be displayed
   */
  public FileEmptyException(String message) {
    super(message);
  }
}
