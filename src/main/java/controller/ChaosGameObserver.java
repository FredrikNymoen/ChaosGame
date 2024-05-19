package controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.chaosGame.ChaosGame;
import model.chaosGame.ChaosGameDescription;
import view.Layout;

public interface ChaosGameObserver {
  void onSliderValueChanged(VBox stepsBox, int newValue);
  void onCanvasSizeChanged(Canvas fractalCanvas, ChaosGame currentChaosGame, CheckBox colorModeCheckbox);
  void onTransformationSelected(VBox leftside, String transformation);
  void drawFractal(Canvas fractalCanvas, ChaosGame chaosGame, CheckBox colorModeCheckbox);
  void onJuliaToggleSwitched(ToggleButton juliaToggle, GridPane coordGrid, VBox stepsBox, Button iterativeTransformationButton);
  void addMatrixVectorRow(int row, GridPane affineGrid, Layout layout);
  void removeMatrixVectorRow(GridPane affineGrid);
  void resetFieldsToDefaultStyle(GridPane affineGrid, GridPane juliaGrid, VBox affineBox);
  void copyLastTransformation(ChaosGameDescription chaosGameDescription, ToggleGroup transformationsGroup, GridPane coordGrid, GridPane affineGrid, GridPane juliaGrid, ToggleButton juliaToggleSwitch, Layout layout, String transformationType);
}
