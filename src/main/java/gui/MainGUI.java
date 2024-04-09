package gui;

import chaosGame.ChaosCanvas;
import chaosGame.ChaosGame;
import chaosGame.ChaosGameDescription;
import factory.ChaosGameDescriptionFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import mathcore.Complex;
import mathcore.Matrix2x2;
import mathcore.Vector2D;
import transformations.AffineTransform2D;
import transformations.Transform2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import java.util.function.UnaryOperator;

public class MainGUI extends Application {
  private GridPane affineGrid; // This needs to be accessible by the button's event handler
  private VBox affineBox; // Container for the affine transformation section
  private GridPane juliaGrid;
  private RadioButton affine;
  private RadioButton julia;
  private RadioButton barnsley;
  private RadioButton sierpinski;
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

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    // TODO Auto-generated method stub
    BorderPane root = new BorderPane();

    root.setPadding(new Insets(10));

    // Scrollable Left side layout
    VBox leftSide = new VBox(10);
    leftSide.setPadding(new Insets(10));
    scrollPane = new ScrollPane(leftSide);
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide horizontal scrollbar
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide vertical scrollbar

    // Transformation checkboxes
    VBox transformationBox = new VBox(5);
    Label transformationLabel = new Label("Transformations");
    transformationBox.getChildren().add(transformationLabel);
    transformationsGroup = new ToggleGroup();

    affine = new RadioButton("Affine");
    affine.setToggleGroup(transformationsGroup);
    barnsley = new RadioButton("Barnsley");
    barnsley.setToggleGroup(transformationsGroup);
    julia = new RadioButton("Julia");
    julia.setToggleGroup(transformationsGroup);
    sierpinski = new RadioButton("Sierpinski");
    sierpinski.setToggleGroup(transformationsGroup);

    HBox transformationsBox = new HBox(10);
    transformationsBox.getChildren().addAll(affine, barnsley, julia, sierpinski);
    transformationBox.getChildren().add(transformationsBox);


