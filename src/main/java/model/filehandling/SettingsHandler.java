package model.filehandling;

import exception.FileEmptyException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * The SettingsHandler class is used to read and write application settings to and from files.
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class SettingsHandler {
  private Properties appSettings = new Properties();
  private final String settingsFilePath;

  /**
   * Constructor for SettingsHandler.
   * @param settingsFilePath the path to the settings file
   */
  public SettingsHandler(String settingsFilePath) {
    this.settingsFilePath = settingsFilePath;
  }

  /**
   * Loads application settings from a specified file.
   * @throws Exception if the file is not found, empty, or if an error occurs while reading the file
   * @return a Properties object representing the application settings
   */
  public Properties loadSettings() throws Exception{
    try (FileInputStream fis = new FileInputStream(settingsFilePath)) {
      if (fis.available() == 0) {
        throw new FileEmptyException("Settings file is empty.");
      }
      appSettings.load(fis);
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    }
    catch (FileEmptyException e) {
      throw new FileEmptyException("Settings file is empty.");
    }
    catch (Exception e) {
      throw new Exception("Error reading file.");
    }
    return appSettings;
  }

  /**
   * Saves application settings to a specified file.
   * @param appSettings the application settings to be saved
   * @throws Exception if an error occurs while writing the settings to the file
   */
  public void saveSettings(Properties appSettings) throws Exception{
    try (FileOutputStream fos = new FileOutputStream(settingsFilePath)) {
      appSettings.store(fos, "Application Settings");
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (Exception e) {
      throw new Exception("Error writing to file.");
    }
  }
}
