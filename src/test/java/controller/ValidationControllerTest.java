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
 * @version v1.0.0
 */
public class ValidationControllerTest {

  private ValidationController validationController;

  @BeforeAll
  public static void initToolkit() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    Platform.startup(latch::countDown);
    if (!latch.await(5, TimeUnit.SECONDS)) {
      throw new IllegalStateException("JavaFX platform did not initialize");
    }
  }

  /**
   * Sets up the test environment before each test.
   * It initializes the ValidationController.
   */
  @BeforeEach
  public void setUp() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> {
      validationController = new ValidationController();
      latch.countDown();
    });
    if (!latch.await(5, TimeUnit.SECONDS)) {
      throw new IllegalStateException("JavaFX platform did not initialize");
    }
  }

  /**
   * Tests validation of all fields with valid inputs.
   * It verifies that all fields are validated correctly.
   */
  @Test
  void testIsAllFieldsValid_Valid() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> {
      try {
        GridPane juliaGrid = new GridPane();
        GridPane affineGrid = new GridPane();
        GridPane coordGrid = new GridPane();
        ToggleGroup transformationsGroup = new ToggleGroup();

        // Add valid test data to the grids and toggle group
        addTestDataToJuliaGrid(juliaGrid, "1.0");
        addTestDataToAffineGrid(affineGrid, "1.0", "2.0", "3.0", "4.0", "5.0", "6.0");
        addTestDataToCoordGrid(coordGrid, "0.0");
        addTestDataToTransformationsGroup(transformationsGroup);

        assertTrue(validationController.isAllFieldsValid(juliaGrid, transformationsGroup, affineGrid, coordGrid));
      } finally {
        latch.countDown();
      }
    });
    if (!latch.await(5, TimeUnit.SECONDS)) {
      throw new IllegalStateException("Test did not complete in time");
    }
  }

  /**
   * Tests validation of all fields with invalid inputs.
   * It verifies that invalid fields are detected correctly.
   */
  @Test
  void testIsAllFieldsValid_Invalid() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> {
      try {
        GridPane juliaGrid = new GridPane();
        GridPane affineGrid = new GridPane();
        GridPane coordGrid = new GridPane();
        ToggleGroup transformationsGroup = new ToggleGroup();

        // Add invalid test data to the grids and toggle group
        addTestDataToJuliaGrid(juliaGrid, "");
        addTestDataToAffineGrid(affineGrid, "1.0", "2.0", "", "4.0", "5.0", "6.0");
        addTestDataToCoordGrid(coordGrid, "");
        addTestDataToTransformationsGroup(transformationsGroup);

        assertFalse(validationController.isAllFieldsValid(juliaGrid, transformationsGroup, affineGrid, coordGrid));
      } finally {
        latch.countDown();
      }
    });
    if (!latch.await(5, TimeUnit.SECONDS)) {
      throw new IllegalStateException("Test did not complete in time");
    }
  }

  /**
   * Tests checking for empty fields with valid inputs.
   * It verifies that no missing fields are detected.
   */
  @Test
  void testCheckForEmptyFields_Valid() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> {
      try {
        GridPane affineGrid = new GridPane();
        ToggleGroup transformationsGroup = new ToggleGroup();
        TextField realPartField = new TextField("1.0");
        TextField imaginaryPartField = new TextField("2.0");

        // Add valid test data to the affine grid and toggle group
        addTestDataToAffineGrid(affineGrid, "1.0", "2.0", "3.0", "4.0", "5.0", "6.0");
        addTestDataToTransformationsGroup(transformationsGroup);

        List<String> missingFields = validationController.checkForEmptyFields(transformationsGroup, affineGrid, realPartField, imaginaryPartField);
        assertTrue(missingFields.isEmpty());
      } finally {
        latch.countDown();
      }
    });
    if (!latch.await(5, TimeUnit.SECONDS)) {
      throw new IllegalStateException("Test did not complete in time");
    }
  }

  /**
   * Tests checking for empty fields with invalid inputs.
   * It verifies that missing fields are detected correctly.
   */
  @Test
  void testCheckForEmptyFields_Invalid() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> {
      try {
        GridPane affineGrid = new GridPane();
        ToggleGroup transformationsGroup = new ToggleGroup();
        TextField realPartField = new TextField("");
        TextField imaginaryPartField = new TextField("2.0");

        // Add invalid test data to the affine grid and toggle group
        addTestDataToAffineGrid(affineGrid, "1.0", "2.0", "", "4.0", "5.0", "6.0");
        addTestDataToTransformationsGroup(transformationsGroup);

        List<String> missingFields = validationController.checkForEmptyFields(transformationsGroup, affineGrid, realPartField, imaginaryPartField);
        assertFalse(missingFields.isEmpty());
      } finally {
        latch.countDown();
      }
    });
    if (!latch.await(5, TimeUnit.SECONDS)) {
      throw new IllegalStateException("Test did not complete in time");
    }
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

  /**
   * Tests validating coordinate fields with valid inputs.
   * It verifies that the coordinate fields are validated correctly.
   */
  @Test
  void testValidateCoordFieldsAndSetStyle_Valid() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> {
      try {
        TextField field1 = new TextField("1.0");
        TextField field2 = new TextField("2.0");

        boolean allFieldsValid = validationController.validateCoordFieldsAndSetStyle(field1, field2, true);
        assertTrue(allFieldsValid);
        assertEquals("", field1.getStyle());
        assertEquals("", field2.getStyle());
      } finally {
        latch.countDown();
      }
    });
    if (!latch.await(5, TimeUnit.SECONDS)) {
      throw new IllegalStateException("Test did not complete in time");
    }
  }

  /**
   * Tests validating coordinate fields with invalid inputs.
   * It verifies that invalid coordinate fields are detected correctly.
   */
  @Test
  void testValidateCoordFieldsAndSetStyle_Invalid() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> {
      try {
        TextField field1 = new TextField("");
        TextField field2 = new TextField("2.0");

        boolean allFieldsValid = validationController.validateCoordFieldsAndSetStyle(field1, field2, true);
        assertFalse(allFieldsValid);
        assertEquals(Utility.RED_BORDER, field1.getStyle());
        assertEquals(Utility.RED_BORDER, field2.getStyle());
      } finally {
        latch.countDown();
      }
    });
    if (!latch.await(5, TimeUnit.SECONDS)) {
      throw new IllegalStateException("Test did not complete in time");
    }
  }

  /**
   * Adds test data to the Julia grid.
   *
   * @param juliaGrid the GridPane to add the test data to
   * @param realPart  the value for the real part field
   */
  private void addTestDataToJuliaGrid(GridPane juliaGrid, String realPart) {
    TextField realPartField = new TextField(realPart);
    TextField imaginaryPartField = new TextField("2.0");
    juliaGrid.add(realPartField, 0, 0); // Adding to row 0, column 0
    juliaGrid.add(imaginaryPartField, 1, 0); // Adding to row 0, column 1
  }

  /**
   * Adds test data to the affine grid.
   * @param affineGrid the GridPane to add the test data to
   * @param values the values to add to the grid
   */
  private void addTestDataToAffineGrid(GridPane affineGrid, String... values) {
    int index = 0;
    for (int row = 0; row < 2; row++) { // Adjust the row count as needed
      for (int col = 0; col < 7; col++) { // Adjust the column count as needed
        TextField textField = new TextField(values[index++]);
        affineGrid.add(textField, col, row);
      }
    }
  }

  /**
   * Adds test data to the coordinate grid.
   *
   * @param coordGrid the GridPane to add the test data to
   * @param minY      the value for the min Y field
   */
  private void addTestDataToCoordGrid(GridPane coordGrid, String minY) {
    TextField minXField = new TextField("0.0");
    TextField minYField = new TextField(minY);
    TextField maxXField = new TextField("10.0");
    TextField maxYField = new TextField("10.0");
    coordGrid.add(minXField, 0, 0);
    coordGrid.add(minYField, 1, 0);
    coordGrid.add(maxXField, 2, 0);
    coordGrid.add(maxYField, 3, 0);
  }

  /**
   * Adds test data to the transformations group.
   *
   * @param transformationsGroup the ToggleGroup to add the test data to
   */
  private void addTestDataToTransformationsGroup(ToggleGroup transformationsGroup) {
    RadioButton affineButton = new RadioButton("Affine");
    RadioButton juliaButton = new RadioButton("Julia");
    transformationsGroup.getToggles().addAll(affineButton, juliaButton);

    if ("Affine".equals("Affine")) {
      transformationsGroup.selectToggle(affineButton);
    } else if ("Julia".equals("Affine")) {
      transformationsGroup.selectToggle(juliaButton);
    }
  }
}
