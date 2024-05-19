package util;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class UIHelper {
  public static TextField[] getCoordinateTextFields(GridPane coordGrid){
    TextField minXField = ((TextField) getNodeFromGridPane(coordGrid, 0, 1));
    TextField minYField = ((TextField) getNodeFromGridPane(coordGrid, 1, 1));
    TextField maxXField = ((TextField) getNodeFromGridPane(coordGrid, 2, 1));
    TextField maxYField = ((TextField) getNodeFromGridPane(coordGrid, 3, 1));
    return new TextField[]{minXField, minYField, maxXField, maxYField};
  }

  public static TextField[] getJuliaTextFields(GridPane juliaGrid){
    TextField realPartField = ((TextField) getNodeFromGridPane(juliaGrid, 0, 2));
    TextField imaginaryPartField = ((TextField) getNodeFromGridPane(juliaGrid, 1, 2));
    return new TextField[]{realPartField, imaginaryPartField};
  }

  public static Map<String, TextField> getTextFieldsCoordAndJuliaMap(GridPane coordGrid, GridPane juliaGrid) {
    Map<String, TextField> fields = new HashMap<>();

    TextField[] coordinateFields = getCoordinateTextFields(coordGrid);
    fields.put("minXField", coordinateFields[0]);
    fields.put("minYField", coordinateFields[1]);
    fields.put("maxXField", coordinateFields[2]);
    fields.put("maxYField", coordinateFields[3]);

    TextField[] coordinateFieldsJulia = getJuliaTextFields(juliaGrid);
    fields.put("realPartField", coordinateFieldsJulia[0]);
    fields.put("imaginaryPartField", coordinateFieldsJulia[1]);

    return fields;
  }

  /**
   * Gets a node from a GridPane at a specified column and row. The method iterates through the
   * children of the GridPane and returns the node at the specified column and row. If no node is
   * found, the method returns null.
   *
   * @param gridPane the GridPane to get the node from
   * @param col      the column of the node
   * @param row      the row of the node
   * @return Node the node at the specified column and row
   */

  public static Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
    for (Node node : gridPane.getChildren()) {
      if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
        return node;
      }
    }
    return null;
  }
}
