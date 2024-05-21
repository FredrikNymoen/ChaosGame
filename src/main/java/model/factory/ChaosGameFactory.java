package model.factory;

import model.chaosgame.ChaosCanvas;
import model.chaosgame.ChaosGame;
import model.chaosgame.ChaosGameDescription;
import model.mathcore.Complex;
import model.mathcore.Vector2D;
import util.Utility;

/**
 * The ChaosGameFactory class is used to create ChaosGame objects with specified parameters.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class ChaosGameFactory {

  /**
   * Creates a chaos game with a specified description, width, height and number of steps. The
   * method creates a ChaosGame object with the specified parameters and runs the chaos game for the
   * specified number of steps.
   *
   * @param description              the description of the chaos game
   * @param width                    the width of the canvas
   * @param height                   the height of the canvas
   * @param steps                    the number of steps to run the chaos game
   * @param isBarnsleyTransformation whether to use Barnsley transformation or not
   * @return ChaosGame the created chaos game
   */
  public ChaosGame createChaosGame(ChaosGameDescription description, int width, int height,
      int steps, boolean isBarnsleyTransformation) {
    ChaosGame chaosGame = new ChaosGame(description, width, height);
    if (isBarnsleyTransformation) {
      chaosGame.runStepsForBarnsley(steps);
    } else {
      chaosGame.runSteps(steps);
    }
    return chaosGame;
  }

  /**
   * Creates a julia chaos game with convergence mode. Convergence mode checks if the coordinates to
   * the pixel converges. If it does, the pixel is included in the fractal.
   *
   * @param c the complex number c
   * @return ChaosGame the created chaos game
   */
  public ChaosGame createJuliaChaosGameWithConvergenceMode(Complex c) {
    double r = 2;
    ChaosGame chaosGame = new ChaosGame(Utility.CHAOS_GAME_WIDTH, Utility.CHAOS_GAME_HEIGHT);
    ChaosCanvas canvas = chaosGame.getCanvas();

    double width = canvas.getCanvasArray()[0].length;
    double height = canvas.getCanvasArray().length;

    // Loop through each pixel on the screen
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
        Vector2D vector = canvas.pixelToCoordinate(new Vector2D(i, j));
        double x = vector.getX0();
        double y = vector.getX1();

        int iteration = 0;
        int maxIterations = 40; // Maximum iterations for convergence check
        double xtemp;

        // Iterative escape test(r*r)
        while ((x * x + y * y) < (r * r) && iteration < maxIterations) {
          xtemp = x * x - y * y;
          y = 2 * x * y + c.getX1();
          x = xtemp + c.getX0();
          iteration++;
        }

        if (iteration == maxIterations && (canvas.getPixel(vector) != 1)) {
          canvas.putPixel(vector);
        }
      }
    }
    return chaosGame;
  }

  /**
   * Creates a mandelbrot chaos game. Checks if the coordinates to the pixel converges. If it does,
   * the pixel is included in the fractal.
   *
   * @return ChaosGame the created chaos game
   */
  public ChaosGame createMandelbrotChaosGame() {
    double r = 2; // Escape radius

    ChaosGame chaosGame = new ChaosGame(Utility.CHAOS_GAME_WIDTH, Utility.CHAOS_GAME_HEIGHT);
    ChaosCanvas canvas = chaosGame.getCanvas();

    double width = canvas.getCanvasArray()[0].length;
    double height = canvas.getCanvasArray().length;

    // Loop through each pixel on the screen
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
        Vector2D vector = canvas.pixelToCoordinate(new Vector2D(i, j));
        double cx = vector.getX0();
        double cy = vector.getX1();

        double x = 0;
        double y = 0;
        int iteration = 0;
        int maxIterations = 40; // Maximum iterations for convergence check

        while ((x * x + y * y) < (r * r) && iteration < maxIterations) {
          double xtemp = x * x - y * y + cx;
          y = 2 * x * y + cy;
          x = xtemp;
          iteration++;
        }

        // Mark the pixel based on whether it escaped or not
        if (iteration < maxIterations) {
          canvas.putPixel(vector);
        }
      }
    }
    return chaosGame;
  }

}
