package controller;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import util.UIHelper;
import util.Utility;

/**
 * ValidationController class is used to validate user input in the GUI.
 * The class checks if all fields are valid and updates the GUI accordingly.
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class ValidationController {

  /**
   * Checks if all fields are valid and updates the GUI accordingly.
   * @param juliaGrid the GridPane containing the Julia set fields
   * @param transformationsGroup the ToggleGroup containing the transformation options
   * @param affineGrid the GridPane containing the affine transformation fields
   * @param coordGrid the GridPane containing the coordinate fields
   * @return true if all fields are valid, false otherwise
   */
  public boolean isAllFieldsValid(GridPane juliaGrid, ToggleGroup transformationsGroup, GridPane affineGrid, GridPane coordGrid) {
    boolean allFieldsValid = true;

    TextField[] coordinateFieldsJulia = UIHelper.getJuliaTextFields(juliaGrid);
    TextField realPartField = coordinateFieldsJulia[0];
    TextField imaginaryPartField = coordinateFieldsJulia[1];

    List<String> missingInputs = checkForEmptyFields(transformationsGroup,
        affineGrid, realPartField, imaginaryPartField);

    // Handle each case of missing inputs to update the GUI
    for (String notFilled : missingInputs) {
      allFieldsValid = false; // At least one field is not valid
      if (notFilled.startsWith("(")){
        String[] parts = notFilled.split("[, ]+");
        int row = Integer.parseInt(parts[0].substring(1));
        int col = Integer.parseInt(parts[1].substring(0, parts[1].length() - 1));

        TextField textField = (TextField) UIHelper.getNodeFromGridPane(affineGrid, col, row);
        textField.setStyle(Utility.RED_BORDER);
      } else {
        switch (notFilled) {
          case "Real part":
            realPartField.setStyle(Utility.RED_BORDER);
            break;
          case "Imaginary part":
            imaginaryPartField.setStyle(Utility.RED_BORDER);
            break;
        }
      }
    }

    TextField[] coordinateFields = UIHelper.getCoordinateTextFields(coordGrid);
    TextField minXField = coordinateFields[0];
    TextField minYField = coordinateFields[1];
    TextField maxXField = coordinateFields[2];
    TextField maxYField = coordinateFields[3];

    // Validate and parse the coordinate fields
    allFieldsValid &= validateCoordFieldsAndSetStyle(minXField, minYField, allFieldsValid);
    allFieldsValid &= validateCoordFieldsAndSetStyle(maxXField, maxYField, allFieldsValid);

    return allFieldsValid;
  }


  /**
   * Checks for empty fields in the GUI and returns a list of missing fields.
   * @param transformationsGroup the ToggleGroup containing the transformation options
   * @param affineGrid the GridPane containing the affine transformation fields
   * @param realPartField the TextField containing the real part of the Julia set
   * @param imaginaryPartField the TextField containing the imaginary part of the Julia set
   * @return a list of missing fields
   */
  public List<String> checkForEmptyFields(ToggleGroup transformationsGroup, GridPane affineGrid,
      TextField realPartField, TextField imaginaryPartField) {
    List<String> missingArray = new ArrayList<>();
    RadioButton selectedButton = (RadioButton) transformationsGroup.getSelectedToggle();
    if (selectedButton != null) {  // Make sure there is a selected toggle
      switch (selectedButton.getText()) {
        case "Affine":
          for (int row = 0; row < affineGrid.getRowCount(); row++) {
            for (int i = 0; i < 4; i++) {  // Check matrix elements
              TextField textField = (TextField) UIHelper.getNodeFromGridPane(affineGrid, i, row);
              if (!isDouble(textField.getText())) {
                missingArray.add("(" + row + ", " + i + ")");
              }
            }
            for (int i = 5; i < 7; i++) {  // Check vector elements
              TextField textField = (TextField) UIHelper.getNodeFromGridPane(affineGrid, i, row);
              if (!isDouble(textField.getText())) {
                missingArray.add("(" + row + ", " + i + ")");
              }
            }
          }
          break;
        case "Julia":
          if (!isDouble(realPartField.getText())) {
            missingArray.add("Real part");
          }
          if (!isDouble(imaginaryPartField.getText())) {
            missingArray.add("Imaginary part");
          }
          break;
      }
    }
    return missingArray;
  }

  /**
   * Checks if a string can be parsed to a double.
   * @param text the string to check
   * @return true if the string can be parsed to a double, false otherwise
   */
  public boolean isDouble(String text) {
    boolean flag = true;
    try {
      Double.parseDouble(text); // Try to parse the text to a double
    } catch (NumberFormatException e) {
      flag = false;
    }
    return flag;
  }

  /**
   * Validates the coordinate fields and sets the style of the fields accordingly.
   * @param field1 the first coordinate field
   * @param field2 the second coordinate field
   * @param allFieldsValid true if all fields are valid, false otherwise
   * @return true if all fields are valid, false otherwise
   */
  public boolean validateCoordFieldsAndSetStyle(TextField field1, TextField field2, boolean allFieldsValid) {
    if (field1.getText().trim().isEmpty() || field2.getText().trim().isEmpty()) {
      field1.setStyle(Utility.RED_BORDER);
      field2.setStyle(Utility.RED_BORDER);
      allFieldsValid = false;
    } else {
      field1.setStyle("");
      field2.setStyle("");
    }
    return allFieldsValid;
  }

}
