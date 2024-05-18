package controller;

import java.util.HashMap;
import java.util.Map;
import model.chaosGame.ChaosGame;
import model.chaosGame.ChaosGameDescription;
import model.factory.ChaosGameDescriptionFactory;
import model.factory.ChaosGameFactory;
import model.filehandling.ChaosGameFileHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.converter.DoubleStringConverter;
import model.mathcore.Complex;
import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;

/**
 * ChaosGameController class is used to control the chaos game GUI.
 * The class contains methods to get affine transformation values,
 * create a chaos game and handle transformation selection.
 */

public class ChaosGameController {
  private ChaosGameFactory chaosGameFactory;
  public ChaosGameController(){
    chaosGameFactory = new ChaosGameFactory();
  }

  /**
   * Gets the affine transformation values from the affine grid. The affine grid is a GridPane
   * containing text fields for the matrix and vector values. The method retrieves the values from
   * the grid and stores them in lists.
   *
   * @param affineMatrices the list to store the affine matrices
   * @param affineVectors  the list to store the affine vectors
   * @param affineGrid     the grid containing the affine transformation values
   */

  public void getAffineTransformationValues(List<Matrix2x2> affineMatrices,
      List<Vector2D> affineVectors, GridPane affineGrid) {
    // Assuming there are 4 rows, and each row has 4 matrix fields followed by 2 vector fields
    for (int row = 0; row < affineGrid.getRowCount(); row++) {
      double[] matrixValues = new double[4]; // To store a00, a01, a10, a11
      double[] vectorValues = new double[2]; // To store x0, y0

      // Retrieve matrix values
      for (int i = 0; i < 4; i++) { // matrixValues indexes are 0 to 3
        TextField textField = (TextField) getNodeFromGridPane(affineGrid, i, row);
        try {
          matrixValues[i] = Double.parseDouble(textField.getText());
        } catch (NumberFormatException e) {
          System.out.println("Invalid input for matrix values.");
        }
      }

      // Retrieve vector values
      for (int i = 0; i < 2; i++) { // vectorValues indexes are 0 to 1, grid positions are 5 and 6
        TextField textField = (TextField) getNodeFromGridPane(affineGrid, i + 5, row);
        try {
          vectorValues[i] = Double.parseDouble(textField.getText());
        } catch (NumberFormatException e) {
          System.out.println("Invalid input for vector values.");
        }
      }

      // Now you have the values for this row in matrixValues and vectorValues
      // Do whatever processing you need with these values
      Matrix2x2 matrix = new Matrix2x2(matrixValues[0], matrixValues[1], matrixValues[2],
          matrixValues[3]);
      Vector2D vector = new Vector2D(vectorValues[0], vectorValues[1]);
      affineMatrices.add(matrix);
      affineVectors.add(vector);
    }
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

  public Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
    for (Node node : gridPane.getChildren()) {
      if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
        return node;
      }
    }
    return null;
  }


  /**
   * Handles the selection of a transformation from a ToggleGroup. The method retrieves the selected
   * RadioButton from the ToggleGroup and creates a ChaosGameDescription based on the selected
   * transformation. The ChaosGameDescription is then written to a file and a ChaosGame object is
   * created.
   *
   * @param transformationsGroup the ToggleGroup containing the transformation selection
   * @param affineGrid           the GridPane containing the affine transformation values
   * @param steps                the number of steps to run the chaos game
   * @return ChaosGame the created chaos game
   */


  public ChaosGame handleTransformationSelection(ToggleGroup transformationsGroup,
      GridPane affineGrid, GridPane juliaGrid, GridPane coordGrid, int steps) {
    ChaosGameDescriptionFactory factory = new ChaosGameDescriptionFactory();
    ChaosGameFileHandler fileHandler = new ChaosGameFileHandler();
    ChaosGameDescription description = null;
    Complex c = null;

    TextField[] coordFields = getCoordinateTextFields(coordGrid);
    TextField minXField = coordFields[0];
    TextField minYField = coordFields[1];
    TextField maxXField = coordFields[2];
    TextField maxYField = coordFields[3];
    Vector2D minCoords = new Vector2D(Double.parseDouble(minXField.getText()),
        Double.parseDouble(minYField.getText()));
    Vector2D maxCoords = new Vector2D(Double.parseDouble(maxXField.getText()),
        Double.parseDouble(maxYField.getText()));

    RadioButton selectedButton = (RadioButton) transformationsGroup.getSelectedToggle();
    if (selectedButton != null) {
      switch (selectedButton.getText()) {
        case "Affine":
          List<Matrix2x2> matrices = new ArrayList<>();
          List<Vector2D> vectors = new ArrayList<>();
          getAffineTransformationValues(matrices, vectors, affineGrid);
          description = factory.affine(matrices, vectors, minCoords, maxCoords);
          fileHandler.writeToFile(description, "file.csv", "affine");
          break;
        case "Julia":
          TextField[] juliaFields = getJuliaTextFields(juliaGrid);
          double realPart = Double.parseDouble(juliaFields[0].getText());
          double imaginaryPart = Double.parseDouble(juliaFields[1].getText());
          c = new Complex(realPart, imaginaryPart);
          description = factory.julia(minCoords, maxCoords, c);
          break;
        case "Sierpinski":
          description = factory.sierpinski(minCoords, maxCoords);
          fileHandler.writeToFile(description, "file.csv", "sierpinski");
          break;
        case "Barnsley":
          description = factory.barnsley(minCoords, maxCoords);
          fileHandler.writeToFile(description, "file.csv", "barnsley");
          break;
        case "Maple-Tree":
          description = factory.mapleTree(minCoords, maxCoords);
          fileHandler.writeToFile(description, "file.csv", "mapleTree");
          break;
      }
    }

    ChaosGame chaosGame = null;
    String transformation = selectedButton.getText();
    boolean isBarnsley = transformation.equals("Barnsley");
    ToggleButton juliaToggleButton = (ToggleButton) getNodeFromGridPane(juliaGrid, 0, 1);
    switch (transformation) {
      case "Julia":
        if (juliaToggleButton.isSelected()) {
          chaosGame = chaosGameFactory.createJuliaChaosGame(c);
          fileHandler.writeToFile(description, "file.csv", "convergence-mode");
        } else {
          chaosGame = chaosGameFactory.createChaosGame(description, 900, 750, steps, isBarnsley);
          fileHandler.writeToFile(description, "file.csv", "steps-mode");
        }
        break;
      case "Mandelbrot":
        chaosGame = chaosGameFactory.createMandelbrotChaosGame();
        fileHandler.writeLineToFile("file.csv", "Mandelbrot");
        break;
      default:
        chaosGame = chaosGameFactory.createChaosGame(description, 900, 750, steps, isBarnsley);
        break;
    }

    return chaosGame;
  }


  public TextField createDecimalTextField(String defaultValue) {
    TextField textField = new TextField(defaultValue);
    UnaryOperator<Change> decimalFilter = change -> change.getControlNewText().matches("-?((\\d*)|(\\d+\\.\\d*))") ? change : null;
    textField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), Double.parseDouble(defaultValue), decimalFilter));
    return textField;
  }

  public HBox createCenteredHBox(Node node) {
    HBox hbox = new HBox(node);
    hbox.setAlignment(Pos.CENTER);
    return hbox;
  }

  public RadioButton createRadioButton(ToggleGroup toggleGroup, String label) {
    RadioButton radioButton = new RadioButton(label);
    radioButton.setUserData(label);
    radioButton.setToggleGroup(toggleGroup);
    return radioButton;
  }

  public TextField[] getCoordinateTextFields(GridPane coordGrid){
    TextField minXField = ((TextField) getNodeFromGridPane(coordGrid, 0, 1));
    TextField minYField = ((TextField) getNodeFromGridPane(coordGrid, 1, 1));
    TextField maxXField = ((TextField) getNodeFromGridPane(coordGrid, 2, 1));
    TextField maxYField = ((TextField) getNodeFromGridPane(coordGrid, 3, 1));
    return new TextField[]{minXField, minYField, maxXField, maxYField};
  }

  public TextField[] getJuliaTextFields(GridPane juliaGrid){
    TextField realPartField = ((TextField) getNodeFromGridPane(juliaGrid, 0, 2));
    TextField imaginaryPartField = ((TextField) getNodeFromGridPane(juliaGrid, 1, 2));
    return new TextField[]{realPartField, imaginaryPartField};
  }

  public Map<String, TextField> getTextFieldsCoordAndJuliaMap(GridPane coordGrid, GridPane juliaGrid) {
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

}
