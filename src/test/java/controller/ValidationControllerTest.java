package controller;

import javafx.application.Platform;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Utility;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ValidationController.
 * This class contains unit tests for the ValidationController class methods.
 * It verifies the functionality of validating user input in the Chaos Game application.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
class ValidationControllerTest {

  private ValidationController validationController;

  /**
   * Sets up the test environment before each test.
   * It initializes the ValidationController.
   */
  @BeforeEach
  public void setUp() throws Exception {
    validationController = new ValidationController();
  }


  /**
   * Tests checking if a string can be parsed to a double.
   * It verifies that valid strings are parsed correctly.
   */
  @Test
  void testIsDouble_Valid() {
    assertTrue(validationController.isDouble("123.45"));
    assertTrue(validationController.isDouble("-123.45"));
    assertTrue(validationController.isDouble("0.0"));
  }

  /**
   * Tests checking if a string can be parsed to a double.
   * It verifies that invalid strings are not parsed as doubles.
   */
  @Test
  void testIsDouble_Invalid() {
    assertFalse(validationController.isDouble("abc"));
    assertFalse(validationController.isDouble(""));
    assertFalse(validationController.isDouble("123.45.67"));
  }


}
