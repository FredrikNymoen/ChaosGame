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

public class HandleActionController {
  private ChaosGameObserver observer;
  private ErrorHandling errorHandling;
  private ChaosGameController chaosGameController;
  public HandleActionController(ChaosGameObserver observer) {
    this.observer =  observer;
    this.errorHandling = new ErrorHandling();
    this.chaosGameController = new ChaosGameController();
  }

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

  public void handleTransformationSelected(VBox leftSide, String chosenTransformation){
    try {
      observer.onTransformationSelected(leftSide, chosenTransformation);
    }
    catch (Exception e) {
      errorHandling.failedToSelectTransformation(e);
    }
  }

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

  public void onSliderValueChanged(VBox stepsBox, int newValue) {
    observer.onSliderValueChanged(stepsBox, newValue);
  }

  public void onJuliaToggleSwitched(ToggleButton juliaToggleSwitch, GridPane coordGrid, VBox stepsBox, Button iterativeTransformationButton) {
    observer.onJuliaToggleSwitched(juliaToggleSwitch, coordGrid, stepsBox, iterativeTransformationButton);
  }

  public void addMatrixVectorRow(int rowCount, GridPane affineGrid, Layout layout) {
    observer.addMatrixVectorRow(rowCount, affineGrid, layout);
  }

  public void removeMatrixVectorRow(GridPane affineGrid) {
    observer.removeMatrixVectorRow(affineGrid);
  }

  public void resetFieldsToDefaultStyle(GridPane affineGrid, GridPane juliaGrid, VBox affineBox) {
    observer.resetFieldsToDefaultStyle(affineGrid, juliaGrid, affineBox);
  }

  public void drawFractal(Canvas fractalCanvas, ChaosGame currentChaosGame, CheckBox colorModeCheckbox) {
    observer.drawFractal(fractalCanvas, currentChaosGame, colorModeCheckbox);
  }

  public void onCanvasSizeChanged(Canvas fractalCanvas, ChaosGame currentChaosGame, CheckBox colorModeCheckbox) {
    observer.onCanvasSizeChanged(fractalCanvas, currentChaosGame, colorModeCheckbox);
  }
}
