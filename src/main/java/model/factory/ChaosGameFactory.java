package model.factory;

import model.chaosGame.ChaosCanvas;
import model.chaosGame.ChaosGame;
import model.chaosGame.ChaosGameDescription;
import model.mathcore.Complex;
import model.mathcore.Vector2D;
import util.Utility;

public class ChaosGameFactory {

  /**
   * Creates a chaos game with a specified description, width, height and number of steps. The
   * method creates a ChaosGame object with the specified parameters and runs the chaos game for the
   * specified number of steps.
   *
   * @param description the description of the chaos game
   * @param width       the width of the canvas
   * @param height      the height of the canvas
   * @param steps       the number of steps to run the chaos game
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

  public ChaosGame createJuliaChaosGame(Complex c) {
    double r = 2;

    ChaosGame chaosGame = new ChaosGame(Utility.CHAOS_GAME_WIDTH, Utility.CHAOS_GAME_HEIGHT);
    ChaosCanvas canvas = chaosGame.getCanvas();

    // Assuming the fractal drawing's size for positioning
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
        double xtemp = 0;

        // Iterative escape test(r*r)
        while ((x * x + y * y) < (r*r) && iteration < maxIterations) {
          xtemp = x * x - y * y;
          y = 2 * x * y + c.getX1();
          x = xtemp + c.getX0();
          iteration++;
        }

        if (iteration == maxIterations) {
          if(canvas.getPixel(vector) == 1){
          }
          else {
            canvas.putPixel(vector);
          }
        }
      }
    }

    return chaosGame;
  }

  public ChaosGame createMandelbrotChaosGame(){
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

        double x = 0, y = 0;
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
          canvas.putPixel(vector); // Assuming this method marks the pixel based on iteration or color
        }
      }
    }

    return chaosGame;
  }

}
