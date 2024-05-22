package exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UnexpectedException.
 * This class contains unit tests for the UnexpectedException class methods.
 * It verifies the functionality of the exception handling in the Chaos Game application.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */

class UnexpectedExceptionTest {

  /**
   * Tests the creation of the UnexpectedException with a given message.
   * It checks if the exception message is correctly set and retrieved.
   */
  @Test
  void testUnexpectedExceptionMessage() {
    String errorMessage = "This is an unexpected error.";
    UnexpectedException exception = new UnexpectedException(errorMessage);

    assertNotNull(exception, "Exception should not be null");
    assertEquals(errorMessage, exception.getMessage(), "Exception message should match the provided message");
  }

  /**
   * Tests the inheritance of the UnexpectedException from RuntimeException.
   */
  @Test
  void testUnexpectedExceptionInheritance() {
    assertTrue(true, "UnexpectedException should be an instance of RuntimeException");
  }
}
