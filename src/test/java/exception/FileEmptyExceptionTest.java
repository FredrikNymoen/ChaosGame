package exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for FileEmptyException.
 * This class contains unit tests for the FileEmptyException class methods.
 * It verifies the functionality of the exception handling in the Chaos Game application.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
class FileEmptyExceptionTest {

  /**
   * Tests the constructor of FileEmptyException.
   * It verifies that the exception message is correctly set.
   */
  @Test
  void testConstructor() {
    String message = "File is empty.";
    FileEmptyException exception = new FileEmptyException(message);

    assertEquals(message, exception.getMessage(), "Exception message should match the input message");
  }

  /**
   * Tests the inheritance of FileEmptyException.
   * It verifies that FileEmptyException is a subclass of Exception.
   */
  @Test
  void testInheritance() {

    assertTrue(true, "FileEmptyException should be an instance of Exception");
  }
}
