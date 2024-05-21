package view;

import java.util.function.UnaryOperator;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.util.converter.DoubleStringConverter;
import util.Utility;

/**
 * This class provides methods to create and configure the layout of the UI components for the Chaos
 * Game application.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class Layout {

  /**
   * Creates a ScrollPane for the left side of the UI.
   *
   * @return a configured ScrollPane instance
   */
  public ScrollPane createLeftsideScrollPane() {
    VBox leftSide = new VBox(10);
    leftSide.setPadding(new Insets(10));
    ScrollPane scrollPane = new ScrollPane(leftSide);
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide horizontal scrollbar
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide vertical scrollbar
    return scrollPane;
  }

  /**
   * Creates a VBox for the transformation options.
   *
   * @return a configured VBox instance
   */
  public VBox createTransformationsBox() {
    VBox transformationBox = new VBox(5);
    transformationBox.setAlignment(Pos.CENTER);
    Label transformationLabel = new Label("Transformations");
    transformationLabel.getStyleClass().add(Utility.BOLD_LABEL);
    transformationBox.getChildren().add(transformationLabel);
    return transformationBox;
  }

  /**
   * Adds transformation options to the specified VBox.
   *
   * @param transformationBox    the VBox to which the transformation options are added
   * @param transformationsGroup the ToggleGroup for the transformation options
   */
  public void addTransformationOptions(VBox transformationBox, ToggleGroup transformationsGroup) {
    // Create and add radio buttons
    for (String[] row : Utility.TRANSFORMATIONS) {
      HBox rowBox = new HBox(10);
      rowBox.setAlignment(Pos.CENTER);
      for (String label : row) {
        RadioButton radioButton = createRadioButton(transformationsGroup, label);
        rowBox.getChildren().add(radioButton);
      }
      transformationBox.getChildren().add(rowBox);
    }
  }

  /**
   * Creates a VBox for the steps slider and label.
   *
   * @return a configured VBox instance
   */
  public VBox createStepsBox() {
    VBox stepsBox = new VBox(5);
    stepsBox.setAlignment(Pos.CENTER);

    Label stepsLabel = new Label("Steps");
    stepsLabel.getStyleClass().add(Utility.BOLD_LABEL);

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

  /**
   * Creates a GridPane for the coordinate inputs.
   *
   * @return a configured GridPane instance
   */
  public GridPane createCoordGrid() {
    GridPane coordGrid = new GridPane();
    coordGrid.setHgap(10);
    coordGrid.setVgap(10);
    Label minCoordLabel = new Label("Min.Coord");
    minCoordLabel.getStyleClass().add(Utility.SMALL_LABEL);
    Label maxCoordLabel = new Label("Max.Coord");
    maxCoordLabel.getStyleClass().add(Utility.SMALL_LABEL);
    coordGrid.add(minCoordLabel, 0, 0);
    coordGrid.add(maxCoordLabel, 2, 0);

    TextField minX = createDecimalTextField("-4");
    TextField minY = createDecimalTextField("-1");
    TextField maxX = createDecimalTextField("4");
    TextField maxY = createDecimalTextField("10");

    coordGrid.addRow(1, minX, minY, maxX, maxY);
    return coordGrid;
  }

  /**
   * Creates a GridPane for the Julia set inputs.
   *
   * @return a configured GridPane instance
   */
  public GridPane createJuliaGrid() {
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
    GridPane.setHgrow(juliaToggleSwitch, Priority.ALWAYS);
    juliaGrid.add(juliaToggleSwitch, 0, 1, 4, 1);

    TextField realPartField = createDecimalTextField("0.0");
    TextField imaginaryPartField = createDecimalTextField("0.0");
    juliaGrid.addRow(2, realPartField, imaginaryPartField);
    return juliaGrid;
  }

  /**
   * Creates a VBox for the affine transformation inputs.
   *
   * @return a configured VBox instance
   */
  public VBox createAffineBox() {
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

  /**
   * Creates a button for initiating iterative transformation mode.
   *
   * @return a configured Button instance
   */
  public Button createIterativeTransformationButton() {
    Button iterativeTransformationButton = new Button(
        "Make fractal with Iterative Transformation mode");
    iterativeTransformationButton.getStyleClass().add(Utility.OPTION_BUTTON);
    iterativeTransformationButton.getStyleClass().add("iterativeTransformation-button");
    return iterativeTransformationButton;
  }

  /**
   * Creates a button for copying the last transformation.
   *
   * @return a configured Button instance
   */
  public Button createCopyLastTransformationButton() {
    Button copyLastTransformationButton = new Button("Copy Last Shown Transformation");
    copyLastTransformationButton.getStyleClass().add(Utility.OPTION_BUTTON);
    copyLastTransformationButton.getStyleClass().add("copy-button");
    return copyLastTransformationButton;
  }

  /**
   * Sets up the left side of the UI with a separator line.
   *
   * @param scrollPane the ScrollPane to be added to the left side
   * @param leftSide   the VBox containing the left side UI elements
   * @param root       the BorderPane root layout
   */
  public void setupLeftSideWithSeperatorLine(ScrollPane scrollPane, VBox leftSide,
      BorderPane root) {
    scrollPane.setContent(leftSide);
    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    scrollPane.setPrefWidth(screenWidth * 0.25);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);

    Separator separator = new Separator();
    separator.setOrientation(Orientation.VERTICAL);

    HBox leftLayout = new HBox(scrollPane, separator);
    HBox.setHgrow(scrollPane, Priority.ALWAYS);
    root.setLeft(leftLayout);
  }

  /**
   * Creates a TextField with a decimal number filter and a default value.
   *
   * @param defaultValue the default value for the TextField
   * @return a configured TextField instance
   */
  public TextField createDecimalTextField(String defaultValue) {
    TextField textField = new TextField(defaultValue);
    UnaryOperator<Change> decimalFilter = change ->
        change.getControlNewText().matches("-?((\\d*)|(\\d+\\.\\d*))") ? change : null;
    textField.setTextFormatter(
        new TextFormatter<>(new DoubleStringConverter(), Double.parseDouble(defaultValue),
            decimalFilter));
    return textField;
  }

  /**
   * Creates a centered HBox containing the specified Node.
   *
   * @param node the Node to be centered within the HBox
   * @return a configured HBox instance
   */
  public HBox createCenteredHBox(Node node) {
    HBox hbox = new HBox(node);
    hbox.setAlignment(Pos.CENTER);
    return hbox;
  }

  /**
   * Creates a RadioButton with the specified label and adds it to the specified ToggleGroup.
   *
   * @param toggleGroup the ToggleGroup to which the RadioButton is added
   * @param label       the label for the RadioButton
   * @return a configured RadioButton instance
   */
  public RadioButton createRadioButton(ToggleGroup toggleGroup, String label) {
    RadioButton radioButton = new RadioButton(label);
    radioButton.setUserData(label);
    radioButton.setToggleGroup(toggleGroup);
    return radioButton;
  }
}
