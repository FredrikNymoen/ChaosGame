package util;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UIHelper.
 * This class tests the utility methods for interacting with GridPane and TextField elements.
 *
 * @author Amund Larsen & Fredrik Nymoen
 * @version v1.0.0
 */
class UIHelperTest extends ApplicationTest {

  private GridPane coordGrid;
  private GridPane juliaGrid;

  /**
   * Sets up the test environment by initializing the GridPanes and adding TextFields.
   */
  @BeforeEach
  void setUp() {
    coordGrid = new GridPane();
    juliaGrid = new GridPane();

    // Setup coordGrid with TextFields at specific positions
    TextField minXField = new TextField();
    TextField minYField = new TextField();
    TextField maxXField = new TextField();
    TextField maxYField = new TextField();
    coordGrid.add(minXField, 0, 1);
    coordGrid.add(minYField, 1, 1);
    coordGrid.add(maxXField, 2, 1);
    coordGrid.add(maxYField, 3, 1);

    // Setup juliaGrid with TextFields at specific positions
    TextField realPartField = new TextField();
    TextField imaginaryPartField = new TextField();
    juliaGrid.add(realPartField, 0, 2);
    juliaGrid.add(imaginaryPartField, 1, 2);
  }

  /**
   * Tests the {@link UIHelper#getCoordinateTextFields(GridPane)} method.
   * Verifies that the correct TextField instances are retrieved from the coordGrid.
   */
  @Test
  void testGetCoordinateTextFields() {
    TextField[] fields = UIHelper.getCoordinateTextFields(coordGrid);

    assertEquals(4, fields.length);
    assertSame(coordGrid.getChildren().get(0), fields[0]);
    assertSame(coordGrid.getChildren().get(1), fields[1]);
    assertSame(coordGrid.getChildren().get(2), fields[2]);
    assertSame(coordGrid.getChildren().get(3), fields[3]);
  }

  /**
   * Tests the {@link UIHelper#getJuliaTextFields(GridPane)} method.
   * Verifies that the correct TextField instances are retrieved from the juliaGrid.
   */
  @Test
  void testGetJuliaTextFields() {
    TextField[] fields = UIHelper.getJuliaTextFields(juliaGrid);

    assertEquals(2, fields.length);
    assertSame(juliaGrid.getChildren().get(0), fields[0]);
    assertSame(juliaGrid.getChildren().get(1), fields[1]);
  }

  /**
   * Tests the {@link UIHelper#getTextFieldsCoordAndJuliaMap(GridPane, GridPane)} method.
   * Verifies that the correct TextField instances are retrieved and mapped from both coordGrid and juliaGrid.
   */
  @Test
  void testGetTextFieldsCoordAndJuliaMap() {
    Map<String, TextField> fieldsMap = UIHelper.getTextFieldsCoordAndJuliaMap(coordGrid, juliaGrid);

    assertEquals(6, fieldsMap.size());
    assertSame(coordGrid.getChildren().get(0), fieldsMap.get("minXField"));
    assertSame(coordGrid.getChildren().get(1), fieldsMap.get("minYField"));
    assertSame(coordGrid.getChildren().get(2), fieldsMap.get("maxXField"));
    assertSame(coordGrid.getChildren().get(3), fieldsMap.get("maxYField"));
    assertSame(juliaGrid.getChildren().get(0), fieldsMap.get("realPartField"));
    assertSame(juliaGrid.getChildren().get(1), fieldsMap.get("imaginaryPartField"));
  }

  /**
   * Tests the {@link UIHelper#getNodeFromGridPane(GridPane, int, int)} method.
   * Verifies that the correct Node instances are retrieved from the coordGrid and that null is returned when no node is found.
   */
  @Test
  void testGetNodeFromGridPane() {
    Node minXField = coordGrid.getChildren().get(0);
    Node minYField = coordGrid.getChildren().get(1);
    Node maxXField = coordGrid.getChildren().get(2);
    Node maxYField = coordGrid.getChildren().get(3);

    assertSame(minXField, UIHelper.getNodeFromGridPane(coordGrid, 0, 1));
    assertSame(minYField, UIHelper.getNodeFromGridPane(coordGrid, 1, 1));
    assertSame(maxXField, UIHelper.getNodeFromGridPane(coordGrid, 2, 1));
    assertSame(maxYField, UIHelper.getNodeFromGridPane(coordGrid, 3, 1));

    assertNull(UIHelper.getNodeFromGridPane(coordGrid, 0, 0)); // No node at this position
    assertNull(UIHelper.getNodeFromGridPane(coordGrid, 4, 1)); // No node at this position
  }
}
