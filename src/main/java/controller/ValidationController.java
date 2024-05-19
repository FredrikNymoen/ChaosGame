package controller;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import util.UIHelper;
import util.Utility;

public class ValidationController {

  public boolean isAllFieldsValid(GridPane juliaGrid, ToggleGroup transformationsGroup, GridPane affineGrid, GridPane coordGrid) {
    boolean allFieldsValid = true;

    TextField[] coordinateFieldsJulia = UIHelper.getJuliaTextFields(juliaGrid);
    TextField realPartField = coordinateFieldsJulia[0];
    TextField imaginaryPartField = coordinateFieldsJulia[1];

    List<String> missingInputs = checkForEmptyFields(transformationsGroup,
        affineGrid, realPartField, imaginaryPartField);

    // Handle each case of missing inputs to update the GUI
    for (String notFilled : missingInputs) {
      allFieldsValid = false; // Mark as invalid since there's an error
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

    // Validate and parse minimum coordinates
    allFieldsValid &= validateCoordFieldsAndSetStyle(minXField, minYField, allFieldsValid);
    allFieldsValid &= validateCoordFieldsAndSetStyle(maxXField, maxYField, allFieldsValid);

    return allFieldsValid;
  }


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


  public boolean isDouble(String text) {
    try {
      Double.parseDouble(text); // Try to parse the text to a double
      return true; // Parsing succeeded, so it's a valid double
    } catch (NumberFormatException e) {
      return false; // Parsing failed, it's not a valid double
    }
  }

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
