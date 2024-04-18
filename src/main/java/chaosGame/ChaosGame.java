package chaosGame;

import chaosGame.ChaosCanvas;
import java.util.Random;
import java.util.Stack;
import mathcore.Vector2D;
import transformations.Transform2D;

/**
 * chaosGame.ChaosGame class is used to represent a chaos game where you can generate fractals or other complex structures
 * through repeated application of random transformations. It uses chaosGame.ChaosCanvas where the results of these
 * transformations are plotted.
 */
public class ChaosGame {
  private ChaosCanvas canvas;
  private ChaosGameDescription description;
  private Vector2D currentPoint;
  public Random random;
  private Stack<Vector2D> pointStack;

    /**
     * Constructor for the chaosGame.ChaosGame class. Constructs a new chaos game using a specified set of rules and dimensions
     * for the canvas.
     *
     * @param description the description of the chaos game
     * @param width the width of the canvas
     * @param height the height of the canvas
     */

  public ChaosGame(ChaosGameDescription description, int width, int height) {
    this.description = description;
    this.canvas = new ChaosCanvas(width, height, description.getMinCoords(), description.getMaxCoords());
    this.currentPoint = new Vector2D(0, 0);
    this.random = new Random();
    this.pointStack = new Stack<>();
    pointStack.push(currentPoint);
  }
  /**
   * Returns the canvas of the chaos game.
   *
   * @return chaosGame.ChaosCanvas the canvas of the chaos game
   */
  public ChaosCanvas getCanvas() {
    return canvas;
  }
    /**
     * Runs a specified number of steps in the Chaos Game. Each step a random transformation is selected and applied
     * to the current point, where the result is plotted on the canvas.
     *
     * @param steps the number of steps to run the chaos game
     */

  /*public void runSteps(int steps) {
    for (int i = 0; i < steps; i++) {
      int transformIndex = random.nextInt(description.getTransforms().size());
      Transform2D transform = description.getTransforms().get(transformIndex);
      currentPoint = transform.transform(currentPoint);
      canvas.putPixel(currentPoint);
    }
  }*/

  public void runSteps(int steps) {
      while (pointStack.size() != 0) {
        Vector2D currentPoint = pointStack.pop();

        for(int j = 0; j < description.getTransforms().size(); j++) {
          Transform2D transform = description.getTransforms().get(j);
          Vector2D newPoint = transform.transform(currentPoint);
          if(canvas.getCanvasArray()[(int) newPoint.getX0()][(int) newPoint.getX1()] == 0){
            pointStack.push(newPoint);
            canvas.putPixel(newPoint);
          }
        }
      }
  }

  public void display(){
    int[][] canvasArray = getCanvas().getCanvasArray();
    for (int i = 0; i < canvasArray.length; i++) {
      for (int j = 0; j < canvasArray[i].length; j++) {
        if (canvasArray[i][j] == 0) {
          System.out.print(" ");
        } else {
          //System.out.print("■");
          System.out.print("X");
        }
      }
      System.out.println();
    }
  }

}
