package view;

import controller.ChaosGameObserver;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.chaosGame.ChaosGame;
import model.chaosGame.ChaosGameDescription;
import model.transformations.AffineTransform2D;
import model.transformations.JuliaTransform;
import util.UIHelper;

public class EventHandler implements ChaosGameObserver {
  private FractalDrawer fractalDrawer = new FractalDrawer();

  @Override
  public void onTransformationSelected(VBox leftside, String transformation){
    VBox affineBox = (VBox) leftside.getChildren().get(4);
    GridPane juliaGrid = (GridPane) leftside.getChildren().get(3);
    ToggleButton toggleSwitch = (ToggleButton) UIHelper.getNodeFromGridPane(juliaGrid, 0, 1);
    GridPane coordGrid = (GridPane) leftside.getChildren().get(2);
    VBox stepsBox = (VBox) leftside.getChildren().get(1);
    Button iterativeTransformationButton = (Button) ((HBox) leftside.getChildren().get(6)).getChildren().get(0);

    switch (transformation) {
      case "Affine":
        affineBox.setDisable(false);
        juliaGrid.setDisable(true);
        stepsBox.setDisable(false);
        coordGrid.setDisable(false);
        iterativeTransformationButton.setDisable(false);
        break;
      case "Julia":
        juliaGrid.setDisable(false);
        affineBox.setDisable(true);
        if (toggleSwitch.isSelected()) {
          stepsBox.setDisable(true);
          coordGrid.setDisable(true);
          iterativeTransformationButton.setDisable(true);
        } else {
          stepsBox.setDisable(false);
          coordGrid.setDisable(false);
          iterativeTransformationButton.setDisable(false);
        }
        break;
      case "Mandelbrot":
        affineBox.setDisable(true);
        juliaGrid.setDisable(true);
        stepsBox.setDisable(true);
        coordGrid.setDisable(true);
        iterativeTransformationButton.setDisable(true);
        break;
      default:
        affineBox.setDisable(true);
        juliaGrid.setDisable(true);
        stepsBox.setDisable(false);
        coordGrid.setDisable(false);
        iterativeTransformationButton.setDisable(false);
        break;
    }
  }

  @Override
  public void onCanvasSizeChanged(Canvas fractalCanvas, ChaosGame currentChaosGame, CheckBox colorModeCheckbox){
    if (currentChaosGame != null) {
      fractalDrawer.drawFractal(fractalCanvas, currentChaosGame, colorModeCheckbox);
    }
  }

  @Override
  public void drawFractal(Canvas fractalCanvas, ChaosGame chaosGame, CheckBox colorModeCheckbox){
    fractalDrawer.drawFractal(fractalCanvas, chaosGame, colorModeCheckbox);
  }


  @Override
  public void onSliderValueChanged(VBox stepsBox, int newValue) {
    Label stepsValueLabel = (Label) stepsBox.getChildren().get(2);
    stepsValueLabel.setText(String.format("%,d", newValue)); // Format the number with commas
  }

  @Override
  public void onJuliaToggleSwitched(ToggleButton juliaToggle, GridPane coordGrid, VBox stepsBox, Button iterativeTransformationButton){
    if (juliaToggle.isSelected()) {
      juliaToggle.setText("Use Convergence Iteration mode (click to change)");
      stepsBox.setDisable(true);
      coordGrid.setDisable(true);
      iterativeTransformationButton.setDisable(true);
    } else {
      juliaToggle.setText("Use Steps mode (click to change)");
      stepsBox.setDisable(false);
      coordGrid.setDisable(false);
      iterativeTransformationButton.setDisable(false);
    }
  }

  @Override
  public void addMatrixVectorRow(int row, GridPane affineGrid, Layout layout) {
    String[] matrixPlaceholders = {"a00", "a01", "a10", "a11"};
    for (int i = 0; i < matrixPlaceholders.length; i++) {
      TextField matrixField = layout.createDecimalTextField("0.0");
      matrixField.setPrefWidth(50);
      matrixField.setPromptText(matrixPlaceholders[i]);
      affineGrid.add(matrixField, i, row);
    }

    // Space between matrix and vector elements
    Pane spacer = new Pane();
    spacer.setMinSize(20, 1);
    affineGrid.add(spacer, 4, row);

    // Vector elements with placeholders
    TextField vectorFieldX = layout.createDecimalTextField("0.0");
    vectorFieldX.setPrefWidth(50);
    vectorFieldX.setPromptText("x0");
    affineGrid.add(vectorFieldX, 5, row);

    TextField vectorFieldY = layout.createDecimalTextField("0.0");
    vectorFieldY.setPrefWidth(50);
    vectorFieldY.setPromptText("y0");
    affineGrid.add(vectorFieldY, 6, row);
  }

