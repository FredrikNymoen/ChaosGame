package util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ErrorHandling {
  private Logger logger = Logger.getLogger(ErrorHandling.class.getName());
  public void fileNotFound(Exception e) {
    logger.log(Level.SEVERE, "File not found or IO error: {0}", e.getMessage());
  }

  public void fileIsEmpty(Exception e) {
    logger.log(Level.WARNING, "File is empty or malformed: {0}", e.getMessage());
  }

  public void error(Exception e) {
    logger.log(Level.SEVERE, "An unexpected error occurred: {0}", e.getMessage());
  }

  public void failedToSelectTransformation(Exception e) {
    logger.log(Level.WARNING, "Failed to select transformation: {0}", e.getMessage());
  }

  public void failedToCopyLastTransformation(Exception e) {
    logger.log(Level.WARNING, "Failed to copy last transformation: {0}", e.getMessage());
  }

  public void failedToMakeFractalWithIterativeTransformation(Exception e) {
    logger.log(Level.WARNING, "Failed to make fractal with iterative transformation: {0}", e.getMessage());
  }

}
