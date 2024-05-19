package view;

import controller.ChaosGameObserver;
import controller.HandleActionController;
import controller.ValidationController;
import java.util.Map;
import model.chaosGame.ChaosGame;
import model.filehandling.SettingsHandler;
import java.util.Properties;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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
import javafx.scene.canvas.Canvas;
import util.ErrorHandling;
import util.UIHelper;
import util.Utility;

public class MainGUI extends Application{
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
  private Canvas fractalCanvas;
  private ChaosGame currentChaosGame;

  private final ChaosGameObserver observer = new EventHandler();
  private final Layout layout = new Layout();
  private final ValidationController validationController = new ValidationController();
  private final HandleActionController handleActionController = new HandleActionController(observer);



  public static void main(String[] args) {
    launch(args);
  }

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

    configureLeftSide(root);
    configureRightSide(root);
    setupListeners();

    primaryStage.setOnCloseRequest(event -> saveSettings());
    loadSettings();

    Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass().getResource("/chaosgame.css").toExternalForm());
    primaryStage.setTitle(Utility.APPLICATION_NAME);
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true); // Set the stage to be maximized
    primaryStage.show();
  }

  public void configureScrollPane() {
    // Scrollable Left side layout
    scrollPane = layout.createLeftsideScrollPane();
    leftSide = (VBox) scrollPane.getContent();
  }

  public void configureTransformationButtonOptions() {
    // Transformation checkboxes
    transformationBox = layout.createTransformationsBox();
    transformationsGroup = new ToggleGroup();
    layout.addTransformationOptions(transformationBox, transformationsGroup);
  }


  public void configureStepsBox() {
    // Steps input
    stepsBox = layout.createStepsBox();
    stepsSlider = (Slider) stepsBox.getChildren().get(1);

    // Bind the slider's value to the label
    stepsSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
        -> observer.onSliderValueChanged(stepsBox, newValue.intValue()));
  }

  public void configureCoordinateFields() {
    coordGrid = layout.createCoordGrid();
  }

  public void configureJuliaFields() {
    juliaGrid = layout.createJuliaGrid();
    juliaToggleSwitch = (ToggleButton) UIHelper.getNodeFromGridPane(juliaGrid, 0, 1);

    // Add an event handler to change the text when the button is toggled
    juliaToggleSwitch.setOnAction(event -> observer.onJuliaToggleSwitched(juliaToggleSwitch, coordGrid, stepsBox, iterativeTransformationButton));
  }

  public void configureAffineBox() {
    affineBox = layout.createAffineBox();
    affineGrid = (GridPane) affineBox.getChildren().get(1);
    observer.addMatrixVectorRow(0, affineGrid, layout);

    Button addButton = (Button) ((HBox) affineBox.getChildren().get(2)).getChildren().get(0);
    Button removeButton = (Button) ((HBox) affineBox.getChildren().get(2)).getChildren().get(1);
    addButton.setOnAction(event -> observer.addMatrixVectorRow(affineGrid.getRowCount(), affineGrid, layout));
    removeButton.setOnAction(event -> observer.removeMatrixVectorRow(affineGrid));
  }


  public void configureShowButton() {
    showButton = new Button("Show");
    showButton.getStyleClass().add("show-button");
    showButton.setOnAction(event -> {
      observer.resetFieldsToDefaultStyle(affineGrid, juliaGrid, affineBox);  // Reset all fields to default style
      boolean allFieldsValid = validationController.isAllFieldsValid(juliaGrid, transformationsGroup, affineGrid, coordGrid);
      missingInputMessage.setVisible(!allFieldsValid);

      // Proceed only if all fields are valid
      if (allFieldsValid) {
        int steps = (int) stepsSlider.getValue();
        // Reset all fields to default style
        observer.resetFieldsToDefaultStyle(affineGrid, juliaGrid, affineBox);
        currentChaosGame = handleActionController.showButtonClicked(transformationsGroup,
                affineGrid, juliaGrid, coordGrid, steps);
        observer.drawFractal(fractalCanvas, currentChaosGame, colorModeCheckbox);
      }
    });
  }

  public void configureIterativeTransformationButton() {
    iterativeTransformationButton = layout.createIterativeTransformationButton();
    /*iterativeTransformationButton.setOnAction(event -> {
      try{
      currentChaosGame = controller.handleTransformationSelection(transformationsGroup, affineGrid, juliaGrid, coordGrid, (int) stepsSlider.getValue());
      currentChaosGame.fractalWithIterationTransformation();
      observer.drawFractal(fractalCanvas, currentChaosGame, colorModeCheckbox);
      }
      catch (Exception e) {
        errorHandling.failedToMakeFractalWithIterativeTransformation(e);
      }
    });*/
    iterativeTransformationButton.setOnAction(event
        -> handleActionController.handleIterativeTransformation(transformationsGroup, affineGrid, juliaGrid, coordGrid, stepsSlider.getValue(), fractalCanvas, colorModeCheckbox));
  }

  public void configureMissingInputMessage() {
    missingInputMessage = new Label("Please fill in all required fields.");
    missingInputMessage.setStyle("-fx-text-fill: red;");
    missingInputMessage.setVisible(false);
  }

  public void configureColorModeCheckbox() {
    colorModeCheckbox = new CheckBox("Enable Heatmap Color Mode");
    colorModeCheckbox.setSelected(false);
    colorModeCheckbox.getStyleClass().add("heatmap-checkbox");
  }

  public void configureCopyLastTransformationButton() {
    copyLastTransformationButton = layout.createCopyLastTransformationButton();
    /*try {
      copyLastTransformationButton.setOnAction(event
          -> observer.copyLastTransformation(transformationsGroup, coordGrid, affineGrid,
              juliaGrid, juliaToggleSwitch, layout));
    }
    catch (Exception e) {
      errorHandling.failedToCopyLastTransformation(e);
    }*/
    copyLastTransformationButton.setOnAction(event -> handleActionController.handleCopyLastTransformation(
        transformationsGroup, coordGrid, affineGrid, juliaGrid, juliaToggleSwitch, layout));
  }


  public void configureLeftSide(BorderPane root) {
    leftSide.getStyleClass().add("left-side");
    VBox spacingBox = new VBox();
    spacingBox.setMinHeight(10);
    VBox spacingBox2 = new VBox();
    spacingBox2.setMinHeight(4);

    HBox centeredShowButtonBox = layout.createCenteredHBox(showButton);
    HBox centeredColorModeCheckboxBox = layout.createCenteredHBox(colorModeCheckbox);
    HBox centeredIterativeTransformationBox = layout.createCenteredHBox(iterativeTransformationButton);
    HBox centeredCopyTransformationButtonBox = layout.createCenteredHBox(copyLastTransformationButton);

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
        spacingBox2
    );

    layout.setupLeftSideWithSeperatorLine(scrollPane, leftSide, root);
  }

  public void configureRightSide(BorderPane root) {
    fractalCanvas = new Canvas();

    fractalCanvas.widthProperty().bind(root.widthProperty().subtract(scrollPane.getPrefWidth()));
    fractalCanvas.heightProperty().bind(root.heightProperty());

    root.setRight(fractalCanvas);
  }


  public void setupListeners() {
    fractalCanvas.widthProperty().addListener(obs -> observer.onCanvasSizeChanged(fractalCanvas, currentChaosGame, colorModeCheckbox));
    fractalCanvas.heightProperty().addListener(obs -> observer.onCanvasSizeChanged(fractalCanvas, currentChaosGame, colorModeCheckbox));
    initializeRadioButtonListener();
  }

  public void initializeRadioButtonListener(){
    affineBox.setDisable(true);
    juliaGrid.setDisable(true);
    transformationsGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
      /*try {
        if (newToggle != null) {
          RadioButton selectedButton = (RadioButton) newToggle;
          observer.onTransformationSelected(leftSide, selectedButton.getText());
        }
      }
      catch (Exception e) {
        errorHandling.failedToSelectTransformation(e);
      }
    });*/
      if (newToggle != null) {
        RadioButton selectedButton = (RadioButton) newToggle;
        handleActionController.handleTransformationSelected(leftSide, selectedButton.getText());
      }
    });
  }


  public void loadSettings() {
    Map<String, TextField> fields = UIHelper.getTextFieldsCoordAndJuliaMap(coordGrid, juliaGrid);

    //Properties appSettings = settingsHandler.loadSettings();
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
          .filter(
              t -> t.getUserData().equals(appSettings.getProperty("transformation", "Affine")))
          .findFirst().orElse(null);
      if (selectedButton != null) {
        transformationsGroup.selectToggle(selectedButton);
      }
    } catch (Exception e) {
      transformationsGroup.selectToggle(transformationsGroup.getToggles().get(0));
    }
    colorModeCheckbox.setSelected(Boolean.parseBoolean(appSettings.getProperty("colorMode", "false")));
    juliaToggleSwitch.setSelected(Boolean.parseBoolean(appSettings.getProperty("juliaToggleSwitch", "false")));
  }


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
    appSettings.setProperty("transformation", ((RadioButton) transformationsGroup.getSelectedToggle()).getText());
    appSettings.setProperty("colorMode", String.valueOf(colorModeCheckbox.isSelected()));
    appSettings.setProperty("juliaToggleSwitch", String.valueOf(juliaToggleSwitch.isSelected()));
    //settingsHandler.saveSettings(appSettings);
    handleActionController.saveSettings(appSettings);
  }

}