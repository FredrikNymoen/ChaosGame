package gui;

import chaosGame.ChaosGame;
import chaosGame.ChaosGameDescription;
import filehandling.ChaosGameFileHandler;
import filehandling.SettingsHandler;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import transformations.AffineTransform2D;
import transformations.JuliaTransform;

public class MainGUI extends Application implements ChaosGameObserver{
  private VBox leftSide;
  private ScrollPane scrollPane; // ScrollPane for the left side
  private VBox transformationBox;
  private ToggleGroup transformationsGroup;
  private VBox stepsBox;
  private Slider stepsSlider;
  private GridPane coordGrid;
  private GridPane juliaGrid;
  private VBox affineBox; // Container for the affine transformation section
  private GridPane affineGrid; // This needs to be accessible by the button's event handler
  private Button showButton;
  private Button iterativeTransformationButton;
  private Canvas fractalCanvas; // Canvas for drawing the fractal
  private GraphicsContext gc; // GraphicsContext for fractalCanvas

  //private TextField realPartField;
  //private TextField imaginaryPartField;
  private ChaosGameController controller = new ChaosGameController();
  private ChaosGame currentChaosGame;
  


  private CheckBox colorModeCheckbox;

  private Label missingInputMessage;
  private final String settingsFilePath = "appSettings.properties";
  private Button copyLastTransformationButton;
  private ToggleButton toggleSwitch;
  private SettingsHandler settingsHandler = new SettingsHandler(settingsFilePath);


  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    BorderPane root = new BorderPane();
    root.setPadding(new Insets(10));

    configureScrollPane();
    configureTransformationButtonOptions();
    configureStepsSlider();
    configureCoordinateFields();
    configureJuliaFields();
    configureAffineControls();
    configureColorModeCheckbox();
    configureIterativeTransformationButton();
    configureShowButton();
    configureMissingInputMessage();
    configureCopyLastTransformationButton();

    setupLeftSide(root);
    setupRightSide(root);
    setupListeners();

