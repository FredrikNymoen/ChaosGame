package view;

import controller.HandleActionController;
import controller.ValidationController;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.chaosgame.ChaosGame;
import util.UIHelper;
import util.Utility;

/**
 * The main graphical user interface class for the Chaos Game application. This class sets up the
 * layout, initializes components, and handles events.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class MainGUI extends Application {

  private final Layout layout = new Layout();
  private final ValidationController validationController = new ValidationController();
  private final ChaosGameObserver observer = new EventHandler();
  private final HandleActionController handleActionController = new HandleActionController(
      observer);
  private VBox leftSide;
  private ScrollPane scrollPane;
  private VBox transformationBox;
  private ToggleGroup transformationsGroup;
  private VBox stepsBox;
  private Slider stepsSlider;
  private GridPane coordGrid;
  private GridPane juliaGrid;
  private ToggleButton juliaToggleSwitch;
  private VBox affineBox;
  private GridPane affineGrid;
  private Button showButton;
  private Button iterativeTransformationButton;
  private Label missingInputMessage;
  private CheckBox colorModeCheckbox;
  private Button copyLastTransformationButton;
  private Button exitButton;
  private Canvas fractalCanvas;
  private ChaosGame currentChaosGame;

  /**
   * The main method to launch the JavaFX application.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * The start method is the main entry point for the JavaFX application. It sets up the primary
   * stage and initializes all UI components.
   *
   * @param primaryStage the primary stage for this application
   */
  @Override
  public void start(Stage primaryStage) {
    BorderPane root = new BorderPane();
    root.setPadding(new Insets(10));

    configureScrollPane();
    configureTransformationButtonOptions();
    configureStepsBox();
    configureCoordinateFields();
    configureJuliaFields();
    configureAffineBox();
    configureColorModeCheckbox();
    configureIterativeTransformationButton();
    configureShowButton();
    configureMissingInputMessage();
    configureCopyLastTransformationButton();
    configureExitButton(primaryStage);

    configureLeftSide(root);
    configureRightSide(root);
    setupListeners();

    primaryStage.setOnCloseRequest(event -> saveSettings());
    loadSettings();

    Scene scene = new Scene(root);
    scene.getStylesheets().add(Objects.requireNonNull(getClass()
        .getResource("/chaosgame.css")).toExternalForm());
    primaryStage.setTitle(Utility.APPLICATION_NAME);
    primaryStage.setScene(scene);
    primaryStage.setFullScreen(true); // Set the stage to full screen
    primaryStage.show();
  }


  /**
   * Configures the scroll pane for the left side of the layout.
   */
  public void configureScrollPane() {
    scrollPane = layout.createLeftsideScrollPane();
    leftSide = (VBox) scrollPane.getContent();
  }

  /**
   * Configures the transformation button options.
   */
  public void configureTransformationButtonOptions() {
    transformationBox = layout.createTransformationsBox();
    transformationsGroup = new ToggleGroup();
    layout.addTransformationOptions(transformationBox, transformationsGroup);
  }

  /**
   * Configures the steps input box and binds the slider's value to a label.
   */
  public void configureStepsBox() {
    stepsBox = layout.createStepsBox();
    stepsSlider = (Slider) stepsBox.getChildren().get(1);

    // Bind the slider's value to the label
    stepsSlider.valueProperty().addListener(
        (ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            -> handleActionController.onSliderValueChanged(stepsBox, newValue.intValue()));
  }

  /**
   * Configures the coordinate input fields.
   */
  public void configureCoordinateFields() {
    coordGrid = layout.createCoordGrid();
  }

  /**
   * Configures the Julia set input fields and toggle button.
   */
  public void configureJuliaFields() {
    juliaGrid = layout.createJuliaGrid();
    juliaToggleSwitch = (ToggleButton) UIHelper.getNodeFromGridPane(juliaGrid, 0, 1);

    // Add an event handler to change the text when the button is toggled
    assert juliaToggleSwitch != null;
    juliaToggleSwitch.setOnAction(
        event -> handleActionController.onJuliaToggleSwitched(juliaToggleSwitch, coordGrid,
            stepsBox, iterativeTransformationButton));
  }

  /**
   * Configures the affine transformation input box and buttons.
   */
  public void configureAffineBox() {
    affineBox = layout.createAffineBox();
    affineGrid = (GridPane) affineBox.getChildren().get(1);
    handleActionController.addMatrixVectorRow(0, affineGrid, layout);

    Button addButton = (Button) ((HBox) affineBox.getChildren().get(2)).getChildren().get(0);
    Button removeButton = (Button) ((HBox) affineBox.getChildren().get(2)).getChildren().get(1);
    addButton.setOnAction(
        event -> handleActionController.addMatrixVectorRow(affineGrid.getRowCount(), affineGrid,
            layout));
    removeButton.setOnAction(event -> handleActionController.removeMatrixVectorRow(affineGrid));
  }

  /**
   * Configures the show button and its action handler.
   */
  public void configureShowButton() {
    showButton = new Button("Show");
    showButton.getStyleClass().add("show-button");
    showButton.setOnAction(event -> {
      handleActionController.resetFieldsToDefaultStyle(affineGrid, juliaGrid,
          affineBox);  // Reset all fields to default style
      boolean allFieldsValid = validationController.isAllFieldsValid(juliaGrid,
          transformationsGroup, affineGrid, coordGrid);
      missingInputMessage.setVisible(!allFieldsValid);

      // Proceed only if all fields are valid
      if (allFieldsValid) {
        int steps = (int) stepsSlider.getValue();
        handleActionController.resetFieldsToDefaultStyle(affineGrid, juliaGrid, affineBox);
        currentChaosGame = handleActionController.showButtonClicked(transformationsGroup,
            affineGrid, juliaGrid, coordGrid, steps);
        handleActionController.drawFractal(fractalCanvas, currentChaosGame, colorModeCheckbox);
      }
    });
  }

  /**
   * Configures the iterative transformation button and its action handler.
   */
  public void configureIterativeTransformationButton() {
    iterativeTransformationButton = layout.createIterativeTransformationButton();
    iterativeTransformationButton.setOnAction(event -> {
      handleActionController.resetFieldsToDefaultStyle(affineGrid, juliaGrid,
          affineBox);  // Reset all fields to default style
      boolean allFieldsValid = validationController.isAllFieldsValid(juliaGrid,
          transformationsGroup, affineGrid, coordGrid);
      missingInputMessage.setVisible(!allFieldsValid);

      // Proceed only if all fields are valid
      if (allFieldsValid) {
        handleActionController.resetFieldsToDefaultStyle(affineGrid, juliaGrid, affineBox);
        handleActionController.handleIterativeTransformation(transformationsGroup, affineGrid,
            juliaGrid, coordGrid, stepsSlider.getValue(), fractalCanvas, colorModeCheckbox);
      }
    });
  }

  /**
   * Configures the label for displaying missing input messages.
   */
  public void configureMissingInputMessage() {
    missingInputMessage = new Label("Please fill in all required fields.");
    missingInputMessage.setStyle("-fx-text-fill: red;");
    missingInputMessage.setVisible(false);
  }

  /**
   * Configures the color mode checkbox.
   */
  public void configureColorModeCheckbox() {
    colorModeCheckbox = new CheckBox("Enable Heatmap Color Mode");
    colorModeCheckbox.setSelected(false);
    colorModeCheckbox.getStyleClass().add("heatmap-checkbox");
  }

  /**
   * Configures the button for copying the last transformation.
   */
  public void configureCopyLastTransformationButton() {
    copyLastTransformationButton = layout.createCopyLastTransformationButton();
    copyLastTransformationButton.setOnAction(
        event -> handleActionController.handleCopyLastTransformation(
            transformationsGroup, coordGrid, affineGrid, juliaGrid, juliaToggleSwitch, layout));
  }

  /**
   * Configures the exit button and its action handler.
   *
   * @param primaryStage the primary stage of the application
   */
  public void configureExitButton(Stage primaryStage) {
    exitButton = new Button("Exit");
    exitButton.getStyleClass().add("exit-button");
    exitButton.setOnAction(event -> {
      saveSettings();
      primaryStage.close();
    });
  }

  /**
   * Configures the left side of the layout with various components.
   *
   * @param root the root layout
   */
  public void configureLeftSide(BorderPane root) {
    leftSide.getStyleClass().add("left-side");
    VBox spacingBox = new VBox();
    spacingBox.setMinHeight(10);
    VBox spacingBox2 = new VBox();
    spacingBox2.setMinHeight(4);

    HBox centeredShowButtonBox = layout.createCenteredHBox(showButton);
    HBox centeredColorModeCheckboxBox = layout.createCenteredHBox(colorModeCheckbox);
    HBox centeredIterativeTransformationBox = layout.createCenteredHBox(
        iterativeTransformationButton);
    HBox centeredCopyTransformationButtonBox = layout.createCenteredHBox(
        copyLastTransformationButton);
    HBox centeredExitButtonBox = layout.createCenteredHBox(exitButton);

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
        centeredCopyTransformationButtonBox,
        spacingBox2,
        centeredExitButtonBox
    );

    layout.setupLeftSideWithSeperatorLine(scrollPane, leftSide, root);
  }

  /**
   * Configures the right side of the layout with the fractal canvas.
   *
   * @param root the root layout
   */
  public void configureRightSide(BorderPane root) {
    fractalCanvas = new Canvas();

    fractalCanvas.widthProperty().bind(root.widthProperty().subtract(scrollPane.getPrefWidth()));
    fractalCanvas.heightProperty().bind(root.heightProperty());

    root.setRight(fractalCanvas);
  }

  /**
   * Sets up listeners for various components.
   */
  public void setupListeners() {
    fractalCanvas.widthProperty().addListener(
        obs -> handleActionController.onCanvasSizeChanged(fractalCanvas, currentChaosGame,
            colorModeCheckbox));
    fractalCanvas.heightProperty().addListener(
        obs -> handleActionController.onCanvasSizeChanged(fractalCanvas, currentChaosGame,
            colorModeCheckbox));
    initializeRadioButtonListener();
  }

  /**
   * Initializes the listener for the radio button group.
   */
  public void initializeRadioButtonListener() {
    affineBox.setDisable(true);
    juliaGrid.setDisable(true);
    transformationsGroup.selectedToggleProperty()
        .addListener((observable, oldToggle, newToggle) -> {
          if (newToggle != null) {
            RadioButton selectedButton = (RadioButton) newToggle;
            handleActionController.handleTransformationSelected(leftSide, selectedButton.getText());
          }
        });
  }

  /**
   * Loads the application settings from a properties file.
   */
  public void loadSettings() {
    Map<String, TextField> fields = UIHelper.getTextFieldsCoordAndJuliaMap(coordGrid, juliaGrid);

    Properties appSettings = handleActionController.loadSettings();
    fields.get("minXField").setText(appSettings.getProperty("minX", "-4"));
    fields.get("minYField").setText(appSettings.getProperty("minY", "-1"));
    fields.get("maxXField").setText(appSettings.getProperty("maxX", "4"));
    fields.get("maxYField").setText(appSettings.getProperty("maxY", "10"));
    stepsSlider.setValue(Double.parseDouble(appSettings.getProperty("steps", "0")));
    fields.get("realPartField").setText(appSettings.getProperty("realPart", "0.285"));
    fields.get("imaginaryPartField").setText(appSettings.getProperty("imaginaryPart", "0.01"));
    try {
      RadioButton selectedButton = (RadioButton) transformationsGroup.getToggles().stream()
          .filter(t -> t.getUserData().equals(appSettings.getProperty("transformation", "Affine")))
          .findFirst().orElse(null);
      if (selectedButton != null) {
        transformationsGroup.selectToggle(selectedButton);
      }
    } catch (Exception e) {
      transformationsGroup.selectToggle(transformationsGroup.getToggles().get(0));
    }
    colorModeCheckbox.setSelected(
        Boolean.parseBoolean(appSettings.getProperty("colorMode", "false")));
    juliaToggleSwitch.setSelected(
        Boolean.parseBoolean(appSettings.getProperty("juliaToggleSwitch", "false")));
  }

  /**
   * Saves the application settings to a properties file.
   */
  public void saveSettings() {
    Map<String, TextField> fields = UIHelper.getTextFieldsCoordAndJuliaMap(coordGrid, juliaGrid);

    Properties appSettings = new Properties();
    appSettings.setProperty("minX", fields.get("minXField").getText());
    appSettings.setProperty("minY", fields.get("minYField").getText());
    appSettings.setProperty("maxX", fields.get("maxXField").getText());
    appSettings.setProperty("maxY", fields.get("maxYField").getText());
    appSettings.setProperty("steps", stepsSlider.getValue() + "");
    appSettings.setProperty("realPart", fields.get("realPartField").getText());
    appSettings.setProperty("imaginaryPart", fields.get("imaginaryPartField").getText());
    appSettings.setProperty("transformation",
        ((RadioButton) transformationsGroup.getSelectedToggle()).getText());
    appSettings.setProperty("colorMode", String.valueOf(colorModeCheckbox.isSelected()));
    appSettings.setProperty("juliaToggleSwitch", String.valueOf(juliaToggleSwitch.isSelected()));
    handleActionController.saveSettings(appSettings);
  }
}
