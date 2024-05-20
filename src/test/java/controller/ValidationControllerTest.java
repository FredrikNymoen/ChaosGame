package controller;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.UIHelper;
import util.Utility;

import java.util.List;

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

  /**
   * Sets up the test environment before each test.
   * It initializes the ValidationController.
   */
  @BeforeEach
  public void setUp() {
    validationController = new ValidationController();
  }

  /**
   * Tests validation of all fields with valid inputs.
   * It verifies that all fields are validated correctly.
   */
  @Test
  public void testIsAllFieldsValid_Valid() {
    GridPane juliaGrid = new GridPane();
    GridPane affineGrid = new GridPane();
    GridPane coordGrid = new GridPane();
    ToggleGroup transformationsGroup = new ToggleGroup();

    // Add valid test data to the grids and toggle group
    addTestDataToJuliaGrid(juliaGrid, "1.0", "2.0");
    addTestDataToAffineGrid(affineGrid, "1.0", "2.0", "3.0", "4.0", "5.0", "6.0");
    addTestDataToCoordGrid(coordGrid, "0.0", "0.0", "10.0", "10.0");
    addTestDataToTransformationsGroup(transformationsGroup, "Affine");

    assertTrue(validationController.isAllFieldsValid(juliaGrid, transformationsGroup, affineGrid, coordGrid));
  }

  /**
   * Tests validation of all fields with invalid inputs.
   * It verifies that invalid fields are detected correctly.
   */
  @Test
  public void testIsAllFieldsValid_Invalid() {
    GridPane juliaGrid = new GridPane();
    GridPane affineGrid = new GridPane();
    GridPane coordGrid = new GridPane();
    ToggleGroup transformationsGroup = new ToggleGroup();

    // Add invalid test data to the grids and toggle group
    addTestDataToJuliaGrid(juliaGrid, "", "2.0");
    addTestDataToAffineGrid(affineGrid, "1.0", "2.0", "", "4.0", "5.0", "6.0");
    addTestDataToCoordGrid(coordGrid, "0.0", "", "10.0", "10.0");
    addTestDataToTransformationsGroup(transformationsGroup, "Affine");

    assertFalse(validationController.isAllFieldsValid(juliaGrid, transformationsGroup, affineGrid, coordGrid));
  }

  /**
   * Tests checking for empty fields with valid inputs.
   * It verifies that no missing fields are detected.
   */
  @Test
  public void testCheckForEmptyFields_Valid() {
    GridPane affineGrid = new GridPane();
    ToggleGroup transformationsGroup = new ToggleGroup();
    TextField realPartField = new TextField("1.0");
    TextField imaginaryPartField = new TextField("2.0");

    // Add valid test data to the affine grid and toggle group
    addTestDataToAffineGrid(affineGrid, "1.0", "2.0", "3.0", "4.0", "5.0", "6.0");
    addTestDataToTransformationsGroup(transformationsGroup, "Affine");

    List<String> missingFields = validationController.checkForEmptyFields(transformationsGroup, affineGrid, realPartField, imaginaryPartField);
    assertTrue(missingFields.isEmpty());
  }

  /**
   * Tests checking for empty fields with invalid inputs.
   * It verifies that missing fields are detected correctly.
   */
  @Test
  public void testCheckForEmptyFields_Invalid() {
    GridPane affineGrid = new GridPane();
    ToggleGroup transformationsGroup = new ToggleGroup();
    TextField realPartField = new TextField("");
    TextField imaginaryPartField = new TextField("2.0");

    // Add invalid test data to the affine grid and toggle group
    addTestDataToAffineGrid(affineGrid, "1.0", "2.0", "", "4.0", "5.0", "6.0");
    addTestDataToTransformationsGroup(transformationsGroup, "Affine");

    List<String> missingFields = validationController.checkForEmptyFields(transformationsGroup, affineGrid, realPartField, imaginaryPartField);
    assertFalse(missingFields.isEmpty());
  }

  /**
   * Tests checking if a string can be parsed to a double.
   * It verifies that valid strings are parsed correctly.
   */
  @Test
  public void testIsDouble_Valid() {
    assertTrue(validationController.isDouble("123.45"));
    assertTrue(validationController.isDouble("-123.45"));
    assertTrue(validationController.isDouble("0.0"));
  }

  /**
   * Tests checking if a string can be parsed to a double.
   * It verifies that invalid strings are not parsed as doubles.
   */
  @Test
  public void testIsDouble_Invalid() {
    assertFalse(validationController.isDouble("abc"));
    assertFalse(validationController.isDouble(""));
    assertFalse(validationController.isDouble("123.45.67"));
  }

  /**
   * Tests validating coordinate fields with valid inputs.
   * It verifies that the coordinate fields are validated correctly.
   */
  @Test
  public void testValidateCoordFieldsAndSetStyle_Valid() {
    TextField field1 = new TextField("1.0");
    TextField field2 = new TextField("2.0");

    boolean allFieldsValid = validationController.validateCoordFieldsAndSetStyle(field1, field2, true);
    assertTrue(allFieldsValid);
    assertEquals("", field1.getStyle());
    assertEquals("", field2.getStyle());
  }

  /**
   * Tests validating coordinate fields with invalid inputs.
   * It verifies that invalid coordinate fields are detected correctly.
   */
  @Test
  public void testValidateCoordFieldsAndSetStyle_Invalid() {
    TextField field1 = new TextField("");
    TextField field2 = new TextField("2.0");

    boolean allFieldsValid = validationController.validateCoordFieldsAndSetStyle(field1, field2, true);
    assertFalse(allFieldsValid);
    assertEquals(Utility.RED_BORDER, field1.getStyle());
    assertEquals(Utility.RED_BORDER, field2.getStyle());
  }

  /**
   * Adds test data to the Julia grid.
   * @param juliaGrid the GridPane to add the test data to
   * @param realPart the value for the real part field
   * @param imaginaryPart the value for the imaginary part field
   */
  private void addTestDataToJuliaGrid(GridPane juliaGrid, String realPart, String imaginaryPart) {
    TextField realPartField = new TextField(realPart);
    TextField imaginaryPartField = new TextField(imaginaryPart);
    juliaGrid.add(realPartField, 0, 2);
    juliaGrid.add(imaginaryPartField, 1, 2);
  }

  /**
   * Adds test data to the affine grid.
   * @param affineGrid the GridPane to add the test data to
   * @param values the values to add to the grid
   */
  private void addTestDataToAffineGrid(GridPane affineGrid, String... values) {
    int index = 0;
    for (int row = 0; row < affineGrid.getRowCount(); row++) {
      for (int col = 0; col < affineGrid.getColumnCount(); col++) {
        TextField textField = new TextField(values[index++]);
        affineGrid.add(textField, col, row);
      }
    }
  }

  /**
   * Adds test data to the coordinate grid.
   * @param coordGrid the GridPane to add the test data to
   * @param minX the value for the min X field
   * @param minY the value for the min Y field
   * @param maxX the value for the max X field
   * @param maxY the value for the max Y field
   */
  private void addTestDataToCoordGrid(GridPane coordGrid, String minX, String minY, String maxX, String maxY) {
    TextField minXField = new TextField(minX);
    TextField minYField = new TextField(minY);
    TextField maxXField = new TextField(maxX);
    TextField maxYField = new TextField(maxY);
    coordGrid.add(minXField, 0, 0);
    coordGrid.add(minYField, 1, 0);
    coordGrid.add(maxXField, 2, 0);
    coordGrid.add(maxYField, 3, 0);
  }

  /**
   * Adds test data to the transformations group.
   * @param transformationsGroup the ToggleGroup to add the test data to
   * @param selectedOption the option to select in the toggle group
   */
  private void addTestDataToTransformationsGroup(ToggleGroup transformationsGroup, String selectedOption) {
    RadioButton affineButton = new RadioButton("Affine");
    RadioButton juliaButton = new RadioButton("Julia");
    transformationsGroup.getToggles().addAll(affineButton, juliaButton);

    if ("Affine".equals(selectedOption)) {
      transformationsGroup.selectToggle(affineButton);
    } else if ("Julia".equals(selectedOption)) {
      transformationsGroup.selectToggle(juliaButton);
    }
  }
}
