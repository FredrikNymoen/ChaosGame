package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for ErrorHandling.
 * This class tests the logging functionality of the ErrorHandling class.
 * @author Amund Larsen & Fredrik Nymoen
 * @version v1.0.0
 */
class ErrorHandlingTest {

  private ErrorHandling errorHandling;
  private TestHandler testHandler;

  /**
   * Sets up the test environment.
   * Initializes the {@link ErrorHandling} instance and configures the logger to use a custom {@link TestHandler}.
   */
  @BeforeEach
  void setUp() {
    errorHandling = new ErrorHandling();
    Logger logger = Logger.getLogger(ErrorHandling.class.getName());

    // Remove all handlers to avoid duplicate logs
    LogManager.getLogManager().reset();

    testHandler = new TestHandler();
    logger.addHandler(testHandler);
    logger.setUseParentHandlers(false);
  }

  /**
   * Tests the fileNotFound method.
   * Verifies that a severe log message is produced when a file not found error occurs.
   */
  @Test
  void testFileNotFound() {
    Exception e = new Exception("File not found");
    errorHandling.fileNotFound(e);
    assertLog(Level.SEVERE, "File not found or IO error: File not found");
  }

  /**
   * Tests the fileIsEmpty method.
   * Verifies that a warning log message is produced when a file is empty or malformed.
   */
  @Test
  void testFileIsEmpty() {
    Exception e = new Exception("File is empty");
    errorHandling.fileIsEmpty(e);
    assertLog(Level.WARNING, "File is empty or malformed: File is empty");
  }

  /**
   * Tests the error method.
   * Verifies that a severe log message is produced when an unexpected error occurs.
   */
  @Test
  void testError() {
    Exception e = new Exception("Unexpected error");
    errorHandling.error(e);
    assertLog(Level.SEVERE, "An unexpected error occurred: Unexpected error");
  }

  /**
   * Tests the failedToSelectTransformation method.
   * Verifies that a warning log message is produced when transformation selection fails.
   */
  @Test
  void testFailedToSelectTransformation() {
    Exception e = new Exception("Transformation failed");
    errorHandling.failedToSelectTransformation(e);
    assertLog(Level.WARNING, "Failed to select transformation: Transformation failed");
  }

  /**
   * Tests the failedToCopyLastTransformation method.
   * Verifies that a warning log message is produced when copying the last transformation fails.
   */
  @Test
  void testFailedToCopyLastTransformation() {
    Exception e = new Exception("Copy failed");
    errorHandling.failedToCopyLastTransformation(e);
    assertLog(Level.WARNING, "Failed to copy last transformation: Copy failed");
  }

  /**
   * Tests the failedToMakeFractalWithIterativeTransformation method.
   * Verifies that a warning log message is produced when making a fractal with iterative transformation fails.
   */
  @Test
  void testFailedToMakeFractalWithIterativeTransformation() {
    Exception e = new Exception("Iterative transformation failed");
    errorHandling.failedToMakeFractalWithIterativeTransformation(e);
    assertLog(Level.WARNING, "Failed to make fractal with iterative transformation: Iterative transformation failed");
  }

  /**
   * Asserts that the last log record matches the expected level and message.
   *
   * @param level   the expected log level
   * @param message the expected log message
   */
  void assertLog(Level level, String message) {
    LogRecord record = testHandler.getLastLogRecord();
    assertEquals(record.getLevel(), level,
        "Expected log level: " + level + ", but got: " + record.getLevel());
    assertEquals(record.getMessage(), message,
        "Expected log message: \"" + message + "\", but got: \"" + record.getMessage() + "\"");
  }

  /**
   * A custom log handler that captures the last log record for testing purposes.
   */
  private static class TestHandler extends ConsoleHandler {
    private LogRecord lastLogRecord;

    @Override
    public void publish(LogRecord record) {
      super.publish(record);
      lastLogRecord = record;
    }

    /**
     * Returns the last log record.
     *
     * @return the last log record
     */
    public LogRecord getLastLogRecord() {
      return lastLogRecord;
    }
  }
}
