package controller;

import exception.FileEmptyException;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.chaosGame.ChaosGame;
import model.chaosGame.ChaosGameDescription;
import util.ErrorHandling;
import view.ChaosGameObserver;
import view.Layout;

/**
 * This class is responsible for handling actions and events in the Chaos Game application.
 * It acts as a controller between the UI components and the model classes.
 * It delegates the actual work to the observer and handles any exceptions that may occur.
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class HandleActionController {
  private ChaosGameObserver observer;
  private ErrorHandling errorHandling;
  private ChaosGameController chaosGameController;

  /**
   * Constructs a new HandleActionController with the specified observer.
   * @param observer the observer for the Chaos Game application
   */
  public HandleActionController(ChaosGameObserver observer) {
    this.observer =  observer;
    this.errorHandling = new ErrorHandling();
    this.chaosGameController = new ChaosGameController();
  }

  /**
   * Handles the event when the user clicks on the iterative transformation button.
   * This method delegates the work to the ChaosGameController and the observer.
   *
   * @param transformationsGroup the toggle group containing the transformation options
   * @param affineGrid           the grid containing the affine transformation inputs
   * @param juliaGrid            the grid containing the Julia transformation inputs
   * @param coordGrid            the grid containing the coordinate inputs
   * @param steps                the number of steps for the fractal generation
   * @param fractalCanvas        the canvas on which to draw the fractal
   * @param colorModeCheckbox    the checkbox indicating whether color mode is enabled
   */
  public void handleIterativeTransformation(ToggleGroup transformationsGroup, GridPane affineGrid, GridPane juliaGrid,
      GridPane coordGrid, double steps, Canvas fractalCanvas, CheckBox colorModeCheckbox) {
    try {
      ChaosGame currentChaosGame = chaosGameController.handleTransformationSelection(transformationsGroup, affineGrid, juliaGrid, coordGrid, (int) steps);
      currentChaosGame.fractalWithIterationTransformation();
      observer.drawFractal(fractalCanvas, currentChaosGame, colorModeCheckbox);
    }
    catch (Exception e) {
      errorHandling.failedToMakeFractalWithIterativeTransformation(e);
    }
  }

  /**
   * Handles the event when the user clicks the "Copy Last Transformation" button.
   * This method delegates the work to the ChaosGameController and the observer.
   *
   * @param transformationsGroup the toggle group containing the transformation options
   * @param coordGrid            the grid containing the coordinate inputs
   * @param affineGrid           the grid containing the affine transformation inputs
   * @param juliaGrid            the grid containing the Julia transformation inputs
   * @param juliaToggleSwitch    the toggle button for switching Julia mode
   * @param layout               the layout of the UI components
   */
  public void handleCopyLastTransformation(ToggleGroup transformationsGroup, GridPane coordGrid, GridPane affineGrid, GridPane juliaGrid, ToggleButton juliaToggleSwitch, Layout layout){
    try {
      if (chaosGameController.checkForMandelbrot()) {
        transformationsGroup.selectToggle(transformationsGroup.getToggles().get(4));
      } else {
        ChaosGameDescription lastDescription = chaosGameController.readFromFile();
        observer.copyLastTransformation(lastDescription, transformationsGroup, coordGrid, affineGrid, juliaGrid, juliaToggleSwitch, layout, chaosGameController.readTransformationType());
      }
    }
    catch (Exception e) {
      errorHandling.failedToCopyLastTransformation(e);
    }
  }

  /**
   * Handles the event when the user selects a transformation.
   * This method delegates the work to the observer.
   *
   * @param leftSide           the VBox containing the UI elements related to transformations
   * @param chosenTransformation the name of the selected transformation
   */
  public void handleTransformationSelected(VBox leftSide, String chosenTransformation){
    try {
      observer.onTransformationSelected(leftSide, chosenTransformation);
    }
    catch (Exception e) {
      errorHandling.failedToSelectTransformation(e);
    }
  }

  /**
   * Handles the event when the user clicks the "Show" button.
   * This method delegates the work to the ChaosGameController.
   *
   * @param transformationsGroup the toggle group containing the transformation options
   * @param affineGrid           the grid containing the affine transformation inputs
   * @param juliaGrid            the grid containing the Julia transformation inputs
   * @param coordGrid            the grid containing the coordinate inputs
   * @param steps                the number of steps for the fractal generation
   * @return the ChaosGame instance created from the selected transformation
   */
  public ChaosGame showButtonClicked(ToggleGroup transformationsGroup, GridPane affineGrid, GridPane juliaGrid,
      GridPane coordGrid, int steps){
    ChaosGame chaosGame = null;
    try {
      chaosGame = chaosGameController.handleTransformationSelection(transformationsGroup,
          affineGrid, juliaGrid, coordGrid, steps);
    }
    catch (IOException e) {
      errorHandling.fileNotFound(e);
    }
    catch (Exception e) {
      errorHandling.error(e);
    }

    return chaosGame;
  }

  /**
   * Saves the application settings to a file.
   *
   * @param appSettings the properties to save
   */
  public void saveSettings(Properties appSettings) {
    try {
      chaosGameController.saveSettings(appSettings);
    }
    catch (IOException e) {
      errorHandling.fileNotFound(e);
    }
    catch (Exception e) {
      errorHandling.error(e);
    }
  }

  /**
   * Loads the application settings from a file.
   *
   * @return the properties loaded from the file
   */
  public Properties loadSettings() {
    Properties appSettings = null;
    try {
      appSettings = chaosGameController.loadSettings();
    }
    catch (IOException e) {
      errorHandling.fileNotFound(e);
    }
    catch (FileEmptyException e) {
      errorHandling.fileIsEmpty(e);
    }
    catch (Exception e) {
      errorHandling.error(e);
    }
    return appSettings;
  }

  /**
   * Handles the event when the user changes the value of the steps slider.
   * This method delegates the work to the observer.
   *
   * @param stepsBox the VBox containing the steps slider and label
   * @param newValue the new value of the slider
   */
  public void onSliderValueChanged(VBox stepsBox, int newValue) {
    observer.onSliderValueChanged(stepsBox, newValue);
  }

  /**
   * Handles the event when the user switches the Julia toggle button.
   * This method delegates the work to the observer.
   *
   * @param juliaToggleSwitch the toggle button for switching Julia mode
   * @param coordGrid         the grid containing coordinate-related UI elements
   * @param stepsBox          the VBox containing step-related UI elements
   * @param iterativeTransformationButton the button for iterative transformation
   */
  public void onJuliaToggleSwitched(ToggleButton juliaToggleSwitch, GridPane coordGrid, VBox stepsBox, Button iterativeTransformationButton) {
    observer.onJuliaToggleSwitched(juliaToggleSwitch, coordGrid, stepsBox, iterativeTransformationButton);
  }

  /**
   * Adds a new row for matrix-vector transformation inputs.
   * This method delegates the work to the observer.
   *
   * @param rowCount    the number of rows in the grid
   * @param affineGrid  the grid containing the affine transformation inputs
   * @param layout      the layout of the UI components
   */
  public void addMatrixVectorRow(int rowCount, GridPane affineGrid, Layout layout) {
    observer.addMatrixVectorRow(rowCount, affineGrid, layout);
  }

  /**
   * Removes the last row of matrix-vector transformation inputs.
   * This method delegates the work to the observer.
   *
   * @param affineGrid the grid containing the affine transformation inputs
   */
  public void removeMatrixVectorRow(GridPane affineGrid) {
    observer.removeMatrixVectorRow(affineGrid);
  }

  /**
   * Resets input fields to their default style.
   * This method delegates the work to the observer.
   *
   * @param affineGrid  the grid containing affine transformation inputs
   * @param juliaGrid   the grid containing Julia set inputs
   * @param affineBox   the VBox containing affine transformation inputs
   */
  public void resetFieldsToDefaultStyle(GridPane affineGrid, GridPane juliaGrid, VBox affineBox) {
    observer.resetFieldsToDefaultStyle(affineGrid, juliaGrid, affineBox);
  }

  /**
   * Draws the fractal on the specified canvas.
   * This method delegates the work to the observer.
   *
   * @param fractalCanvas    the canvas on which to draw the fractal
   * @param currentChaosGame the chaos game instance to use for drawing the fractal
   * @param colorModeCheckbox the checkbox indicating whether color mode is enabled
   */
  public void drawFractal(Canvas fractalCanvas, ChaosGame currentChaosGame, CheckBox colorModeCheckbox) {
    observer.drawFractal(fractalCanvas, currentChaosGame, colorModeCheckbox);
  }

  /**
   * Handles the event when the canvas size is changed.
   * This method delegates the work to the observer.
   *
   * @param fractalCanvas    the canvas on which to draw the fractal
   * @param currentChaosGame the current chaos game instance
   * @param colorModeCheckbox the checkbox indicating whether color mode is enabled
   */
  public void onCanvasSizeChanged(Canvas fractalCanvas, ChaosGame currentChaosGame, CheckBox colorModeCheckbox) {
    observer.onCanvasSizeChanged(fractalCanvas, currentChaosGame, colorModeCheckbox);
  }
}
