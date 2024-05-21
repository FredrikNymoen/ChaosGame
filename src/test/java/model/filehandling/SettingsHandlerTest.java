package model.filehandling;

import static org.junit.jupiter.api.Assertions.*;

import exception.FileEmptyException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the SettingsHandler class.
 * This class tests the loadSettings and saveSettings methods of the SettingsHandler class.
 * author Fredrik Nymoen & Amund Larsen
 * version v1.0.0
 */
class SettingsHandlerTest {
  private SettingsHandler settingsHandler;
  private File tempFile;

  /**
   * Sets up a temporary file for testing, before each test.
   */
  @BeforeEach
  void setUp() throws IOException {
    tempFile = File.createTempFile("testSettings", ".properties");
    settingsHandler = new SettingsHandler(tempFile.getAbsolutePath());
  }

  /**
   * Deletes the temporary file after each test.
   */
  @AfterEach
  void tearDown() {

  }

  /**
   * Tests loading settings from a non-empty file.
   * It verifies that the settings are loaded correctly.
   */
  @Test
  void testLoadSettings() throws Exception {
    Properties expectedProperties = new Properties();
    expectedProperties.setProperty("key1", "value1");
    expectedProperties.setProperty("key2", "value2");

    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
      expectedProperties.store(fos, "Test Settings");
    }

    Properties loadedProperties = settingsHandler.loadSettings();
    assertEquals("value1", loadedProperties.getProperty("key1"));
    assertEquals("value2", loadedProperties.getProperty("key2"));
  }

  /**
   * Tests loading settings from an empty file.
   * It verifies that a FileEmptyException is thrown.
   */
  @Test
  void testLoadSettingsFileEmptyException() {
    Exception exception = assertThrows(FileEmptyException.class, () -> settingsHandler.loadSettings());

    String expectedMessage = "Settings file is empty.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
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

    settingsHandler.saveSettings(propertiesToSave);

    Properties loadedProperties = new Properties();
    try (FileInputStream fis = new FileInputStream(tempFile)) {
      loadedProperties.load(fis);
    }

    assertEquals("value1", loadedProperties.getProperty("key1"));
    assertEquals("value2", loadedProperties.getProperty("key2"));
  }

  /**
   * Tests saving settings to a file and handling an IOException.
   * It verifies that an IOException is thrown when the file path is invalid.
   */
  @Test
  void testSaveSettingsIOException() {
    SettingsHandler invalidSettingsHandler = new SettingsHandler("/invalid/path/to/settings.properties");
    Properties propertiesToSave = new Properties();
    propertiesToSave.setProperty("key1", "value1");

    Exception exception = assertThrows(IOException.class, () -> invalidSettingsHandler.saveSettings(propertiesToSave));

    String expectedMessage = "Error writing to file:";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
