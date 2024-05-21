package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.chaosGame.ChaosGame;
import model.chaosGame.ChaosGameDescription;

/**
 * This interface defines the observer for the Chaos Game application. It contains methods that
 * handle various events and actions related to the chaos game and its UI components.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public interface ChaosGameObserver {

  /**
   * Called when the value of a slider changes.
   *
   * @param stepsBox the VBox containing step-related UI elements
   * @param newValue the new value of the slider
   */
  void onSliderValueChanged(VBox stepsBox, int newValue);

  /**
   * Called when the size of the canvas changes.
   *
   * @param fractalCanvas     the canvas on which the fractal is drawn
   * @param currentChaosGame  the current chaos game instance
   * @param colorModeCheckbox the checkbox indicating whether color mode is enabled
   */
  void onCanvasSizeChanged(Canvas fractalCanvas, ChaosGame currentChaosGame,
      CheckBox colorModeCheckbox);

  /**
   * Called when a transformation is selected.
   *
   * @param leftside       the VBox containing UI elements related to transformations
   * @param transformation the name of the selected transformation
   */
  void onTransformationSelected(VBox leftside, String transformation);

  /**
   * Draws the fractal on the specified canvas.
   *
   * @param fractalCanvas     the canvas on which to draw the fractal
   * @param chaosGame         the chaos game instance to use for drawing the fractal
   * @param colorModeCheckbox the checkbox indicating whether color mode is enabled
   */
  void drawFractal(Canvas fractalCanvas, ChaosGame chaosGame, CheckBox colorModeCheckbox);

  /**
   * Called when the Julia toggle button is switched.
   *
   * @param juliaToggle                   the toggle button for switching Julia mode
   * @param coordGrid                     the grid containing coordinate-related UI elements
   * @param stepsBox                      the VBox containing step-related UI elements
   * @param iterativeTransformationButton the button for iterative transformation
   */
  void onJuliaToggleSwitched(ToggleButton juliaToggle, GridPane coordGrid, VBox stepsBox,
      Button iterativeTransformationButton);

  /**
   * Adds a new row for matrix-vector transformation inputs.
   *
   * @param row        the index of the row to be added
   * @param affineGrid the grid containing affine transformation inputs
   * @param layout     the layout manager for arranging UI elements
   */
  void addMatrixVectorRow(int row, GridPane affineGrid, Layout layout);

  /**
   * Removes the last row of matrix-vector transformation inputs.
   *
   * @param affineGrid the grid containing affine transformation inputs
   */
  void removeMatrixVectorRow(GridPane affineGrid);

  /**
   * Resets input fields to their default style.
   *
   * @param affineGrid the grid containing affine transformation inputs
   * @param juliaGrid  the grid containing Julia set inputs
   * @param affineBox  the VBox containing affine transformation UI elements
   */
  void resetFieldsToDefaultStyle(GridPane affineGrid, GridPane juliaGrid, VBox affineBox);

  /**
   * Copies the last transformation to a new one.
   *
   * @param chaosGameDescription the description of the current chaos game
   * @param transformationsGroup the group of toggle buttons representing transformations
   * @param coordGrid            the grid containing coordinate-related UI elements
   * @param affineGrid           the grid containing affine transformation inputs
   * @param juliaGrid            the grid containing Julia set inputs
   * @param juliaToggleSwitch    the toggle button for switching Julia mode
   * @param layout               the layout manager for arranging UI elements
   * @param transformationType   the type of transformation to copy
   */
  void copyLastTransformation(ChaosGameDescription chaosGameDescription,
      ToggleGroup transformationsGroup, GridPane coordGrid, GridPane affineGrid, GridPane juliaGrid,
      ToggleButton juliaToggleSwitch, Layout layout, String transformationType);
}
