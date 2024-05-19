package util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles logging various types of errors and warnings.
 * It provides methods for logging specific error messages related to file operations,
 * transformation selection, copying transformations, and iterative transformations.
 */
public class ErrorHandling {

  //Best practice to make a logger static final
  //as it ensures that there is a single logger instance per class.
  //You can create a non-static logger instance in a small application, but this is best practice.
  private static final Logger logger = Logger.getLogger(ErrorHandling.class.getName());

  /**
   * Logs a severe error message indicating that a file was not found or an IO error occurred.
   *
   * @param e the exception that was thrown
   */
  public void fileNotFound(Exception e) {
    logger.log(Level.SEVERE, "File not found or IO error: " + e.getMessage());
  }

  /**
   * Logs a warning message indicating that a file is empty or malformed.
   *
   * @param e the exception that was thrown
   */
  public void fileIsEmpty(Exception e) {
    logger.log(Level.WARNING, "File is empty or malformed: " + e.getMessage());
  }

  /**
   * Logs a severe error message indicating that an unexpected error occurred.
   *
   * @param e the exception that was thrown
   */
  public void error(Exception e) {
    logger.log(Level.SEVERE, "An unexpected error occurred: " + e.getMessage());
  }

  /**
   * Logs a warning message indicating that a transformation selection failed.
   *
   * @param e the exception that was thrown
   */
  public void failedToSelectTransformation(Exception e) {
    logger.log(Level.WARNING, "Failed to select transformation: " + e.getMessage());
  }

  /**
   * Logs a warning message indicating that copying the last transformation failed.
   *
   * @param e the exception that was thrown
   */
  public void failedToCopyLastTransformation(Exception e) {
    logger.log(Level.WARNING, "Failed to copy last transformation: " + e.getMessage());
  }

  /**
   * Logs a warning message indicating that making a fractal with iterative transformation failed.
   *
   * @param e the exception that was thrown
   */
  public void failedToMakeFractalWithIterativeTransformation(Exception e) {
    logger.log(Level.WARNING, "Failed to make fractal with iterative transformation: " + e.getMessage());
  }
}
