package model.filehandling;

import exception.FileEmptyException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import util.ErrorHandling;

public class SettingsHandler {
  private Properties appSettings = new Properties();
  private final String settingsFilePath;

  public SettingsHandler(String settingsFilePath) {
    this.settingsFilePath = settingsFilePath;
  }

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
