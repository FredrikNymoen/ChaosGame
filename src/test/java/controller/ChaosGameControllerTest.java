package controller;

import java.util.List;
import java.util.Properties;
import model.chaosgame.ChaosGameDescription;
import model.factory.ChaosGameDescriptionFactory;
import model.filehandling.SettingsHandler;
import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import model.transformations.AffineTransform2D;
import model.transformations.Transform2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Utility;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ChaosGameController.
 * This class contains unit tests for the ChaosGameController class methods.
 * It verifies the functionality of creating and managing Chaos Game objects,
 * handling settings, and reading and writing to files.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
class ChaosGameControllerTest {

  private ChaosGameController chaosGameController;

  /**
   * Sets up the test environment before each test.
   * It initializes the ChaosGameController and its dependencies.
   */
  @BeforeEach
  public void setUp() {
    chaosGameController = new ChaosGameController();
  }


  /**
   * Tests loading settings from a file.
   * It verifies that the settings are loaded correctly.
   */
  @Test
  void testLoadSettings() throws Exception {
    Properties expectedProperties = new Properties();
    expectedProperties.setProperty("key1", "value1");
    expectedProperties.setProperty("key2", "value2");

    // Write test properties to the settings file
    SettingsHandler settingsHandler = new SettingsHandler(Utility.SETTINGS_FILE_PATH);
    settingsHandler.saveSettings(expectedProperties);

    Properties loadedProperties = chaosGameController.loadSettings();
    assertEquals("value1", loadedProperties.getProperty("key1"));
    assertEquals("value2", loadedProperties.getProperty("key2"));
  }

  /**
   * Tests saving settings to a file.
   * It verifies that the settings are saved correctly.
   */
  @Test
  void testSaveSettings() throws Exception {
    Properties propertiesToSave = new Properties();
    propertiesToSave.setProperty("key1", "value1");
    propertiesToSave.setProperty("key2", "value2");

    chaosGameController.saveSettings(propertiesToSave);

    // Load the settings directly to verify they were saved correctly
    SettingsHandler settingsHandler = new SettingsHandler(Utility.SETTINGS_FILE_PATH);
    Properties loadedProperties = settingsHandler.loadSettings();
    assertEquals("value1", loadedProperties.getProperty("key1"));
    assertEquals("value2", loadedProperties.getProperty("key2"));
  }


  /**
   * Tests reading a ChaosGameDescription from a file.
   * It verifies that the ChaosGameDescription object is read correctly.
   */
  @Test
  void testReadFromFile() throws Exception {
    ChaosGameDescriptionFactory chaosGameDescriptionFactory = new ChaosGameDescriptionFactory();
    ChaosGameDescription expectedDescription = chaosGameDescriptionFactory.mapleTree(new Vector2D(0, 0), new Vector2D(1, 1));

    // Write test description to file
    chaosGameController.writeToFile(expectedDescription, "maple-tree");

    ChaosGameDescription actualDescription = chaosGameController.readFromFile();
    String expectedDescriptionMinCoords = expectedDescription.getMinCoords().getX0() + ", " + expectedDescription.getMinCoords().getX1();
    String actualDescriptionMinCoords = actualDescription.getMinCoords().getX0() + ", " + actualDescription.getMinCoords().getX1();
    String expectedDescriptionMaxCoords = expectedDescription.getMaxCoords().getX0() + ", " + expectedDescription.getMaxCoords().getX1();
    String actualDescriptionMaxCoords = actualDescription.getMaxCoords().getX0() + ", " + actualDescription.getMaxCoords().getX1();

    assertEquals(expectedDescriptionMinCoords, actualDescriptionMinCoords);
    assertEquals(expectedDescriptionMaxCoords, actualDescriptionMaxCoords);

    List<Transform2D> expectedTransforms = expectedDescription.getTransforms();
    List<Transform2D> actualTransforms = actualDescription.getTransforms();
    for (int i = 0; i < expectedTransforms.size(); i++) {
      Matrix2x2 expectedMatrix = ((AffineTransform2D) expectedTransforms.get(i)).getMatrix();
      Matrix2x2 actualMatrix = ((AffineTransform2D) actualTransforms.get(i)).getMatrix();
      Vector2D expectedVector = ((AffineTransform2D) expectedTransforms.get(i)).getVector();
      Vector2D actualVector = ((AffineTransform2D) actualTransforms.get(i)).getVector();
      String expectedMatrixString = expectedMatrix.geta00() + ", " + expectedMatrix.geta01() + ", " + expectedMatrix.geta10() + ", " + expectedMatrix.geta11();
      String actualMatrixString = actualMatrix.geta00() + ", " + actualMatrix.geta01() + ", " + actualMatrix.geta10() + ", " + actualMatrix.geta11();
      String expectedVectorString = expectedVector.getX0() + ", " + expectedVector.getX1();
      String actualVectorString = actualVector.getX0() + ", " + actualVector.getX1();

      assertEquals(expectedMatrixString, actualMatrixString);
      assertEquals(expectedVectorString, actualVectorString);
    }

  }

  /**
   * Tests reading the transformation type from a file.
   * It verifies that the transformation type is read correctly.
   */
  @Test
  void testReadTransformationType() throws Exception {
    String expectedType = "barnsley";
    String line = "Affine2d, " + expectedType;

    // Write test transformation type to file
    chaosGameController.writeLineToFile(line);

    String actualType = chaosGameController.readTransformationType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Tests writing a ChaosGameDescription to a file.
   * It verifies that the ChaosGameDescription is written correctly.
   */
  @Test
  void testWriteToFile() throws Exception {
    ChaosGameDescriptionFactory chaosGameDescriptionFactory = new ChaosGameDescriptionFactory();
    ChaosGameDescription description = chaosGameDescriptionFactory.mapleTree(new Vector2D(0, 0), new Vector2D(1, 1));
    String transformationType = "maple-tree";

    chaosGameController.writeToFile(description, transformationType);

    String expectedTransformationType = chaosGameController.readTransformationType();

    assertEquals(transformationType, expectedTransformationType);
  }

  /**
   * Tests writing a line to a file.
   * It verifies that the line is written correctly.
   */
  @Test
  void testWriteLineToFile() throws Exception {
    String line = "Mandelbrot";

    chaosGameController.writeLineToFile(line);

    assertTrue(chaosGameController.checkForMandelbrot());
  }


  /**
   * Tests checking for Mandelbrot description in a file.
   * It verifies that the Mandelbrot check is performed correctly.
   */
  @Test
  void testCheckForMandelbrot() throws Exception {
    String line = "Mandelbrot";

    chaosGameController.writeLineToFile(line);

    assertTrue(chaosGameController.checkForMandelbrot());
  }
}
