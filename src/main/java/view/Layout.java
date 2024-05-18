package view;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import controller.ChaosGameController;
import javafx.stage.Screen;
import util.Utility;

public class Layout {
  private ChaosGameController controller = new ChaosGameController();

  public ScrollPane createLeftsideScrollPane() {
    VBox leftSide = new VBox(10);
    leftSide.setPadding(new Insets(10));
    ScrollPane scrollPane = new ScrollPane(leftSide);
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide horizontal scrollbar
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide vertical scrollbar
    return scrollPane;
  }

  public void addTransformationOptions(VBox transformationBox, ToggleGroup transformationsGroup){
    // Create and add radio buttons
    for (String[] row : Utility.TRANSFORMATIONS) {
      HBox rowBox = new HBox(10);
      rowBox.setAlignment(Pos.CENTER);
      for (String label : row) {
        RadioButton radioButton = controller.createRadioButton(transformationsGroup, label);
        rowBox.getChildren().add(radioButton);
      }
      transformationBox.getChildren().add(rowBox);
    }
  }

  public VBox createStepsBox(){
    VBox stepsBox = new VBox(5);
    stepsBox.setAlignment(Pos.CENTER);

    Label stepsLabel = new Label("Steps");
    stepsLabel.getStyleClass().add("bold-label");

    // Configure the slider
    Slider stepsSlider = new Slider(0, 10000000, 0); // Min, Max, Initial value
    stepsSlider.setShowTickMarks(true);
    stepsSlider.setMajorTickUnit(1000000);
    stepsSlider.setMinorTickCount(4);
    stepsSlider.setBlockIncrement(100000); // Block increment for faster sliding

    // Label to display the current value of the slider
    Label stepsValueLabel = new Label("0");
    stepsValueLabel.getStyleClass().add(Utility.SMALL_LABEL);

    stepsBox.getChildren().addAll(stepsLabel, stepsSlider, stepsValueLabel);
    return stepsBox;
  }

  public GridPane createCoordGrid(){
    GridPane coordGrid = new GridPane();
    coordGrid.setHgap(10);
    coordGrid.setVgap(10);
    Label minCoordLabel = new Label("Min.Coord");
    minCoordLabel.getStyleClass().add(Utility.SMALL_LABEL);
    Label maxCoordLabel = new Label("Max.Coord");
    maxCoordLabel.getStyleClass().add(Utility.SMALL_LABEL);
    coordGrid.add(minCoordLabel, 0, 0);
    coordGrid.add(maxCoordLabel, 2, 0);

    TextField minXField = controller.createDecimalTextField("-4");
    TextField minYField = controller.createDecimalTextField("-1");
    TextField maxXField = controller.createDecimalTextField("4");
    TextField maxYField = controller.createDecimalTextField("10");

    coordGrid.addRow(1, minXField, minYField, maxXField, maxYField);
    return coordGrid;
  }

  public GridPane createJuliaGrid(){
    GridPane juliaGrid = new GridPane();
    juliaGrid.setHgap(10);
    juliaGrid.setVgap(10);
    Label juliaLabel = new Label("Julia-constant");
    juliaLabel.getStyleClass().add(Utility.SMALL_LABEL);
    juliaGrid.add(juliaLabel, 0, 0, 2, 1);

    ToggleButton juliaToggleSwitch = new ToggleButton();
    juliaToggleSwitch.getStyleClass().add("toggle-switch");
    if (juliaToggleSwitch.isSelected()) {
      juliaToggleSwitch.setText("Use Convergence Iteration mode (click to change)");
    } else {
      juliaToggleSwitch.setText("Use Steps mode (click to change)");
    }

    juliaToggleSwitch.setMaxWidth(Double.MAX_VALUE);
    juliaGrid.setHgrow(juliaToggleSwitch, Priority.ALWAYS);
    juliaGrid.add(juliaToggleSwitch, 0, 1, 4, 1);

    TextField realPartField = controller.createDecimalTextField("0.0");
    TextField imaginaryPartField = controller.createDecimalTextField("0.0");
    juliaGrid.addRow(2, realPartField, imaginaryPartField);
    return juliaGrid;
  }


  public VBox createAffineBox(){
    VBox affineBox = new VBox(10);
    Label affineMatrixAndVectorLabel = new Label("Affine matrices and vectors");
    affineMatrixAndVectorLabel.getStyleClass().add(Utility.SMALL_LABEL);
    affineBox.getChildren().add(affineMatrixAndVectorLabel);
    GridPane affineGrid = new GridPane();
    affineGrid.setHgap(10);
    affineGrid.setVgap(10);

    HBox buttonsBox = new HBox(10);
    Button addButton = new Button("Add");
    Button removeButton = new Button("Remove");
    buttonsBox.getChildren().addAll(addButton, removeButton);

    affineBox.getChildren().addAll(affineGrid, buttonsBox);
    return affineBox;
  }

  public void setupLeftSideWithSeperatorLine(ScrollPane scrollPane, VBox leftSide, BorderPane root){
    scrollPane.setContent(leftSide);
    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    scrollPane.setPrefWidth(screenWidth * 0.25);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);

    Separator separator = new Separator();
    separator.setOrientation(Orientation.VERTICAL);

    HBox leftLayout = new HBox(scrollPane, separator);
    leftLayout.setHgrow(scrollPane, Priority.ALWAYS);
    root.setLeft(leftLayout);
  }





}
