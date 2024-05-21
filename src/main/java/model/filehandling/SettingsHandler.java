package model.filehandling;

import exception.FileEmptyException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The SettingsHandler class is used to read and write application settings to and from files.
 * author Fredrik Nymoen & Amund Larsen
 * version v1.0.0
 */
public class SettingsHandler {

  private final String settingsFilePath;
  private final Properties appSettings = new Properties();

  /**
   * Constructor for SettingsHandler.
   *
   * @param settingsFilePath the path to the settings file
   */
  public SettingsHandler(String settingsFilePath) {
    this.settingsFilePath = settingsFilePath;
  }

  /**
   * Loads application settings from a specified file.
   *
   * @return a Properties object representing the application settings
   * @throws IOException if an I/O error occurs while reading the file
   * @throws FileEmptyException if the file is empty
   */
  public Properties loadSettings() throws IOException, FileEmptyException {
    try (FileInputStream fis = new FileInputStream(settingsFilePath)) {
      if (fis.available() == 0) {
        throw new FileEmptyException("Settings file is empty.");
      }
      appSettings.load(fis);
    } catch (IOException e) {
      throw new IOException("Error reading file: " + settingsFilePath, e);
    }
    return appSettings;
  }

  /**
   * Saves application settings to a specified file.
   *
   * @param appSettings the application settings to be saved
   * @throws IOException if an I/O error occurs while writing the settings to the file
   */
  public void saveSettings(Properties appSettings) throws IOException {
    try (FileOutputStream fos = new FileOutputStream(settingsFilePath)) {
      appSettings.store(fos, "Application Settings");
    } catch (IOException e) {
      throw new IOException("Error writing to file: " + settingsFilePath, e);
    }
  }
}