    // Steps input
    VBox stepsBox = new VBox(5);
    Label stepsLabel = new Label("Steps");
    stepsField = new TextField();
    stepsField.setPromptText("(0-10000000)");

    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
      String newText = change.getControlNewText();
      // Match an empty string, or a number from 0 to 9999999, or the number 10000000
      if (newText.matches("([1-9][0-9]{0,6}|10000000|0)?")) {
        return change; // Accept the change
      }
      return null; // Reject the change
    };

    TextFormatter<Integer> textFormatter = new TextFormatter<>(
        new IntegerStringConverter(), // Converter
        0,                            // Default value
        integerFilter                 // Filter
    );

    stepsField.setTextFormatter(textFormatter);
    stepsBox.getChildren().addAll(stepsLabel, stepsField);



    // Coordinate fields
    GridPane coordGrid = new GridPane();
    coordGrid.setHgap(10);
    coordGrid.setVgap(10);
    coordGrid.add(new Label("Min. Coord"), 0, 0);
    coordGrid.add(new Label("Max. Coord"), 2, 0);
    minXField = createTextFieldWithPlaceholder("MinX");
    minXField.setText("-4");
    minYField = createTextFieldWithPlaceholder("MinY");
    minYField.setText("-1");
    maxXField = createTextFieldWithPlaceholder("MaxX");
    maxXField.setText("4");
    maxYField = createTextFieldWithPlaceholder("MaxY");
    maxYField.setText("10");

    UnaryOperator<TextFormatter.Change> decimalFilter = change -> {
      String newText = change.getControlNewText();
      if (newText.matches("-?((\\d*)|(\\d+\\.\\d*))")) { // Regex to match integers and decimal numbers, with an optional minus sign
        return change; // Accept the change
      }
      return null; // Reject the change
    };

    // Create and apply a new TextFormatter to each TextField
    minXField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), Double.parseDouble(minXField.getText()), decimalFilter));
    minYField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), Double.parseDouble(minYField.getText()), decimalFilter));
    maxXField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), Double.parseDouble(maxXField.getText()), decimalFilter));
    maxYField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), Double.parseDouble(maxYField.getText()), decimalFilter));

    coordGrid.add(minXField, 0, 1);
    coordGrid.add(minYField, 1, 1);
    coordGrid.add(maxXField, 2, 1);
    coordGrid.add(maxYField, 3, 1);

    // Julia-constant fields
    juliaGrid = new GridPane();
    juliaGrid.setHgap(10);
    juliaGrid.setVgap(10);
    juliaGrid.add(new Label("Julia-constant"), 0, 0, 2, 1);

    // Instantiate each TextField for the Julia constants with a placeholder
    realPartField = createTextFieldWithPlaceholder("Real part");
    imaginaryPartField = createTextFieldWithPlaceholder("Imaginary part");

    // Create and apply a new TextFormatter to each TextField for Julia constants
    realPartField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), null, decimalFilter));
    imaginaryPartField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), null, decimalFilter));
    // Set the initial texts for these fields
    realPartField.setText("0.285"); // Example value for the real part
    imaginaryPartField.setText("0.01"); // Example value for the imaginary part
    // Add fields to the Julia grid
    juliaGrid.add(realPartField, 0, 1); // Real part
    juliaGrid.add(imaginaryPartField, 1, 1); // Imaginary part

    // Affine matrices and vectors
    affineBox = new VBox(10);
    affineBox.getChildren().add(new Label("Affine matrices and vectors"));

    affineGrid = new GridPane();
    affineGrid.setHgap(10);
    affineGrid.setVgap(10);

    // Add initial 1 rows of matrix and vector inputs
    for (int row = 0; row < 1; row++) {
      addMatrixVectorRow(row);
    }
    affineBox.getChildren().add(affineGrid);


    // Add and Remove buttons
    HBox buttonsBox = new HBox(10);
    Button addButton = new Button("Add");
    addButton.setOnAction(event -> addMatrixVectorRow(affineGrid.getRowCount()));
    Button removeButton = new Button("Remove");
    removeButton.setOnAction(event -> removeMatrixVectorRow());
    buttonsBox.getChildren().addAll(addButton, removeButton);
    affineBox.getChildren().add(buttonsBox);


    // Show button
    Button showButton = new Button("Show");

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


    // Initialize the Canvas for fractal drawing
    //fractalCanvas = new Canvas(800, 600);
    fractalCanvas = new Canvas();
    gc = fractalCanvas.getGraphicsContext2D();

    // Bind the width and height of the fractalCanvas to the width and height of the BorderPane's right side
    fractalCanvas.widthProperty().bind(root.widthProperty().subtract(scrollPane.getPrefWidth()));
    fractalCanvas.heightProperty().bind(root.heightProperty());

    // Position the fractalCanvas on the right side of the BorderPane
    root.setRight(fractalCanvas); // Use setCenter if you prefer it in the center

    // Show button action to draw the fractal
    showButton.setOnAction(event -> drawFractal());

    initializeRadioButtonListener();

    // The drawFractal() method will be responsible for centering the fractal
    fractalCanvas.widthProperty().addListener(obs -> drawFractal());
    fractalCanvas.heightProperty().addListener(obs -> drawFractal());

    Scene scene = new Scene(root);
    primaryStage.setMaximized(true); // Set the stage to be maximized
    primaryStage.setTitle("Chaos game");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  // Helper method to create a TextField with placeholder text
  private TextField createTextFieldWithPlaceholder(String placeholder) {
    TextField textField = new TextField();
    textField.setPromptText(placeholder);
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
    int lastRowIndex = affineGrid.getRowCount() - 1;
    if (lastRowIndex >= 1) {
      // Remove all elements in the last row
      for (int i = 0; i < 7; i++) {
        Node node = getNodeFromGridPane(affineGrid, i, lastRowIndex);
        affineGrid.getChildren().remove(node);
      }
    }
  }

  private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
    for (Node node : gridPane.getChildren()) {
      if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
        return node;
      }
    }
    return null;
  }

  private void initializeRadioButtonListener(){
    // Radio button action listeners
    affine.selectedProperty().addListener((observable, oldValue, newValue) -> {
      affineBox.setDisable(!newValue);
    });
    julia.selectedProperty().addListener((observable, oldValue, newValue) -> {
      juliaGrid.setDisable(!newValue);
    });

    // Initially disable grids, since no option is selected by default
    affineBox.setDisable(true);
    juliaGrid.setDisable(true);

    // Ensure that selecting a radio button enables the respective grid
    transformationsGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
      if (newToggle != null) {
        RadioButton selectedButton = (RadioButton) newToggle;
        switch (selectedButton.getText()) {
          case "Affine":
            affineBox.setDisable(false);
            juliaGrid.setDisable(true);
            break;
          case "Julia":
            affineBox.setDisable(true);
            juliaGrid.setDisable(false);
            break;
          default:
            affineBox.setDisable(true);
            juliaGrid.setDisable(true);
            break;
        }
      } else {
        // No radio buttons are selected, disable both grids
        affineBox.setDisable(true);
        juliaGrid.setDisable(true);
      }
    });
  }

  private void drawFractal(){
    // Get the coordinate values
    double minX = Double.parseDouble(minXField.getText());
    double minY = Double.parseDouble(minYField.getText());
    double maxX = Double.parseDouble(maxXField.getText());
    double maxY = Double.parseDouble(maxYField.getText());


    // Clear the canvas first
    gc.clearRect(0, 0, fractalCanvas.getWidth(), fractalCanvas.getHeight());


    ChaosGame chaosGame = null;
    ChaosGameDescriptionFactory factory = new ChaosGameDescriptionFactory();

    if (affine.isSelected()){
      List<Transform2D> transformations = getAffineTransformationValues();
      ChaosGameDescription description = new ChaosGameDescription(transformations, new Vector2D(-1, -1), new Vector2D(1, 1));
      chaosGame = new ChaosGame(description, 900, 750);
      chaosGame.runSteps(100000);
      // Get affine transformations
      // Create ChaosGameDescription
      // Create ChaosGame
      // Run steps
      // Display

    }
    else if (julia.isSelected()){
      Complex c = new Complex(Double.parseDouble(realPartField.getText()), Double.parseDouble(imaginaryPartField.getText()));
      ChaosGameDescription description = factory.julia(new Vector2D(minX, minY), new Vector2D(maxX, maxY), c);
      chaosGame = new ChaosGame(description, 900, 750);
      chaosGame.runSteps(Integer.parseInt(stepsField.getText()));
      // Get Julia constant
      // Create ChaosGameDescription
      // Create ChaosGame
      // Run steps
      // Display
    } else if (sierpinski.isSelected()){
      ChaosGameDescription description = factory.sierpinski(new Vector2D(minX, minY), new Vector2D(maxX, maxY));
      chaosGame = new ChaosGame(description, 900, 750);
      chaosGame.runSteps(Integer.parseInt(stepsField.getText()));
      // Create ChaosGameDescription
      // Create ChaosGame
      // Run steps
      // Display
    } else if (barnsley.isSelected()){
      ChaosGameDescription description = factory.barnsley(new Vector2D(minX, minY), new Vector2D(maxX, maxY));
      chaosGame = new ChaosGame(description, 900, 750);
      chaosGame.runSteps(Integer.parseInt(stepsField.getText()));
      // Create ChaosGameDescription
      // Create ChaosGame
      // Run steps
      // Display
    }

    // Ensure we have a valid ChaosGame instance before attempting to draw
    if (chaosGame != null) {
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



  private List<Transform2D> getAffineTransformationValues() {
    List<Transform2D> transformations = new ArrayList<>();
    // Assuming there are 4 rows, and each row has 4 matrix fields followed by 2 vector fields
    for (int row = 0; row < affineGrid.getRowCount(); row++) {
      double[] matrixValues = new double[4]; // To store a00, a01, a10, a11
      double[] vectorValues = new double[2]; // To store x0, y0

      // Retrieve matrix values
      for (int i = 0; i < 4; i++) { // matrixValues indexes are 0 to 3
        TextField textField = (TextField) getNodeFromGridPane(affineGrid, i, row);
        try {
          matrixValues[i] = Double.parseDouble(textField.getText());
        } catch (NumberFormatException e) {
          System.out.println("Invalid input for matrix values.");
          return null; // Or handle the error appropriately
        }
      }

      // Retrieve vector values
      for (int i = 0; i < 2; i++) { // vectorValues indexes are 0 to 1, grid positions are 5 and 6
        TextField textField = (TextField) getNodeFromGridPane(affineGrid, i + 5, row);
        try {
          vectorValues[i] = Double.parseDouble(textField.getText());
        } catch (NumberFormatException e) {
          System.out.println("Invalid input for vector values.");
          return null; // Or handle the error appropriately
        }
      }

      // Now you have the values for this row in matrixValues and vectorValues
      // Do whatever processing you need with these values
      System.out.println("Matrix Values: " + Arrays.toString(matrixValues));
      System.out.println("Vector Values: " + Arrays.toString(vectorValues));
      Matrix2x2 matrix = new Matrix2x2(matrixValues[0], matrixValues[1], matrixValues[2], matrixValues[3]);
      Vector2D vector = new Vector2D(vectorValues[0], vectorValues[1]);
      transformations.add(new AffineTransform2D(matrix, vector));
    }
    return transformations;
  }


}
