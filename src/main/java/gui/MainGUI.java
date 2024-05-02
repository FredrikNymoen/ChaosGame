package gui;

import chaosGame.ChaosGame;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import mathcore.Vector2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import java.util.function.UnaryOperator;

public class MainGUI extends Application {
  private GridPane affineGrid; // This needs to be accessible by the button's event handler
  private ToggleGroup transformationsGroup;
  private ScrollPane scrollPane; // ScrollPane for the left side
  private Canvas fractalCanvas; // Canvas for drawing the fractal
  private GraphicsContext gc; // GraphicsContext for fractalCanvas
  private TextField minXField;
  private TextField minYField;
  private TextField maxXField;
  private TextField maxYField;
  private TextField stepsField;
  private TextField realPartField;
  private TextField imaginaryPartField;
  ChaosGameController controller = new ChaosGameController();
  private ChaosGame currentChaosGame;
  
  private VBox leftSide;
  private VBox transformationBox;
  private VBox stepsBox;
  private GridPane coordGrid;
  private GridPane juliaGrid;
  private VBox affineBox; // Container for the affine transformation section
  private Button showButton;
  private CheckBox colorModeCheckbox;  // Checkbox to toggle color mode
  private CheckBox makeFullFractalCheckbox; // Checkbox to toggle full fractal

  private Properties appSettings = new Properties();
  private Label missingInputMessage;
  private final String settingsFilePath = "appSettings.properties";


  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    BorderPane root = new BorderPane();
    root.setPadding(new Insets(10));

    configureScrollPane();
    configureTransformationButtonOptions();
    configureStepsInput();
    configureCoordinateFields();
    configureJuliaConstantFields();
    configureAffineControls();
    configureColorModeCheckbox();
    configureMakeFullFractalCheckbox();
    configureShowButton();
    configureMissingInputMessage();

    setupLeftSide(root);
    setupRightSide(root);
    setupListeners();

    primaryStage.setOnCloseRequest(event -> saveSettings());
    loadSettings();

    Scene scene = new Scene(root);
    primaryStage.setMaximized(true); // Set the stage to be maximized
    primaryStage.setTitle("Chaos game");
    primaryStage.setScene(scene);
    primaryStage.show();
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
    Label transformationLabel = new Label("Transformations");
    transformationBox.getChildren().add(transformationLabel);
    transformationsGroup = new ToggleGroup();
    RadioButton affine = new RadioButton("Affine");
    affine.setUserData("Affine");
    affine.setToggleGroup(transformationsGroup);
    RadioButton barnsley = new RadioButton("Barnsley");
    barnsley.setUserData("Barnsley");
    barnsley.setToggleGroup(transformationsGroup);
    RadioButton julia = new RadioButton("Julia");
    julia.setUserData("Julia");
    julia.setToggleGroup(transformationsGroup);
    RadioButton sierpinski = new RadioButton("Sierpinski");
    sierpinski.setUserData("Sierpinski");
    sierpinski.setToggleGroup(transformationsGroup);

