package gui;

import chaosGame.ChaosCanvas;
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

/**
 * ChaosGameController class is used to control the chaos game GUI.
 * The class contains methods to get affine transformation values,
 * create a chaos game and handle transformation selection.
 */

public class ChaosGameController {

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
      System.out.println("Matrix Values: " + Arrays.toString(matrixValues));
      System.out.println("Vector Values: " + Arrays.toString(vectorValues));
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

  // Helper method to create a TextField with placeholder text

  /**
   * Helper method to create a TextField with placeholder text
   *
   * @param placeholder the placeholder text for the TextField
   * @return TextField the created TextField
   */
  public TextField createTextFieldWithPlaceholder(String placeholder) {
    TextField textField = new TextField();
    textField.setPromptText(placeholder);
    return textField;
  }

  /**
   * Creates a chaos game with a specified description, width, height and number of steps. The
   * method creates a ChaosGame object with the specified parameters and runs the chaos game for the
   * specified number of steps.
   *
   * @param description the description of the chaos game
   * @param width       the width of the canvas
   * @param height      the height of the canvas
   * @param steps       the number of steps to run the chaos game
   * @return ChaosGame the created chaos game
   */


  public ChaosGame createChaosGame(ChaosGameDescription description, int width, int height,
      int steps, boolean isBarnsleyTransformation) {
    ChaosGame chaosGame = new ChaosGame(description, width, height);
    if (isBarnsleyTransformation) {
      chaosGame.runStepsForBarnsley(steps);
    } else {
      chaosGame.runSteps(steps);
    }
    return chaosGame;
  }

  /**
   * Handles the selection of a transformation from a ToggleGroup. The method retrieves the selected
   * RadioButton from the ToggleGroup and creates a ChaosGameDescription based on the selected
   * transformation. The ChaosGameDescription is then written to a file and a ChaosGame object is
   * created.
   *
   * @param transformationsGroup the ToggleGroup containing the transformation selection
   * @param affineGrid           the GridPane containing the affine transformation values
   * @param realPartField        the TextField containing the real part of the complex number for
   *                             the Julia set
   * @param imaginaryPartField   the TextField containing the imaginary part of the complex number
   *                             for the Julia set
   * @param minCoords            the minimum coordinates of the canvas
   * @param maxCoords            the maximum coordinates of the canvas
   * @param steps                the number of steps to run the chaos game
   * @return ChaosGame the created chaos game
   */


  public ChaosGame handleTransformationSelection(ToggleGroup transformationsGroup,
      GridPane affineGrid, TextField realPartField, TextField imaginaryPartField,
      Vector2D minCoords, Vector2D maxCoords, int steps) {
    ChaosGameDescriptionFactory factory = new ChaosGameDescriptionFactory();
    ChaosGameDescription description = null;
    Complex c = null;

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
          c = new Complex(Double.parseDouble(realPartField.getText()),
              Double.parseDouble(imaginaryPartField.getText()));
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

      ChaosGame chaosGame = null;
      if (selectedButton.getText().equals("Julia")) {
        chaosGame = generateJuliaGame(c);
      } else {
        chaosGame = createChaosGame(description, 900, 750, steps,
            selectedButton.getText().equals("Barnsley"));
      }
      return chaosGame;
    }
    return null;
  }

  public ChaosGame generateJuliaGame(Complex c) {
    double modulus = Math.sqrt(
        c.getX0() * c.getX0() + c.getX1() * c.getX1()); // Calculate modulus of c
    double r = Math.sqrt(1 + modulus); // Choose an appropriate escape radius

    ChaosGame chaosGame = new ChaosGame(900, 750);
    ChaosCanvas canvas = chaosGame.getCanvas();

    // Assuming the fractal drawing's size for positioning
    double width = canvas.getCanvasArray()[0].length;
    double height = canvas.getCanvasArray().length;

    // Loop through each pixel on the screen
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
        Vector2D vector = canvas.pixelToCoordinate(new Vector2D(i, j));
        double x = vector.getX0();
        double y = vector.getX1();

        int iteration = 0;
        int maxIterations = 1000; // Maximum iterations for convergence check

        // Iterative escape test
        while (x * x + y * y < r * r && iteration < maxIterations) {
          double xtemp = x * x - y * y;
          y = 2 * x * y + c.getX1();
          x = xtemp + c.getX0();
          iteration++;
        }

        if (iteration == maxIterations) {
          //Vector2D vector = canvas.pixelToCoordinate(new Vector2D(x, y));
          canvas.putPixel(vector);
          //canvas.putPixel(new Vector2D(i, j));
        }
      }
    }

    //chaosGame.setCanvas(canvas);
    return chaosGame;
  }
  public List<String> checkForEmptyFields(ToggleGroup transformationsGroup, GridPane affineGrid,
                                          TextField realPartField, TextField imaginaryPartField, int steps) {
    List<String> missingArray = new ArrayList<>();
    if (steps == 0) {
      missingArray.add("Please fill in the number of steps.");
    }
    RadioButton selectedButton = (RadioButton) transformationsGroup.getSelectedToggle();
    if (selectedButton != null) {  // Make sure there is a selected toggle
      switch (selectedButton.getText()) {
        case "Affine":
          for (int row = 0; row < affineGrid.getRowCount(); row++) {
            for (int i = 0; i < 4; i++) {  // Check matrix elements
              TextField textField = (TextField) getNodeFromGridPane(affineGrid, i, row);
              if (!isDouble(textField.getText())) {
                missingArray.add("Matrix element at (" + row + ", " + i + ") is invalid");
              }
            }
            for (int i = 5; i < 7; i++) {  // Check vector elements
              TextField textField = (TextField) getNodeFromGridPane(affineGrid, i, row);
              if (!isDouble(textField.getText())) {
                missingArray.add("Vector element at (" + row + ", " + i + ") is invalid");
              }
            }
          }
          break;
        case "Julia":
          if (!isDouble(realPartField.getText())) {
            missingArray.add("Real part of the complex number is not a valid double.");
          }
          if (!isDouble(imaginaryPartField.getText())) {
            missingArray.add("Imaginary part of the complex number is not a valid double.");
          }
          break;
        // Add other cases if needed
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
  }
