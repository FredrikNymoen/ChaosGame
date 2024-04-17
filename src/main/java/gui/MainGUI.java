package gui;

import chaosGame.ChaosGame;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    configureShowButton();

    setupLeftSide(root);
    setupRightSide(root);
    setupListeners();

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
    affine.setToggleGroup(transformationsGroup);
    RadioButton barnsley = new RadioButton("Barnsley");
    barnsley.setToggleGroup(transformationsGroup);
    RadioButton julia = new RadioButton("Julia");
    julia.setToggleGroup(transformationsGroup);
    RadioButton sierpinski = new RadioButton("Sierpinski");
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

    realPartField = createDecimalTextField("0.285");
    imaginaryPartField = createDecimalTextField("0.01");
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
    buttonsBox.getChildren().addAll(addButton, removeButton);

    affineBox.getChildren().addAll(affineGrid, buttonsBox);
  }

  private void configureShowButton() {
    // Show button
    showButton = new Button("Show");
    // Show button action to draw the fractal
    showButton.setOnAction(event -> {
      double minX = Double.parseDouble(minXField.getText());
      double minY = Double.parseDouble(minYField.getText());
      double maxX = Double.parseDouble(maxXField.getText());
      double maxY = Double.parseDouble(maxYField.getText());
      int steps = Integer.parseInt(stepsField.getText());
      Vector2D minCoords = new Vector2D(minX, minY);
      Vector2D maxCoords = new Vector2D(maxX, maxY);
      currentChaosGame = controller.handleTransformationSelection(transformationsGroup, affineGrid, realPartField, imaginaryPartField, minCoords, maxCoords, steps);
      if (currentChaosGame != null) {
        drawFractal(currentChaosGame);
      }
    });
  }

  private void setupLeftSide(BorderPane root) {
    // Add all elements to the left side layout
    leftSide.getChildren().addAll(transformationBox, stepsBox, coordGrid, juliaGrid, affineBox, showButton);
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

  private TextField createDecimalTextField(String defaultValue) {
    TextField textField = new TextField(defaultValue);
    UnaryOperator<TextFormatter.Change> decimalFilter = change -> change.getControlNewText().matches("-?((\\d*)|(\\d+\\.\\d*))") ? change : null;
    textField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), Double.parseDouble(defaultValue), decimalFilter));
    return textField;
  }

  private void addMatrixVectorRow(int row) {
    String[] matrixPlaceholders = {"a00", "a01", "a10", "a11"};
    for (int i = 0; i < matrixPlaceholders.length; i++) {
      TextField matrixField = new TextField();
      matrixField.setPrefWidth(50);
      matrixField.setPromptText(matrixPlaceholders[i]);
      affineGrid.add(matrixField, i, row);
    }

    // Space between matrix and vector elements
    Pane spacer = new Pane();
    spacer.setMinSize(20, 1);
    affineGrid.add(spacer, 4, row);

    // Vector elements with placeholders
    TextField vectorFieldX = new TextField();
    vectorFieldX.setPrefWidth(50);
    vectorFieldX.setPromptText("x0");
    affineGrid.add(vectorFieldX, 5, row);

    TextField vectorFieldY = new TextField();
    vectorFieldY.setPrefWidth(50);
    vectorFieldY.setPromptText("y0");
    affineGrid.add(vectorFieldY, 6, row);
  }

  private void removeMatrixVectorRow() {
    ChaosGameController controller = new ChaosGameController();
    int lastRowIndex = affineGrid.getRowCount() - 1;
    if (lastRowIndex >= 1) {
      // Remove all elements in the last row
      for (int i = 0; i < 7; i++) {
        Node node = controller.getNodeFromGridPane(affineGrid, i, lastRowIndex);
        affineGrid.getChildren().remove(node);
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
            break;
          case "Julia":
            juliaGrid.setDisable(false);
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
    int[][] canvasArray = chaosGame.getCanvas().getCanvasArray();

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
        if (canvasArray[i][j] == 1) {
          gc.setFill(Color.BLACK); // Fractal pixel color
        } else {
          gc.setFill(Color.WHITE); // Background color
        }
        gc.fillRect(startX + j, startY + i, 1, 1); // Draw pixel
      }
    }
  }

}