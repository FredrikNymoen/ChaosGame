package controller;

import exception.FileEmptyException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
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
import model.filehandling.SettingsHandler;
import model.mathcore.Complex;
import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import util.ErrorHandling;
import util.UIHelper;
import util.Utility;

/**
 * ChaosGameController class is used to control the chaos game GUI.
 * The class contains methods to get affine transformation values,
 * create a chaos game and handle transformation selection.
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */

public class ChaosGameController {
  private final ChaosGameFactory chaosGameFactory;
  private final ChaosGameDescriptionFactory factory;
  private final SettingsHandler settingsHandler;
  private final ChaosGameFileHandler fileHandler;

  public ChaosGameController(){
    chaosGameFactory = new ChaosGameFactory();
    factory = new ChaosGameDescriptionFactory();
    settingsHandler = new SettingsHandler(Utility.SETTINGS_FILE_PATH);
    fileHandler = new ChaosGameFileHandler(Utility.SHOWN_TRANSFORMATION_FILE_PATH);
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
        TextField textField = (TextField) UIHelper.getNodeFromGridPane(affineGrid, i, row);
        matrixValues[i] = Double.parseDouble(textField.getText());
      }

      // Retrieve vector values
      for (int i = 0; i < 2; i++) { // vectorValues indexes are 0 to 1, grid positions are 5 and 6
        TextField textField = (TextField) UIHelper.getNodeFromGridPane(affineGrid, i + 5, row);
        try {
          vectorValues[i] = Double.parseDouble(textField.getText());
        } catch (NumberFormatException e) {
          System.out.println("Invalid input for vector values.");
        }
      }

      Matrix2x2 matrix = new Matrix2x2(matrixValues[0], matrixValues[1], matrixValues[2],
          matrixValues[3]);
      Vector2D vector = new Vector2D(vectorValues[0], vectorValues[1]);
      affineMatrices.add(matrix);
      affineVectors.add(vector);
    }
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
      GridPane affineGrid, GridPane juliaGrid, GridPane coordGrid, int steps) throws Exception{
    ChaosGame chaosGame = null;
    ChaosGameDescription description = null;
    Complex c = null;

    try {
      TextField[] coordFields = UIHelper.getCoordinateTextFields(coordGrid);
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
            writeToFile(description, "affine");
            break;
          case "Julia":
            TextField[] juliaFields = UIHelper.getJuliaTextFields(juliaGrid);
            double realPart = Double.parseDouble(juliaFields[0].getText());
            double imaginaryPart = Double.parseDouble(juliaFields[1].getText());
            c = new Complex(realPart, imaginaryPart);
            description = factory.julia(minCoords, maxCoords, c);
            break;
          case "Sierpinski":
            description = factory.sierpinski(minCoords, maxCoords);
            writeToFile(description, "sierpinski");
            break;
          case "Barnsley":
            description = factory.barnsley(minCoords, maxCoords);
            writeToFile(description, "barnsley");
            break;
          case "Maple-Tree":
            description = factory.mapleTree(minCoords, maxCoords);
            writeToFile(description, "mapleTree");
            break;
        }
      }

      String transformation = selectedButton.getText();
      boolean isBarnsley = transformation.equals("Barnsley");
      ToggleButton juliaToggleButton = (ToggleButton) UIHelper.getNodeFromGridPane(juliaGrid, 0, 1);
      switch (transformation) {
        case "Julia":
          if (juliaToggleButton.isSelected()) {
            chaosGame = chaosGameFactory.createJuliaChaosGame(c);
            writeToFile(description, "convergence-mode");
          } else {
            chaosGame = chaosGameFactory.createChaosGame(description, Utility.CHAOS_GAME_WIDTH,
                Utility.CHAOS_GAME_HEIGHT, steps, isBarnsley);
            writeToFile(description, "steps-mode");
          }
          break;
        case "Mandelbrot":
          chaosGame = chaosGameFactory.createMandelbrotChaosGame();
          writeLineToFile("Mandelbrot");
          break;
        default:
          chaosGame = chaosGameFactory.createChaosGame(description, Utility.CHAOS_GAME_WIDTH,
              Utility.CHAOS_GAME_HEIGHT, steps, isBarnsley);
          break;
      }
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    }
    catch (Exception e) {
      throw new Exception(e.getMessage());
    }

    return chaosGame;
  }


  public boolean checkForMandelbrot() throws Exception{
    boolean flag = false;
    try{
      flag = fileHandler.checkForMandelbrot();
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (Exception e) {
      throw new Exception("Error reading file.");
    }
    return flag;
  }

  public ChaosGameDescription readFromFile() throws Exception{
    ChaosGameDescription chaosGameDescription = null;
    try{
      chaosGameDescription = fileHandler.readFromFile();
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (FileEmptyException e) {
      throw new FileEmptyException("File is empty.");
    } catch (Exception e) {
      throw new Exception("Error reading file.");
    }
    return chaosGameDescription;
  }

  public String readTransformationType() throws Exception{
    String transformationType = null;
    try{
      transformationType = fileHandler.readTransformationType();
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (Exception e) {
      throw new Exception("Error reading file.");
    }
    return transformationType;
  }

  public void writeToFile(ChaosGameDescription description, String transformationType) throws Exception{
    try{
      fileHandler.writeToFile(description, transformationType);
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (Exception e) {
      throw new Exception("Error writing to file.");
    }
  }

  public void writeLineToFile(String line) throws Exception{
    try {
      fileHandler.writeLineToFile(line);
    }
    catch (IOException e) {
      System.out.println("File not found.");
    } catch (Exception e) {
      System.out.println("Error writing to file.");
    }
  }


  public void saveSettings(Properties appSettings) throws Exception{
    try {
      settingsHandler.saveSettings(appSettings);
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (Exception e) {
      throw new Exception("Error writing to file.");
    }
  }

  public Properties loadSettings() throws Exception{
    Properties appProperties = null;
    try {
      appProperties = settingsHandler.loadSettings();
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (FileEmptyException e) {
      throw new FileEmptyException("File is empty.");
    } catch (Exception e) {
      throw new Exception("Error reading file.");
    }
    return appProperties;
  }
}
