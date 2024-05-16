package filehandling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsHandler {
  private Properties appSettings = new Properties();
  private final String settingsFilePath;

  public SettingsHandler(String settingsFilePath) {
    this.settingsFilePath = settingsFilePath;
  }

  public Properties loadSettings() {
    try (FileInputStream fis = new FileInputStream(settingsFilePath)) {
      appSettings.load(fis);
    } catch (IOException e) {
      System.out.println("Failed to load settings: " + e.getMessage());
    }
    return appSettings;
  }

  public void saveSettings(Properties appSettings) {
    try (FileOutputStream fos = new FileOutputStream(settingsFilePath)) {
      appSettings.store(fos, "Application Settings");
    } catch (IOException e) {
      System.out.println("Failed to save settings: " + e.getMessage());
    }
  }
}
