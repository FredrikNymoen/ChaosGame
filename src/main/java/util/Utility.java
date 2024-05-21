package util;

/**
 * A utility class that holds constants used throughout the Chaos Game application.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class Utility {

  private Utility() {
  }

  /**
   * The name of the application.
   */
  public static final String APPLICATION_NAME = "Chaos Game";

  /**
   * The default width of the Chaos Game window.
   */
  public static final int CHAOS_GAME_WIDTH = 900;

  /**
   * The default height of the Chaos Game window.
   */
  public static final int CHAOS_GAME_HEIGHT = 750;

  /**
   * The style class for bold labels.
   */
  public static final String BOLD_LABEL = "bold-label";

  /**
   * The style class for small labels.
   */
  public static final String SMALL_LABEL = "small-label";

  /**
   * The style class for option buttons.
   */
  public static final String OPTION_BUTTON = "option-button";

  /**
   * The file path for the application settings.
   */
  public static final String SETTINGS_FILE_PATH = "appSettings.properties";

  /**
   * The file path for the shown transformation file.
   */
  public static final String SHOWN_TRANSFORMATION_FILE_PATH = "file.csv";

  /**
   * A 2D array containing the different types of transformations. The transformations are grouped
   * into two rows: the first row contains "Affine", "Barnsley", "Julia", and "Sierpinski"; the
   * second row contains "Mandelbrot" and "Maple-Tree".
   */
  public static final String[][] TRANSFORMATIONS = {
      {"Affine", "Barnsley", "Julia", "Sierpinski"},
      {"Mandelbrot", "Maple-Tree"}
  };

  /**
   * The CSS style string for a red border.
   */
  public static final String RED_BORDER = "-fx-border-color: red;";

  public static final String FILE_NOT_FOUND = "File not found.";
  public static final String ERROR_READING_FILE = "Error reading file.";
  public static final String ERROR_WRITING_TO_FILE = "Error writing to file.";

}