    HBox transformationsBox = new HBox(10);
    transformationsBox.getChildren().addAll(affine, barnsley, julia, sierpinski);
    transformationBox.getChildren().add(transformationsBox);
  }

  private void configureStepsInput() {
    // Steps input
    stepsBox = new VBox(5);
    Label stepsLabel = new Label("Steps");
    stepsField = new TextField();
    stepsField.setPromptText("(0-10000000)");

    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
      String newText = change.getControlNewText();
      return newText.matches("([1-9][0-9]{0,6}|10000000|0)?") ? change : null;
    };

    stepsField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
    stepsBox.getChildren().addAll(stepsLabel, stepsField);
  }

  private void configureCoordinateFields() {
    coordGrid = new GridPane();
    coordGrid.setHgap(10);
    coordGrid.setVgap(10);
    coordGrid.add(new Label("Min. Coord"), 0, 0);
    coordGrid.add(new Label("Max. Coord"), 2, 0);

    minXField = createDecimalTextField("-4");
    minYField = createDecimalTextField("-1");
    maxXField = createDecimalTextField("4");
    maxYField = createDecimalTextField("10");

    coordGrid.addRow(1, minXField, minYField, maxXField, maxYField);
  }

  private void configureJuliaConstantFields() {
    juliaGrid = new GridPane();
    juliaGrid.setHgap(10);
    juliaGrid.setVgap(10);
    juliaGrid.add(new Label("Julia-constant"), 0, 0, 2, 1);

    realPartField = createDecimalTextField("0.28");
    imaginaryPartField = createDecimalTextField("0.9");
    juliaGrid.addRow(1, realPartField, imaginaryPartField);
  }

  private void configureAffineControls() {
    affineBox = new VBox(10);
    affineBox.getChildren().add(new Label("Affine matrices and vectors"));
    affineGrid = new GridPane();
    affineGrid.setHgap(10);
    affineGrid.setVgap(10);
    addMatrixVectorRow(0);

    HBox buttonsBox = new HBox(10);
    Button addButton = new Button("Add");
    addButton.setOnAction(event -> addMatrixVectorRow(affineGrid.getRowCount()));
    Button removeButton = new Button("Remove");
    removeButton.setOnAction(event -> removeMatrixVectorRow());
    HBox spacingBox = new HBox() {{ setPrefWidth(20); }};
    Button mapleTreeButton = new Button("Maple Tree Example");
    mapleTreeButton.setOnAction(event -> fillInAffineGridForMapleTree());
    buttonsBox.getChildren().addAll(addButton, removeButton,spacingBox, mapleTreeButton);

    affineBox.getChildren().addAll(affineGrid, buttonsBox);
  }

  public void configureMissingInputMessage() {
    missingInputMessage = new Label("Please fill in all required fields.");
    missingInputMessage.setStyle("-fx-text-fill: red;");
    missingInputMessage.setVisible(false);
  }

  private void configureShowButton() {
    showButton = new Button("Show");
    showButton.setOnAction(event -> {
      boolean allFieldsValid = true;
      double minX = 0, minY = 0, maxX = 0, maxY = 0;
      int steps = parseStepsSafely(stepsField.getText());

      List<String> missingInputs = controller.checkForEmptyFields(transformationsGroup,
              affineGrid, realPartField, imaginaryPartField, steps);

      resetAllToDefaultStyle();  // Reset all fields to default style

      // Handle each case of missing inputs to update the GUI
      for (String notFilled : missingInputs) {
        allFieldsValid = false; // Mark as invalid since there's an error
        if (notFilled.startsWith("Matrix element at") || notFilled.startsWith("Vector element at")) {
          String[] parts = notFilled.split("[() ,]+");
          int row = Integer.parseInt(parts[3]);
          int col = Integer.parseInt(parts[4]);
          TextField textField = (TextField) getNodeFromGridPane(affineGrid, col, row);
          textField.setStyle("-fx-border-color: red;");
        } else {
          switch (notFilled) {
            case "Please fill in the number of steps.":
              stepsField.setStyle("-fx-border-color: red;");
              break;
            case "Real part of the complex number is not a valid double.":
              realPartField.setStyle("-fx-border-color: red;");
              break;
            case "Imaginary part of the complex number is not a valid double.":
              imaginaryPartField.setStyle("-fx-border-color: red;");
              break;
            // Handle other general errors
          }
        }
      }

      // Validate and parse minimum coordinates
      if (minXField.getText().trim().isEmpty() || minYField.getText().trim().isEmpty()) {
        minXField.setStyle("-fx-border-color: red;");
        minYField.setStyle("-fx-border-color: red;");
        allFieldsValid = false;
      } else {
        try {
          minX = Double.parseDouble(minXField.getText());
          minY = Double.parseDouble(minYField.getText());
          minXField.setStyle("");
          minYField.setStyle("");
        } catch (NumberFormatException e) {
          minXField.setStyle("-fx-border-color: red;");
          minYField.setStyle("-fx-border-color: red;");
          allFieldsValid = false;
        }
      }

      // Validate and parse maximum coordinates
      if (maxXField.getText().trim().isEmpty() || maxYField.getText().trim().isEmpty()) {
        maxXField.setStyle("-fx-border-color: red;");
        maxYField.setStyle("-fx-border-color: red;");
        allFieldsValid = false;
      } else {
        try {
          maxX = Double.parseDouble(maxXField.getText());
          maxY = Double.parseDouble(maxYField.getText());
          maxXField.setStyle("");
          maxYField.setStyle("");
        } catch (NumberFormatException e) {
          maxXField.setStyle("-fx-border-color: red;");
          maxYField.setStyle("-fx-border-color: red;");
          allFieldsValid = false;
        }
       }

      missingInputMessage.setVisible(!allFieldsValid);

      // Proceed only if all fields are valid
      if (allFieldsValid) {
        // Reset all fields to default style
        resetAllToDefaultStyle();
        Vector2D minCoords = new Vector2D(minX, minY);
        Vector2D maxCoords = new Vector2D(maxX, maxY);
        currentChaosGame = controller.handleTransformationSelection(transformationsGroup,
                affineGrid, realPartField, imaginaryPartField, minCoords, maxCoords, steps);
        drawFractal(currentChaosGame);
      } else {
        System.err.println("Please correct the highlighted errors.");
      }
    });
  }
  public void resetAllToDefaultStyle() {
    // Reset all fields to default style
    for (Node node : affineGrid.getChildren()) {
      if (node instanceof TextField) {
        node.setStyle("");
      }
    }
      stepsField.setStyle("");
      affineGrid.setStyle("");
      affineBox.setStyle("");
      realPartField.setStyle("");
      imaginaryPartField.setStyle("");
  }
  private int parseStepsSafely(String text) {
    try {
      return Integer.parseInt(text);
    } catch (NumberFormatException e) {
      return 0; // Return a default indicating invalid input if parsing fails
    }
  }
  /**
   * Retrieves a node from a GridPane at the specified row and column indices.
   *
   * @param gridPane The GridPane from which to fetch the node.
   * @param col The column index of the node.
   * @param row The row index of the node.
   * @return The node found at the specified indices or null if no such node exists.
   */
  public Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
    for (Node node : gridPane.getChildren()) {
      if (GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == col
              && GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row) {
        return node;
      }
    }
    return null;  // Return null if no matching node is found
  }

  private void setupLeftSide(BorderPane root) {
    try{
      VBox spacingBox = new VBox();
      spacingBox.setMinHeight(20); // Adjust as needed to create the desired space
      // Add the existing elements to the VBox
      leftSide.getChildren().addAll(
          transformationBox,
          stepsBox,
          coordGrid,
          juliaGrid,
          affineBox,
          showButton,
          missingInputMessage,
          spacingBox,
          colorModeCheckbox,
          makeFullFractalCheckbox
      );
    } catch (Exception e) {
      System.out.println("Failed to add elements to the left side: " + e.getMessage());
    }

    // Set the ScrollPane as the content of the left side
    scrollPane.setContent(leftSide);
    // Set preferred width for the left side
    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    scrollPane.setPrefWidth(screenWidth * 0.25);
    // Add a vertical separator
    Separator separator = new Separator();
    separator.setOrientation(Orientation.VERTICAL);

    // Layout that contains the left side and the separator
    HBox leftLayout = new HBox(scrollPane, separator);
    // Add the left layout to the root
    root.setLeft(leftLayout);
  }

  private void setupRightSide(BorderPane root) {
    // Initialize the Canvas for fractal drawing
    fractalCanvas = new Canvas();
    gc = fractalCanvas.getGraphicsContext2D();

    // Bind the width and height of the fractalCanvas to the width and height of the BorderPane's right side
    fractalCanvas.widthProperty().bind(root.widthProperty().subtract(scrollPane.getPrefWidth()));
    fractalCanvas.heightProperty().bind(root.heightProperty());

    // Position the fractalCanvas on the right side of the BorderPane
    root.setRight(fractalCanvas); // Use setCenter if you prefer it in the center
  }

  private void setupListeners() {
    fractalCanvas.widthProperty().addListener(obs -> redrawFractalIfNeeded());
    fractalCanvas.heightProperty().addListener(obs -> redrawFractalIfNeeded());
    initializeRadioButtonListener();
  }

  private void configureColorModeCheckbox() {
    colorModeCheckbox = new CheckBox("Enable Heatmap Color Mode");
    colorModeCheckbox.setSelected(false);  // Default is unchecked (B&W mode)
    // Increase font size for visibility
    colorModeCheckbox.setStyle("-fx-font-size: 18px;");
    colorModeCheckbox.setOnAction(event -> redrawFractalIfNeeded());
  }

  private void configureMakeFullFractalCheckbox() {
    makeFullFractalCheckbox = new CheckBox("Make full fractal");
    makeFullFractalCheckbox.setSelected(false);
    makeFullFractalCheckbox.setStyle("-fx-font-size: 18px;");
    makeFullFractalCheckbox.setOnAction(event -> {
      if (makeFullFractalCheckbox.isSelected() && currentChaosGame != null) {
        currentChaosGame.makeFullFractal();
        drawFractal(currentChaosGame);
      } else if (currentChaosGame != null && transformationsGroup.getSelectedToggle().getUserData().equals("Barnsley")) {
        currentChaosGame.runStepsForBarnsley(Integer.parseInt(stepsField.getText()));
        drawFractal(currentChaosGame);
      } else if (currentChaosGame != null) {
        currentChaosGame.runSteps(Integer.parseInt(stepsField.getText()));
        drawFractal(currentChaosGame);
      }
    });
  }

  private TextField createDecimalTextField(String defaultValue) {
    TextField textField = new TextField(defaultValue);
    UnaryOperator<TextFormatter.Change> decimalFilter = change -> change.getControlNewText().matches("-?((\\d*)|(\\d+\\.\\d*))") ? change : null;
    textField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), Double.parseDouble(defaultValue), decimalFilter));
    return textField;
  }

  private void addMatrixVectorRow(int row) {
    String[] matrixPlaceholders = {"a00", "a01", "a10", "a11"};
    for (int i = 0; i < matrixPlaceholders.length; i++) {
      TextField matrixField = createDecimalTextField("0.0");
      matrixField.setPrefWidth(50);
      matrixField.setPromptText(matrixPlaceholders[i]);
      affineGrid.add(matrixField, i, row);
    }

    // Space between matrix and vector elements
    Pane spacer = new Pane();
    spacer.setMinSize(20, 1);
    affineGrid.add(spacer, 4, row);

    // Vector elements with placeholders
    TextField vectorFieldX = createDecimalTextField("0.0");
    vectorFieldX.setPrefWidth(50);
    vectorFieldX.setPromptText("x0");
    affineGrid.add(vectorFieldX, 5, row);

    TextField vectorFieldY = createDecimalTextField("0.0");
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

  private void fillInAffineGridForMapleTree() {
    // Clear existing rows if necessary
    affineGrid.getChildren().clear();

    // Matrix and vector entries as provided
    double[][] matrixValues = {
        {-0.04, 0, -0.23, -0.65},
        {0.61, 0, 0, 0.31},
        {0.65, 0.29, 0, 0.48},
        {0.64, -0.3, 0.16, 0.56}
    };
    double[][] vectorValues = {
        {-0.08, 0.26},
        {0.07, 3.5},
        {0.74, 1.39},
        {-0.56, 0.60}
    };

    // Assuming each row will contain 4 matrix text fields, a spacer, and 2 vector text fields
    for (int i = 0; i < matrixValues.length; i++) {
      addMatrixVectorRow(i);  // Add a new row
      for (int j = 0; j < 4; j++) {  // Set matrix values
        TextField matrixField = (TextField) getNodeFromGridPane(affineGrid, j, i);
        matrixField.setText(String.format("%.2f", matrixValues[i][j]));
      }
      for (int j = 0; j < 2; j++) {  // Set vector values
        TextField vectorField = (TextField) getNodeFromGridPane(affineGrid, 5 + j, i);
        vectorField.setText(String.format("%.2f", vectorValues[i][j]));
      }
    }

  }

  private void redrawFractalIfNeeded() {
    if (currentChaosGame != null) {
      drawFractal(currentChaosGame);
    }
  }

  private void initializeRadioButtonListener(){
    // Disable all grids initially
    affineBox.setDisable(true);
    juliaGrid.setDisable(true);
    transformationsGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
      if (newToggle != null) {
        RadioButton selectedButton = (RadioButton) newToggle;
        switch (selectedButton.getText()) {
          case "Affine":
            affineBox.setDisable(false);
            juliaGrid.setDisable(true);
            break;
          case "Julia":
            juliaGrid.setDisable(false);
            affineBox.setDisable(true);
            break;
          default:
            // Keep all specialized controls disabled if none of the above cases match
            affineBox.setDisable(true);
            juliaGrid.setDisable(true);
            break;
        }
      }
    });
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
            Color color = getColorForValue(intensity);
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
  private Color getColorForValue(double intensity) {
    if (intensity < 0.25) {
      // Interpolate between blue (0) and green (0.25)
      return Color.BLUE.interpolate(Color.GREEN, intensity * 4);
    } else if (intensity < 0.5) {
      // Interpolate between green (0.25) and yellow (0.5)
      return Color.GREEN.interpolate(Color.YELLOW, (intensity - 0.25) * 4);
    } else if (intensity < 0.75) {
      // Interpolate between yellow (0.5) and orange (0.75)
      return Color.YELLOW.interpolate(Color.ORANGE, (intensity - 0.5) * 4);
    } else {
      // Interpolate between orange (0.75) and red (1)
      return Color.ORANGE.interpolate(Color.RED, (intensity - 0.75) * 4);
    }
  }


  private void loadSettings() {
    try (FileInputStream fis = new FileInputStream(settingsFilePath)) {
      appSettings.load(fis);
      minXField.setText(appSettings.getProperty("minX", "-4"));
      minYField.setText(appSettings.getProperty("minY", "-1"));
      maxXField.setText(appSettings.getProperty("maxX", "4"));
      maxYField.setText(appSettings.getProperty("maxY", "10"));
      stepsField.setText(appSettings.getProperty("steps", "0"));
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
    } catch (IOException e) {
      System.out.println("Failed to load settings: " + e.getMessage());
    }
  }

  private void saveSettings() {
    try (FileOutputStream fos = new FileOutputStream(settingsFilePath)) {
      appSettings.setProperty("minX", minXField.getText());
      appSettings.setProperty("minY", minYField.getText());
      appSettings.setProperty("maxX", maxXField.getText());
      appSettings.setProperty("maxY", maxYField.getText());
      appSettings.setProperty("steps", stepsField.getText());
      appSettings.setProperty("realPart", realPartField.getText());
      appSettings.setProperty("imaginaryPart", imaginaryPartField.getText());
      appSettings.setProperty("transformation", ((RadioButton) transformationsGroup.getSelectedToggle()).getText());
      appSettings.setProperty("colorMode", String.valueOf(colorModeCheckbox.isSelected()));
      appSettings.store(fos, "Application Settings");
    } catch (IOException e) {
      System.out.println("Failed to save settings: " + e.getMessage());
    }
  }



}