    primaryStage.setOnCloseRequest(event -> saveSettings());
    loadSettings();

    Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass().getResource("/chaosgame.css").toExternalForm());
    primaryStage.setTitle("Chaos game");
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true); // Set the stage to be maximized
    primaryStage.show();
  }

  private void configureCopyLastTransformationButton() {
    copyLastTransformationButton = new Button("Copy Last Shown Transformation");
    copyLastTransformationButton.getStyleClass().add("option-button");
    copyLastTransformationButton.setOnAction(event -> {
      copyLastTransformation();
    });
  }

  public void copyLastTransformation() {
    ChaosGameFileHandler fileHandler = new ChaosGameFileHandler();
    if(fileHandler.checkForMandelbrot("file.csv")){
      transformationsGroup.selectToggle(transformationsGroup.getToggles().get(4));
    }
    else {
      ChaosGameDescription lastDescription = fileHandler.readFromFile("file.csv");
      TextField[] coordinateFields = controller.getCoordinateTextFields(coordGrid);
      coordinateFields[0].setText(String.valueOf(lastDescription.getMinCoords().getX0()));
      coordinateFields[1].setText(String.valueOf(lastDescription.getMinCoords().getX1()));
      coordinateFields[2].setText(String.valueOf(lastDescription.getMaxCoords().getX0()));
      coordinateFields[3].setText(String.valueOf(lastDescription.getMaxCoords().getX1()));

      if (lastDescription.getTransforms().get(0) instanceof AffineTransform2D) {
        switch (fileHandler.readTransformationType("file.csv")) {
          case "affine":
            transformationsGroup.selectToggle(transformationsGroup.getToggles().get(0));
            copyLastAffineTransformation(lastDescription);
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
        if(fileHandler.readTransformationType("file.csv").equals("convergence-mode")){
          toggleSwitch.setSelected(true);
          toggleSwitch.setText("Use Convergence Iteration mode (click to change)");
        }
        else{
          toggleSwitch.setSelected(false);
          toggleSwitch.setText("Use Steps mode (click to change)");
        }
        transformationsGroup.selectToggle(transformationsGroup.getToggles().get(2));

        TextField[] coordinateFieldsJulia = controller.getJuliaTextFields(juliaGrid);
        TextField realPartField = coordinateFieldsJulia[0];
        TextField imaginaryPartField = coordinateFieldsJulia[1];
        realPartField.setText(
            ((JuliaTransform) lastDescription.getTransforms().get(0)).getPoint().getX0() + "");
        imaginaryPartField.setText(
            ((JuliaTransform) lastDescription.getTransforms().get(0)).getPoint().getX1() + "");
      }
    }
  }

  private void copyLastAffineTransformation(ChaosGameDescription lastDescription) {
    while(affineGrid.getRowCount()!=1){
      removeMatrixVectorRow();
    }
    for(int i=0; i< lastDescription.getTransforms().size(); i++){
      if (i > 0) {
        addMatrixVectorRow(i);
      }
      AffineTransform2D affine = (AffineTransform2D) lastDescription.getTransforms().get(i);
      TextField a00 = (TextField) controller.getNodeFromGridPane(affineGrid, 0, i);
      TextField a01 = (TextField) controller.getNodeFromGridPane(affineGrid, 1, i);
      TextField a10 = (TextField) controller.getNodeFromGridPane(affineGrid, 2, i);
      TextField a11 = (TextField) controller.getNodeFromGridPane(affineGrid, 3, i);
      TextField x0 = (TextField) controller.getNodeFromGridPane(affineGrid, 5, i);
      TextField x1 = (TextField) controller.getNodeFromGridPane(affineGrid, 6, i);
      a00.setText(String.valueOf(affine.getMatrix().geta00()));
      a01.setText(String.valueOf(affine.getMatrix().geta01()));
      a10.setText(String.valueOf(affine.getMatrix().geta10()));
      a11.setText(String.valueOf(affine.getMatrix().geta11()));
      x0.setText(String.valueOf(affine.getVector().getX0()));
      x1.setText(String.valueOf(affine.getVector().getX1()));
    }
  }

  private void configureScrollPane() {
    // Scrollable Left side layout
    leftSide = new VBox(10);
    leftSide.setPadding(new Insets(10));
    scrollPane = new ScrollPane(leftSide);
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide horizontal scrollbar
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide vertical scrollbar
  }

  private void configureTransformationButtonOptions() {
    // Transformation checkboxes
    transformationBox = new VBox(5);
    transformationBox.setAlignment(Pos.CENTER);
    Label transformationLabel = new Label("Transformations");
    transformationLabel.getStyleClass().add("bold-label");
    transformationBox.getChildren().add(transformationLabel);
    transformationsGroup = new ToggleGroup();

    String[][] transformations = {
        {"Affine", "Barnsley", "Julia", "Sierpinski"},
        {"Mandelbrot", "Maple-Tree"}
    };

    // Create and add radio buttons
    for (String[] row : transformations) {
      HBox rowBox = new HBox(10);
      rowBox.setAlignment(Pos.CENTER);
      for (String label : row) {
        RadioButton radioButton = controller.createRadioButton(transformationsGroup, label);
        rowBox.getChildren().add(radioButton);
      }
      transformationBox.getChildren().add(rowBox);
    }
  }


  private void configureStepsSlider() {
    // Steps input
    stepsBox = new VBox(5);
    stepsBox.setAlignment(Pos.CENTER);

    Label stepsLabel = new Label("Steps");
    stepsLabel.getStyleClass().add("bold-label");

    // Configure the slider
    stepsSlider = new Slider(0, 10000000, 0); // Min, Max, Initial value
    stepsSlider.setShowTickMarks(true);
    stepsSlider.setMajorTickUnit(1000000);
    stepsSlider.setMinorTickCount(4);
    stepsSlider.setBlockIncrement(100000); // Block increment for faster sliding

    // Label to display the current value of the slider
    Label stepsValueLabel = new Label("0");
    stepsValueLabel.getStyleClass().add("small-label");

    // Bind the slider's value to the label
    stepsSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
      onSliderValueChanged(newValue.intValue());
    });

    stepsBox.getChildren().addAll(stepsLabel, stepsSlider, stepsValueLabel);
  }

  @Override
  public void onSliderValueChanged(int newValue) {
    Label stepsValueLabel;
    stepsValueLabel = (Label) stepsBox.getChildren().get(2);
    stepsValueLabel.setText(String.format("%,d", newValue)); // Format the number with commas
  }


  private void configureCoordinateFields() {
    coordGrid = new GridPane();
    coordGrid.setHgap(10);
    coordGrid.setVgap(10);
    Label minCoordLabel = new Label("Min.Coord");
    minCoordLabel.getStyleClass().add("small-label");
    Label maxCoordLabel = new Label("Max.Coord");
    maxCoordLabel.getStyleClass().add("small-label");
    coordGrid.add(minCoordLabel, 0, 0);
    coordGrid.add(maxCoordLabel, 2, 0);

    TextField minXField = controller.createDecimalTextField("-4");
    TextField minYField = controller.createDecimalTextField("-1");
    TextField maxXField = controller.createDecimalTextField("4");
    TextField maxYField = controller.createDecimalTextField("10");

    coordGrid.addRow(1, minXField, minYField, maxXField, maxYField);
  }

  private void configureJuliaFields() {
    juliaGrid = new GridPane();
    juliaGrid.setHgap(10);
    juliaGrid.setVgap(10);
    Label juliaLabel = new Label("Julia-constant");
    juliaLabel.getStyleClass().add("small-label");
    juliaGrid.add(juliaLabel, 0, 0, 2, 1);

    toggleSwitch = new ToggleButton();
    toggleSwitch.getStyleClass().add("toggle-switch");
    if (toggleSwitch.isSelected()) {
      toggleSwitch.setText("Use Convergence Iteration mode (click to change)");
    } else {
    toggleSwitch.setText("Use Steps mode (click to change)");
    }
    // Add an event handler to change the text when the button is toggled
    toggleSwitch.setOnAction(event -> {
      if (toggleSwitch.isSelected()) {
        toggleSwitch.setText("Use Convergence Iteration mode (click to change)");        stepsBox.setDisable(true);
        coordGrid.setDisable(true);
      } else {
        toggleSwitch.setText("Use Steps mode (click to change)");
        stepsBox.setDisable(false);
        coordGrid.setDisable(false);
      }
    });
    toggleSwitch.setMaxWidth(Double.MAX_VALUE);
    juliaGrid.setHgrow(toggleSwitch, Priority.ALWAYS);
    juliaGrid.add(toggleSwitch, 0, 1, 4, 1);

    TextField[] coordinateFieldsJulia = controller.getJuliaTextFields(juliaGrid);
    TextField realPartField = coordinateFieldsJulia[0];
    TextField imaginaryPartField = coordinateFieldsJulia[1];
    realPartField = controller.createDecimalTextField("0.0");
    imaginaryPartField = controller.createDecimalTextField("0.0");
    juliaGrid.addRow(2, realPartField, imaginaryPartField);
  }

  private void configureAffineControls() {
    affineBox = new VBox(10);
    Label affineMatrixAndVectorLabel = new Label("Affine matrices and vectors");
    affineMatrixAndVectorLabel.getStyleClass().add("small-label");
    affineBox.getChildren().add(affineMatrixAndVectorLabel);
    affineGrid = new GridPane();
    affineGrid.setHgap(10);
    affineGrid.setVgap(10);
    addMatrixVectorRow(0);

    HBox buttonsBox = new HBox(10);
    Button addButton = new Button("Add");
    addButton.setOnAction(event -> addMatrixVectorRow(affineGrid.getRowCount()));
    Button removeButton = new Button("Remove");
    removeButton.setOnAction(event -> removeMatrixVectorRow());
    buttonsBox.getChildren().addAll(addButton, removeButton);

    affineBox.getChildren().addAll(affineGrid, buttonsBox);
  }

  public void configureMissingInputMessage() {
    missingInputMessage = new Label("Please fill in all required fields.");
    missingInputMessage.setStyle("-fx-text-fill: red;");
    missingInputMessage.setVisible(false);
  }

  private void configureShowButton() {
    showButton = new Button("Show");
    showButton.getStyleClass().add("show-button");
    showButton.setOnAction(event -> {
      boolean allFieldsValid = isAllFieldsValid();

      // Proceed only if all fields are valid
      if (allFieldsValid) {
        int steps = (int) stepsSlider.getValue();
        // Reset all fields to default style
        resetAllToDefaultStyle();
        currentChaosGame = controller.handleTransformationSelection(transformationsGroup,
                affineGrid, juliaGrid, coordGrid, steps);
        drawFractal(currentChaosGame);
      } else {
        System.err.println("Please correct the highlighted errors.");
      }
    });
  }

  private boolean isAllFieldsValid() {
    boolean allFieldsValid = true;

    TextField[] coordinateFieldsJulia = controller.getJuliaTextFields(juliaGrid);
    TextField realPartField = coordinateFieldsJulia[0];
    TextField imaginaryPartField = coordinateFieldsJulia[1];

    List<String> missingInputs = controller.checkForEmptyFields(transformationsGroup,
            affineGrid, realPartField, imaginaryPartField);

    resetAllToDefaultStyle();  // Reset all fields to default style

    // Handle each case of missing inputs to update the GUI
    for (String notFilled : missingInputs) {
      allFieldsValid = false; // Mark as invalid since there's an error
      if (notFilled.startsWith("(")){
        String[] parts = notFilled.split("[() ,]+");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);
        TextField textField = (TextField) controller.getNodeFromGridPane(affineGrid, col, row);
        textField.setStyle("-fx-border-color: red;");
      } else {
        switch (notFilled) {
          case "Real part":
            realPartField.setStyle("-fx-border-color: red;");
            break;
          case "Imaginary part":
            imaginaryPartField.setStyle("-fx-border-color: red;");
            break;
        }
      }
    }

    TextField[] coordinateFields = controller.getCoordinateTextFields(coordGrid);
    TextField minXField = coordinateFields[0];
    TextField minYField = coordinateFields[1];
    TextField maxXField = coordinateFields[2];
    TextField maxYField = coordinateFields[3];

    // Validate and parse minimum coordinates
    if (minXField.getText().trim().isEmpty() || minYField.getText().trim().isEmpty()) {
      minXField.setStyle("-fx-border-color: red;");
      minYField.setStyle("-fx-border-color: red;");
      allFieldsValid = false;
    }
    else {
      minXField.setStyle("");
      minYField.setStyle("");
    }

    // Validate and parse maximum coordinates
    if (maxXField.getText().trim().isEmpty() || maxYField.getText().trim().isEmpty()) {
      maxXField.setStyle("-fx-border-color: red;");
      maxYField.setStyle("-fx-border-color: red;");
      allFieldsValid = false;
    }
    else {
      maxXField.setStyle("");
      maxYField.setStyle("");
    }
    missingInputMessage.setVisible(!allFieldsValid);
    return allFieldsValid;
  }

  public void resetAllToDefaultStyle() {
    // Reset all fields to default style
    for (Node node : affineGrid.getChildren()) {
      if (node instanceof TextField) {
        node.setStyle("");
      }
    }
    TextField[] coordinateFieldsJulia = controller.getJuliaTextFields(juliaGrid);
    TextField realPartField = coordinateFieldsJulia[0];
    TextField imaginaryPartField = coordinateFieldsJulia[1];

    affineGrid.setStyle("");
    affineBox.setStyle("");
    realPartField.setStyle("");
    imaginaryPartField.setStyle("");
  }


  private void setupLeftSide(BorderPane root) {
    leftSide.getStyleClass().add("left-side");
    VBox spacingBox = new VBox();
    spacingBox.setMinHeight(10);

    HBox centeredShowButtonBox = controller.createCenteredHBox(showButton);
    HBox centeredColorModeCheckboxBox = controller.createCenteredHBox(colorModeCheckbox);
    HBox centeredIterativeTransformationBox = controller.createCenteredHBox(iterativeTransformationButton);
    HBox centeredCopyTransformationButtonBox = controller.createCenteredHBox(copyLastTransformationButton);

    leftSide.getChildren().addAll(
        transformationBox,
        stepsBox,
        coordGrid,
        juliaGrid,
        affineBox,
        centeredShowButtonBox,
        centeredIterativeTransformationBox,
        missingInputMessage,
        spacingBox,
        centeredColorModeCheckboxBox,
        centeredCopyTransformationButtonBox
    );

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

  private void setupRightSide(BorderPane root) {
    fractalCanvas = new Canvas();
    gc = fractalCanvas.getGraphicsContext2D();

    fractalCanvas.widthProperty().bind(root.widthProperty().subtract(scrollPane.getPrefWidth()));
    fractalCanvas.heightProperty().bind(root.heightProperty());

    root.setRight(fractalCanvas);
  }

  @Override
  public void onCanvasSizeChanged(){
    if (currentChaosGame != null) {
      drawFractal(currentChaosGame);
    }
  }

  private void configureColorModeCheckbox() {
    colorModeCheckbox = new CheckBox("Enable Heatmap Color Mode");
    colorModeCheckbox.setSelected(false);
    colorModeCheckbox.getStyleClass().add("heatmap-checkbox");
  }

  private void configureIterativeTransformationButton() {
    iterativeTransformationButton = new Button("Make fractal with Iterative Transformation mode");
    iterativeTransformationButton.getStyleClass().add("option-button");
    iterativeTransformationButton.getStyleClass().add("iterativeTransformation-button");
    iterativeTransformationButton.setOnAction(event -> {
      ChaosGame game = controller.handleTransformationSelection(transformationsGroup, affineGrid, juliaGrid, coordGrid, (int) stepsSlider.getValue());
      game.makeFullFractal();
      drawFractal(game);
    });
  }

  private void addMatrixVectorRow(int row) {
    String[] matrixPlaceholders = {"a00", "a01", "a10", "a11"};
    for (int i = 0; i < matrixPlaceholders.length; i++) {
      TextField matrixField = controller.createDecimalTextField("0.0");
      matrixField.setPrefWidth(50);
      matrixField.setPromptText(matrixPlaceholders[i]);
      affineGrid.add(matrixField, i, row);
    }

    // Space between matrix and vector elements
    Pane spacer = new Pane();
    spacer.setMinSize(20, 1);
    affineGrid.add(spacer, 4, row);

    // Vector elements with placeholders
    TextField vectorFieldX = controller.createDecimalTextField("0.0");
    vectorFieldX.setPrefWidth(50);
    vectorFieldX.setPromptText("x0");
    affineGrid.add(vectorFieldX, 5, row);

    TextField vectorFieldY = controller.createDecimalTextField("0.0");
    vectorFieldY.setPrefWidth(50);
    vectorFieldY.setPromptText("y0");
    affineGrid.add(vectorFieldY, 6, row);
  }

  private void removeMatrixVectorRow() {
    int lastRowIndex = affineGrid.getRowCount() - 1;
    if (lastRowIndex >= 1) {
      // Remove all elements in the last row
      for (int i = 0; i < 7; i++) {
        Node node = controller.getNodeFromGridPane(affineGrid, i, lastRowIndex);
        affineGrid.getChildren().remove(node);
      }
    }
  }

  private void setupListeners() {
    fractalCanvas.widthProperty().addListener(obs -> onCanvasSizeChanged());
    fractalCanvas.heightProperty().addListener(obs -> onCanvasSizeChanged());
    initializeRadioButtonListener();
  }

  private void initializeRadioButtonListener(){
    affineBox.setDisable(true);
    juliaGrid.setDisable(true);
    transformationsGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
      if (newToggle != null) {
        RadioButton selectedButton = (RadioButton) newToggle;
        onTransformationSelected(selectedButton.getText());
      }
    });
  }

  @Override
  public void onTransformationSelected(String transformation){
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
        iterativeTransformationButton.setDisable(false);
        if (toggleSwitch.isSelected()) {
          stepsBox.setDisable(true);
          coordGrid.setDisable(true);
        } else {
          stepsBox.setDisable(false);
          coordGrid.setDisable(false);
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

  private void drawFractal(ChaosGame chaosGame){
    gc.clearRect(0, 0, fractalCanvas.getWidth(), fractalCanvas.getHeight());

    int[][] canvasArray = chaosGame.getCanvas().getCanvasArray();
    // Use a set to avoid duplicate values and then convert to a list to sort
    Set<Integer> valueSet = new HashSet<>();
    for (int[] row : canvasArray) {
      for (int value : row) {
        if (value > 0) { // assuming value 0 means no data
          valueSet.add(value);
        }
      }
    }
    List<Integer> sortedValues = new ArrayList<>(valueSet);
    Collections.sort(sortedValues);

    // Dynamically calculate the center of the canvas
    double centerX = fractalCanvas.getWidth() / 2;
    double centerY = fractalCanvas.getHeight() / 2;

    // Assuming the fractal drawing's size for positioning
    double fractalWidth = canvasArray[0].length;
    double fractalHeight = canvasArray.length;

    // Calculate the top-left corner of where the fractal should be drawn
    double startX = centerX - fractalWidth / 2;
    double startY = centerY - fractalHeight / 2;

    // Adjust the drawing loop to position the fractal correctly
    for (int i = 0; i < fractalHeight; i++) {
      for (int j = 0; j < fractalWidth; j++) {

        int value = canvasArray[i][j];
        if(colorModeCheckbox.isSelected() && value > 0){
          int index = sortedValues.indexOf(value);
          if(sortedValues.get(index) == 1){
            gc.setFill(Color.BLUE);
          } else {
            double intensity = (double) index / (sortedValues.size() - 1);
            Color color = controller.getColorForValue(intensity);
            gc.setFill(color);
          }
        }
        else if (value > 0) { // assuming value 0 means no data
          gc.setFill(Color.BLACK);
        }
        else {
          gc.setFill(Color.WHITE); // Background color
        }
        gc.fillRect(startX + j, startY + i, 1, 1); // Draw pixel
      }
    }
  }


  private void loadSettings() {
    TextField[] coordinateFields = controller.getCoordinateTextFields(coordGrid);
    TextField minXField = coordinateFields[0];
    TextField minYField = coordinateFields[1];
    TextField maxXField = coordinateFields[2];
    TextField maxYField = coordinateFields[3];
    TextField[] coordinateFieldsJulia = controller.getJuliaTextFields(juliaGrid);
    TextField realPartField = coordinateFieldsJulia[0];
    TextField imaginaryPartField = coordinateFieldsJulia[1];

    Properties appSettings = settingsHandler.loadSettings();
    minXField.setText(appSettings.getProperty("minX", "-4"));
    minYField.setText(appSettings.getProperty("minY", "-1"));
    maxXField.setText(appSettings.getProperty("maxX", "4"));
    maxYField.setText(appSettings.getProperty("maxY", "10"));
    stepsSlider.setValue(Double.parseDouble(appSettings.getProperty("steps", "0")));
    realPartField.setText(appSettings.getProperty("realPart", "0.285"));
    imaginaryPartField.setText(appSettings.getProperty("imaginaryPart", "0.01"));
    try {
      RadioButton selectedButton = (RadioButton) transformationsGroup.getToggles().stream()
          .filter(
              t -> t.getUserData().equals(appSettings.getProperty("transformation", "Affine")))
          .findFirst().orElse(null);
      if (selectedButton != null) {
        transformationsGroup.selectToggle(selectedButton);
      }
    } catch (Exception e) {
      System.out.println("Failed to select transformation: " + e.getMessage());
    }
    colorModeCheckbox.setSelected(Boolean.parseBoolean(appSettings.getProperty("colorMode", "false")));
    toggleSwitch.setSelected(Boolean.parseBoolean(appSettings.getProperty("juliaToggleSwitch", "false")));
  }

  private void saveSettings() {
    TextField[] coordinateFields = controller.getCoordinateTextFields(coordGrid);
    TextField minXField = coordinateFields[0];
    TextField minYField = coordinateFields[1];
    TextField maxXField = coordinateFields[2];
    TextField maxYField = coordinateFields[3];
    TextField[] coordinateFieldsJulia = controller.getJuliaTextFields(juliaGrid);
    TextField realPartField = coordinateFieldsJulia[0];
    TextField imaginaryPartField = coordinateFieldsJulia[1];

    Properties appSettings = new Properties();
    appSettings.setProperty("minX", minXField.getText());
    appSettings.setProperty("minY", minYField.getText());
    appSettings.setProperty("maxX", maxXField.getText());
    appSettings.setProperty("maxY", maxYField.getText());
    appSettings.setProperty("steps", stepsSlider.getValue() + "");
    appSettings.setProperty("realPart", realPartField.getText());
    appSettings.setProperty("imaginaryPart", imaginaryPartField.getText());
    appSettings.setProperty("transformation", ((RadioButton) transformationsGroup.getSelectedToggle()).getText());
    appSettings.setProperty("colorMode", String.valueOf(colorModeCheckbox.isSelected()));
    appSettings.setProperty("juliaToggleSwitch", String.valueOf(toggleSwitch.isSelected()));
    settingsHandler.saveSettings(appSettings);
  }

}