package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import model.chaosgame.ChaosGame;

/**
 * This class handles the drawing of fractals on a canvas.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class FractalDrawer {

  /**
   * Draws the fractal on the specified canvas based on the ChaosGame data.
   *
   * @param fractalCanvas     the canvas on which the fractal is drawn
   * @param chaosGame         the current chaos game instance containing fractal data
   * @param colorModeCheckbox the checkbox indicating whether color mode is enabled
   */
  public void drawFractal(Canvas fractalCanvas, ChaosGame chaosGame, CheckBox colorModeCheckbox) {
    GraphicsContext gc = fractalCanvas.getGraphicsContext2D();
    gc.clearRect(0, 0, fractalCanvas.getWidth(), fractalCanvas.getHeight());

    int[][] canvasArray = chaosGame.getCanvas().getCanvasArray();
    // Use a set to avoid duplicate values and then convert to a list to sort
    Set<Integer> valueSet = new HashSet<>();
    for (int[] row : canvasArray) {
      for (int value : row) {
        if (value > 0) {
          valueSet.add(value);
        }
      }
    }
    List<Integer> sortedValues = new ArrayList<>(valueSet);
    Collections.sort(sortedValues);

    // Dynamically calculate the center of the canvas
    double centerX = fractalCanvas.getWidth() / 2;
    double centerY = fractalCanvas.getHeight() / 2;

    double fractalWidth = canvasArray[0].length;
    double fractalHeight = canvasArray.length;

    // Calculate the top-left corner of where the fractal should be drawn
    double startX = centerX - fractalWidth / 2;
    double startY = centerY - fractalHeight / 2;

    // Adjust the drawing loop to position the fractal correctly
    for (int i = 0; i < fractalHeight; i++) {
      for (int j = 0; j < fractalWidth; j++) {
        int value = canvasArray[i][j];
        if (colorModeCheckbox.isSelected() && value > 0) {
          int index = sortedValues.indexOf(value);
          if (sortedValues.get(index) == 1) {
            gc.setFill(Color.BLUE);
          } else {
            double intensity = (double) index / (sortedValues.size() - 1);
            Color color = getColorForValue(intensity);
            gc.setFill(color);
          }
        } else if (value > 0) {
          gc.setFill(Color.BLACK);
        } else {
          gc.setFill(Color.WHITE); // Background color
        }
        gc.fillRect(startX + j, startY + i, 1, 1); // Draw pixel
      }
    }
  }

  /**
   * Returns a color for a given intensity value. The color transitions through blue, green, yellow,
   * orange, and red based on the intensity.
   *
   * @param intensity a double value representing the intensity (ranging from 0.0 to 1.0)
   * @return a Color object representing the interpolated color
   */
  public Color getColorForValue(double intensity) {
    if (intensity < 0.25) {
      // Interpolate between blue (0) and green (0.25)
      return Color.BLUE.interpolate(Color.GREEN, intensity * 4);
    } else if (intensity < 0.5) {
      // Interpolate between green (0.25) and yellow (0.5)
      return Color.GREEN.interpolate(Color.YELLOW, (intensity - 0.25) * 4);
    } else if (intensity < 0.75) {
      // Interpolate between yellow (0.5) and orange (0.75)
      return Color.YELLOW.interpolate(Color.ORANGE, (intensity - 0.5) * 4);
    } else {
      // Interpolate between orange (0.75) and red (1)
      return Color.ORANGE.interpolate(Color.RED, (intensity - 0.75) * 4);
    }
  }
}