  @Override
  public void removeMatrixVectorRow(GridPane affineGrid) {
    int lastRowIndex = affineGrid.getRowCount() - 1;
    if (lastRowIndex >= 1) {
      // Remove all elements in the last row
      for (int i = 0; i < 7; i++) {
        Node node = UIHelper.getNodeFromGridPane(affineGrid, i, lastRowIndex);
        affineGrid.getChildren().remove(node);
      }
    }
  }


  @Override
  public void resetFieldsToDefaultStyle(GridPane affineGrid, GridPane juliaGrid, VBox affineBox){
    // Reset all fields to default style
    for (Node node : affineGrid.getChildren()) {
      if (node instanceof TextField) {
        node.setStyle("");
      }
    }
    TextField[] coordinateFieldsJulia = UIHelper.getJuliaTextFields(juliaGrid);
    TextField realPartField = coordinateFieldsJulia[0];
    TextField imaginaryPartField = coordinateFieldsJulia[1];

    affineGrid.setStyle("");
    affineBox.setStyle("");
    realPartField.setStyle("");
    imaginaryPartField.setStyle("");
  }

  public void copyLastTransformation(ChaosGameDescription lastDescription, ToggleGroup transformationsGroup, GridPane coordGrid, GridPane affineGrid, GridPane juliaGrid, ToggleButton juliaToggleSwitch, Layout layout, String transformationType){
      TextField[] coordinateFields = UIHelper.getCoordinateTextFields(coordGrid);
      coordinateFields[0].setText(String.valueOf(lastDescription.getMinCoords().getX0()));
      coordinateFields[1].setText(String.valueOf(lastDescription.getMinCoords().getX1()));
      coordinateFields[2].setText(String.valueOf(lastDescription.getMaxCoords().getX0()));
      coordinateFields[3].setText(String.valueOf(lastDescription.getMaxCoords().getX1()));

      if (lastDescription.getTransforms().get(0) instanceof AffineTransform2D) {
        switch (transformationType) {
          case "affine":
            transformationsGroup.selectToggle(transformationsGroup.getToggles().get(0));
            copyLastAffineTransformation(lastDescription, affineGrid, layout);
            break;
          case "barnsley":
            transformationsGroup.selectToggle(transformationsGroup.getToggles().get(1));
            break;
          case "sierpinski":
            transformationsGroup.selectToggle(transformationsGroup.getToggles().get(3));
            break;
          case "mapleTree":
            transformationsGroup.selectToggle(transformationsGroup.getToggles().get(5));
            break;
        }
      } else {
        if(transformationType.equals("convergence-mode")){
          juliaToggleSwitch.setSelected(true);
          juliaToggleSwitch.setText("Use Convergence Iteration mode (click to change)");
        }
        else{
          juliaToggleSwitch.setSelected(false);
          juliaToggleSwitch.setText("Use Steps mode (click to change)");
        }
        transformationsGroup.selectToggle(transformationsGroup.getToggles().get(2));

        TextField[] coordinateFieldsJulia = UIHelper.getJuliaTextFields(juliaGrid);
        TextField realPartField = coordinateFieldsJulia[0];
        TextField imaginaryPartField = coordinateFieldsJulia[1];
        realPartField.setText(
            ((JuliaTransform) lastDescription.getTransforms().get(0)).getPoint().getX0() + "");
        imaginaryPartField.setText(
            ((JuliaTransform) lastDescription.getTransforms().get(0)).getPoint().getX1() + "");
      }
  }

  public void copyLastAffineTransformation(ChaosGameDescription lastDescription, GridPane affineGrid, Layout layout) {
    while(affineGrid.getRowCount()!=1){
      removeMatrixVectorRow(affineGrid);
    }
    for(int i=0; i< lastDescription.getTransforms().size(); i++){
      if (i > 0) {
        addMatrixVectorRow(i, affineGrid, layout);
      }
      AffineTransform2D affine = (AffineTransform2D) lastDescription.getTransforms().get(i);
      TextField a00 = (TextField) UIHelper.getNodeFromGridPane(affineGrid, 0, i);
      TextField a01 = (TextField) UIHelper.getNodeFromGridPane(affineGrid, 1, i);
      TextField a10 = (TextField) UIHelper.getNodeFromGridPane(affineGrid, 2, i);
      TextField a11 = (TextField) UIHelper.getNodeFromGridPane(affineGrid, 3, i);
      TextField x0 = (TextField) UIHelper.getNodeFromGridPane(affineGrid, 5, i);
      TextField x1 = (TextField) UIHelper.getNodeFromGridPane(affineGrid, 6, i);
      a00.setText(String.valueOf(affine.getMatrix().geta00()));
      a01.setText(String.valueOf(affine.getMatrix().geta01()));
      a10.setText(String.valueOf(affine.getMatrix().geta10()));
      a11.setText(String.valueOf(affine.getMatrix().geta11()));
      x0.setText(String.valueOf(affine.getVector().getX0()));
      x1.setText(String.valueOf(affine.getVector().getX1()));
    }
  }

}
