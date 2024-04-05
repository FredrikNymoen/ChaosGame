package gui;

import java.net.http.HttpResponse.BodyHandler;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class StartupPage extends Application {
  private GridPane affineGrid; // This needs to be accessible by the button's event handler
  private VBox affineBox; // Container for the affine transformation section
  private GridPane juliaGrid;
  private RadioButton affine;
  private RadioButton julia;
  private ToggleGroup transformationsGroup;
  private ScrollPane scrollPane; // ScrollPane for the left side

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
    RadioButton barnsley = new RadioButton("Barnsley");
    barnsley.setToggleGroup(transformationsGroup);
    julia = new RadioButton("Julia");
    julia.setToggleGroup(transformationsGroup);
    RadioButton sierpinski = new RadioButton("Sierpinski");
    sierpinski.setToggleGroup(transformationsGroup);

    HBox transformationsBox = new HBox(10);
    transformationsBox.getChildren().addAll(affine, barnsley, julia, sierpinski);
    transformationBox.getChildren().add(transformationsBox);


    // Steps input
    VBox stepsBox = new VBox(5);
    Label stepsLabel = new Label("Steps");
    TextField stepsField = new TextField();
    stepsField.setPromptText("(0-1000000)");
    stepsBox.getChildren().addAll(stepsLabel, stepsField);


    // Coordinate fields
    GridPane coordGrid = new GridPane();
    coordGrid.setHgap(10);
    coordGrid.setVgap(10);
    coordGrid.add(new Label("Min. Coord"), 0, 0);
    coordGrid.add(new Label("Max. Coord"), 2, 0);
    coordGrid.add(createTextFieldWithPlaceholder("MinX"), 0, 1); // Min X
    coordGrid.add(createTextFieldWithPlaceholder("MinY"), 1, 1); // Min Y
    coordGrid.add(createTextFieldWithPlaceholder("MaxX"), 2, 1); // Max X
    coordGrid.add(createTextFieldWithPlaceholder("MaxY"), 3, 1); // Max Y

    // Julia-constant fields
    juliaGrid = new GridPane();
    juliaGrid.setHgap(10);
    juliaGrid.setVgap(10);
    juliaGrid.add(new Label("Julia-constant"), 0, 0, 2, 1);
    juliaGrid.add(createTextFieldWithPlaceholder("Real part"), 0, 1); // X0
    juliaGrid.add(createTextFieldWithPlaceholder("Imaginary part"), 1, 1); // Y0

    // Affine matrices and vectors
    affineBox = new VBox(10);
    affineBox.getChildren().add(new Label("Affine matrices and vectors"));

    affineGrid = new GridPane();
    affineGrid.setHgap(10);
    affineGrid.setVgap(10);

    // Add initial 4 rows of matrix and vector inputs
    for (int row = 0; row < 4; row++) {
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

    initializeRadioButtonListener();

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
}
