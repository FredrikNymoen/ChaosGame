package controller;

import exception.FileEmptyException;
import exception.UnexpectedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import model.chaosgame.ChaosGame;
import model.chaosgame.ChaosGameDescription;
import model.factory.ChaosGameDescriptionFactory;
import model.factory.ChaosGameFactory;
import model.filehandling.ChaosGameFileHandler;
import model.filehandling.SettingsHandler;
import model.mathcore.Complex;
import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import model.transformations.JuliaTransform;
import util.UIHelper;
import util.Utility;

/**
 * ChaosGameController class is used to control the chaos game GUI. The class contains methods to
 * create a chaos game and read- and write to file.
 *
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
  public ChaosGameController() {
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

    for (int row = 0; row < affineGrid.getRowCount(); row++) {
      double[] matrixValues = new double[4]; // To store a00, a01, a10, a11
      double[] vectorValues = new double[2]; // To store x0, y0

      int finalRow = row;
      // Retrieve matrix values
      IntStream.range(0, 4).forEach(i -> {
        TextField textField = (TextField) UIHelper.getNodeFromGridPane(affineGrid, i, finalRow);
        assert textField != null;
        matrixValues[i] = Double.parseDouble(textField.getText());
      });

      // Retrieve vector values
      IntStream.range(0, 2).forEach(i -> {
        TextField textField = (TextField) UIHelper.getNodeFromGridPane(affineGrid, i + 5, finalRow);
        assert textField != null;
        vectorValues[i] = Double.parseDouble(textField.getText());
      });

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
   * @throws IOException         if an error occurs
   * @throws UnexpectedException if an unexpected error occurs
   */
  public ChaosGame handleTransformationSelection(ToggleGroup transformationsGroup,
      GridPane affineGrid, GridPane juliaGrid, GridPane coordGrid, int steps)
      throws IOException, UnexpectedException {
    ChaosGame chaosGame;
    ChaosGameDescription description = null;

    try {
      Vector2D[] coords = getCoordinates(coordGrid);
      Vector2D minCoords = coords[0];
      Vector2D maxCoords = coords[1];

      RadioButton selectedButton = (RadioButton) transformationsGroup.getSelectedToggle();
      if (selectedButton != null) {
        description = createChaosGameDescription(selectedButton, affineGrid,
            juliaGrid, minCoords, maxCoords);
      }

      assert selectedButton != null;
      chaosGame = createChaosGame(selectedButton, description, juliaGrid, steps);
    } catch (IOException e) {
      throw new IOException(Utility.FILE_NOT_FOUND);
    } catch (Exception e) {
      throw new UnexpectedException(e.getMessage());
    }

    return chaosGame;
  }

  /**
   * Gets the coordinate values from the coordGrid and stores them in a Vector2D array.
   *
   * @param coordGrid the GridPane containing the coordinate values
   * @return Vector2D[] an array containing the minimum and maximum coordinates
   */
  private Vector2D[] getCoordinates(GridPane coordGrid) {
    TextField[] coordFields = UIHelper.getCoordinateTextFields(coordGrid);
    TextField minX = coordFields[0];
    TextField minY = coordFields[1];
    TextField maxX = coordFields[2];
    TextField maxY = coordFields[3];
    Vector2D minCoords = new Vector2D(Double.parseDouble(minX.getText()),
        Double.parseDouble(minY.getText()));
    Vector2D maxCoords = new Vector2D(Double.parseDouble(maxX.getText()),
        Double.parseDouble(maxY.getText()));
    return new Vector2D[]{minCoords, maxCoords};
  }

  /**
   * Creates a ChaosGameDescription object based on the selected transformation.
   *
   * @param selectedButton the selected transformation
   * @param affineGrid     the GridPane containing the affine transformation values
   * @param juliaGrid      the GridPane containing the Julia transformation values
   * @param minCoords      the minimum coordinates of the chaos game
   * @param maxCoords      the maximum coordinates of the chaos game
   * @return the ChaosGameDescription object created based on the selected transformation
   * @throws IOException         if an error occurs
   * @throws UnexpectedException if an unexpected error occurs
   */
  private ChaosGameDescription createChaosGameDescription(RadioButton selectedButton, GridPane
      affineGrid, GridPane juliaGrid, Vector2D minCoords, Vector2D maxCoords)
      throws IOException, UnexpectedException {
    ChaosGameDescription description = null;
    Complex c;

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
      default:
        break;
    }

    return description;
  }

  /**
   * Creates a ChaosGame object based on the selected transformation.
   *
   * @param selectedButton the selected transformation
   * @param description    the ChaosGameDescription object
   * @param juliaGrid      the GridPane containing the Julia transformation values
   * @param steps          the number of steps to run the chaos game
   * @return ChaosGame the ChaosGame object created based on the selected transformation
   * @throws IOException         if an error occurs
   * @throws UnexpectedException if an unexpected error occurs
   */
  private ChaosGame createChaosGame(RadioButton selectedButton, ChaosGameDescription description,
      GridPane juliaGrid, int steps) throws IOException, UnexpectedException {
    ChaosGame chaosGame;
    String transformation = selectedButton.getText();
    boolean isBarnsley = transformation.equals("Barnsley");
    ToggleButton juliaToggleButton = (ToggleButton) UIHelper.getNodeFromGridPane(juliaGrid, 0, 1);

    switch (transformation) {
      case "Julia":
        assert juliaToggleButton != null;
        if (juliaToggleButton.isSelected()) {
          Complex c;
          c = ((JuliaTransform) description.getTransforms().get(0)).getPoint();
          chaosGame = chaosGameFactory.createJuliaChaosGameWithConvergenceMode(c);
          writeToFile(description, "convergence-mode");
        } else {
          chaosGame = chaosGameFactory.createChaosGame(description, Utility.CHAOS_GAME_WIDTH,
              Utility.CHAOS_GAME_HEIGHT, steps, false);
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

    return chaosGame;
  }


  /**
   * Checks if the file contains a Mandelbrot description.
   *
   * @return boolean true if the file contains a Mandelbrot description, false otherwise
   * @throws IOException         if an error occurs
   * @throws UnexpectedException if an unexpected error occurs
   */
  public boolean checkForMandelbrot() throws IOException, UnexpectedException {
    boolean flag;
    try {
      flag = fileHandler.checkForMandelbrot();
    } catch (IOException e) {
      throw new IOException(Utility.FILE_NOT_FOUND);
    } catch (Exception e) {
      throw new UnexpectedException(Utility.ERROR_READING_FILE);
    }
    return flag;
  }

  /**
   * Reads a ChaosGameDescription object from a file.
   *
   * @return ChaosGameDescription the ChaosGameDescription object read from the file
   * @throws IOException         if an error occurs
   * @throws UnexpectedException if an unexpected error occurs
   */
  public ChaosGameDescription readFromFile()
      throws IOException, FileEmptyException, UnexpectedException {
    ChaosGameDescription chaosGameDescription;
    try {
      chaosGameDescription = fileHandler.readFromFile();
    } catch (IOException e) {
      throw new IOException(Utility.FILE_NOT_FOUND);
    } catch (FileEmptyException e) {
      throw new FileEmptyException("File is empty.");
    } catch (Exception e) {
      throw new UnexpectedException(Utility.ERROR_READING_FILE);
    }
    return chaosGameDescription;
  }

  /**
   * Reads the transformation type from a file.
   *
   * @return String the transformation type read from the file
   * @throws IOException         if an error occurs
   * @throws UnexpectedException if an unexpected error occurs
   */
  public String readTransformationType() throws IOException, UnexpectedException {
    String transformationType;
    try {
      transformationType = fileHandler.readTransformationType();
    } catch (IOException e) {
      throw new IOException(Utility.FILE_NOT_FOUND);
    } catch (Exception e) {
      throw new UnexpectedException(Utility.ERROR_READING_FILE);
    }
    return transformationType;
  }

  /**
   * Writes a ChaosGameDescription object to a file.
   *
   * @param description        the ChaosGameDescription object to write to the file
   * @param transformationType the transformation type to write to the file
   * @throws IOException         if an error occurs
   * @throws UnexpectedException if an unexpected error occurs
   */
  public void writeToFile(ChaosGameDescription description,
      String transformationType) throws IOException, UnexpectedException {
    try {
      fileHandler.writeToFile(description, transformationType);
    } catch (IOException e) {
      throw new IOException(Utility.FILE_NOT_FOUND);
    } catch (Exception e) {
      throw new UnexpectedException(Utility.ERROR_WRITING_TO_FILE);
    }
  }

  /**
   * Writes a line to a file.
   *
   * @param line the line to write to the file
   * @throws IOException         if an error occurs
   * @throws UnexpectedException if an unexpected error occurs
   */
  public void writeLineToFile(String line) throws IOException, UnexpectedException {
    try {
      fileHandler.writeLineToFile(line);
    } catch (IOException e) {
      throw new IOException(Utility.FILE_NOT_FOUND);
    } catch (Exception e) {
      throw new UnexpectedException(Utility.ERROR_WRITING_TO_FILE);
    }
  }


  /**
   * Saves the application settings to a file.
   *
   * @param appSettings the application settings to save
   * @throws IOException         if an error occurs
   * @throws UnexpectedException if an unexpected error occurs
   */
  public void saveSettings(Properties appSettings) throws IOException, UnexpectedException {
    try {
      settingsHandler.saveSettings(appSettings);
    } catch (IOException e) {
      throw new IOException(Utility.FILE_NOT_FOUND);
    } catch (Exception e) {
      throw new UnexpectedException(Utility.ERROR_WRITING_TO_FILE);
    }
  }

  /**
   * Loads the application settings from a file.
   *
   * @return Properties the application settings loaded from the file
   * @throws IOException         if an error occurs
   * @throws FileEmptyException  if the file is empty
   * @throws UnexpectedException if an unexpected error occurs
   */
  public Properties loadSettings() throws IOException, FileEmptyException, UnexpectedException {
    Properties appProperties;
    try {
      appProperties = settingsHandler.loadSettings();
    } catch (IOException e) {
      throw new IOException(Utility.FILE_NOT_FOUND);
    } catch (FileEmptyException e) {
      throw new FileEmptyException("File is empty.");
    } catch (Exception e) {
      throw new UnexpectedException(Utility.ERROR_READING_FILE);
    }
    return appProperties;
  }
}
