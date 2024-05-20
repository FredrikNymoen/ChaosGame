package controller;

import exception.FileEmptyException;
import java.io.IOException;
import java.util.Properties;
import model.chaosGame.ChaosGame;
import model.chaosGame.ChaosGameDescription;
import model.factory.ChaosGameDescriptionFactory;
import model.factory.ChaosGameFactory;
import model.filehandling.ChaosGameFileHandler;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import model.filehandling.SettingsHandler;
import model.mathcore.Complex;
import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import util.UIHelper;
import util.Utility;

/**
 * ChaosGameController class is used to control the chaos game GUI.
 * The class contains methods to create a chaos game and read- and write to file.
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */

public class ChaosGameController {
  private final ChaosGameFactory chaosGameFactory;
  private final ChaosGameDescriptionFactory factory;
  private final SettingsHandler settingsHandler;
  private final ChaosGameFileHandler fileHandler;

  /**
   * Constructor for ChaosGameController.
   */
  public ChaosGameController(){
    chaosGameFactory = new ChaosGameFactory();
    factory = new ChaosGameDescriptionFactory();
    settingsHandler = new SettingsHandler(Utility.SETTINGS_FILE_PATH);
    fileHandler = new ChaosGameFileHandler(Utility.SHOWN_TRANSFORMATION_FILE_PATH);
  }

/**
   * Gets the affine transformation values from the affineGrid and stores them in a list of
   * Matrix2x2 and Vector2D objects.
   *
   * @param affineMatrices the list to store the affine matrices
   * @param affineVectors  the list to store the affine vectors
   * @param affineGrid     the GridPane containing the affine transformation values
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
        vectorValues[i] = Double.parseDouble(textField.getText());
      }

      Matrix2x2 matrix = new Matrix2x2(matrixValues[0], matrixValues[1], matrixValues[2],
          matrixValues[3]);
      Vector2D vector = new Vector2D(vectorValues[0], vectorValues[1]);
      affineMatrices.add(matrix);
      affineVectors.add(vector);
    }
  }


  /**
   * Handles the selection of a transformation and creates a ChaosGame object based on the selected
   * transformation.
   *
   * @param transformationsGroup the ToggleGroup containing the transformation selection
   * @param affineGrid           the GridPane containing the affine transformation values
   * @param juliaGrid            the GridPane containing the Julia transformation values
   * @param coordGrid            the GridPane containing the coordinate values
   * @param steps                the number of steps to run the chaos game
   * @return ChaosGame the ChaosGame object created based on the selected transformation
   * @throws Exception if an error occurs
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
            chaosGame = chaosGameFactory.createJuliaChaosGameWithConvergenceMode(c);
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


  /**
   * Checks if the file contains a Mandelbrot description.
   * @return boolean true if the file contains a Mandelbrot description, false otherwise
   * @throws Exception if an error occurs
   */
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

  /**
   * Reads a ChaosGameDescription object from a file.
   * @return ChaosGameDescription the ChaosGameDescription object read from the file
   * @throws Exception if an error occurs
   */
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

  /**
   * Reads the transformation type from a file.
   * @return String the transformation type read from the file
   * @throws Exception if an error occurs
   */
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

  /**
   * Writes a ChaosGameDescription object to a file.
   * @param description the ChaosGameDescription object to write to the file
   * @param transformationType the transformation type to write to the file
   * @throws Exception if an error occurs
   */
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

  /**
   * Writes a line to a file.
   * @param line the line to write to the file
   * @throws Exception if an error occurs
   */
  public void writeLineToFile(String line) throws Exception{
    try {
      fileHandler.writeLineToFile(line);
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (Exception e) {
      throw new Exception("Error writing to file.");
    }
  }


  /**
   * Saves the application settings to a file.
   * @param appSettings the application settings to save
   * @throws Exception if an error occurs
   */
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

  /**
   * Loads the application settings from a file.
   * @return Properties the application settings loaded from the file
   * @throws Exception if an error occurs
   */
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
