package gui;

import chaosGame.ChaosGame;
import chaosGame.ChaosGameDescription;
import factory.ChaosGameDescriptionFactory;
import filehandling.ChaosGameFileHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import mathcore.Complex;
import mathcore.Matrix2x2;
import mathcore.Vector2D;

public class ChaosGameController {

  public void getAffineTransformationValues(List<Matrix2x2> affineMatrices, List<Vector2D> affineVectors, GridPane affineGrid) {
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
      System.out.println("Matrix Values: " + Arrays.toString(matrixValues));
      System.out.println("Vector Values: " + Arrays.toString(vectorValues));
      Matrix2x2 matrix = new Matrix2x2(matrixValues[0], matrixValues[1], matrixValues[2], matrixValues[3]);
      Vector2D vector = new Vector2D(vectorValues[0], vectorValues[1]);
      affineMatrices.add(matrix);
      affineVectors.add(vector);
    }
  }

  public Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
    for (Node node : gridPane.getChildren()) {
      if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
        return node;
      }
    }
    return null;
  }

  // Helper method to create a TextField with placeholder text
  public TextField createTextFieldWithPlaceholder(String placeholder) {
    TextField textField = new TextField();
    textField.setPromptText(placeholder);
    return textField;
  }


  public ChaosGame createChaosGame(ChaosGameDescription description, int width, int height, int steps) {
    ChaosGame chaosGame = new ChaosGame(description, width, height);
    chaosGame.runSteps(steps);
    return chaosGame;
  }


  public ChaosGame handleTransformationSelection(ToggleGroup transformationsGroup, GridPane affineGrid, TextField realPartField, TextField imaginaryPartField, Vector2D minCoords, Vector2D maxCoords, int steps) {
    ChaosGameDescriptionFactory factory = new ChaosGameDescriptionFactory();
    ChaosGameDescription description = null;

    RadioButton selectedButton = (RadioButton) transformationsGroup.getSelectedToggle();
    if (selectedButton != null) {
      switch (selectedButton.getText()) {
        case "Affine":
          List<Matrix2x2> matrices = new ArrayList<>();
          List<Vector2D> vectors = new ArrayList<>();
          getAffineTransformationValues(matrices, vectors, affineGrid);
          description = factory.affine(matrices, vectors, minCoords, maxCoords);
          break;
        case "Julia":
          Complex c = new Complex(Double.parseDouble(realPartField.getText()), Double.parseDouble(imaginaryPartField.getText()));
          description = factory.julia(minCoords, maxCoords, c);
          break;
        case "Sierpinski":
          description = factory.sierpinski(minCoords, maxCoords);
          break;
        case "Barnsley":
          description = factory.barnsley(minCoords, maxCoords);
          break;
      }
    }

    if (description != null) {
      ChaosGameFileHandler fileHandler = new ChaosGameFileHandler();
      fileHandler.writeToFile(description, "file.csv");
      return createChaosGame(description, 900, 750, steps);
    }
    return null;
  }
}
