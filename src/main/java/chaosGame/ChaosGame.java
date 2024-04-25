package chaosGame;

import chaosGame.ChaosCanvas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import mathcore.Matrix2x2;
import mathcore.Vector2D;
import transformations.AffineTransform2D;
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

  public ChaosGame(int width, int height) {
    this.canvas = new ChaosCanvas(width, height, new Vector2D(-1, -1), new Vector2D(1, 1));
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

  public void runSteps(int steps) {
      canvas.clear();
      for (int i = 0; i < steps; i++) {
        int transformIndex = random.nextInt(description.getTransforms().size());
        Transform2D transform = description.getTransforms().get(transformIndex);
        currentPoint = transform.transform(currentPoint);
        canvas.putPixel(currentPoint);
      }
  }

  public void runStepsForBarnsley(int steps){
    canvas.clear();
    for (int i = 0; i < steps; i++) {
      // Define the cumulative probabilities
      List<Double> probabilities = new ArrayList<>();
      probabilities.add(1.0);   // 1% for the first element
      probabilities.add(86.0);  // 85% for the second element (1% + 85%)
      probabilities.add(93.0);  // 7% for the third element (86% + 7%)
      probabilities.add(100.0); // 7% for the fourth element (93% + 7%)

      // Get a random value between 0 and 100
      double randomValue = 100 * random.nextDouble();

      // Determine which index the random value falls into
      int transformIndex = 0;
      for (int j = 0; j < probabilities.size(); j++) {
        if (randomValue < probabilities.get(j)) {
          transformIndex = j;
          break;
        }
      }
      // Retrieve the transform based on the selected index
      List<Transform2D> transforms = description.getTransforms();
      Transform2D transform = transforms.get(transformIndex);
      currentPoint = transform.transform(currentPoint);
      canvas.putPixel(currentPoint);
    }
  }

  public void makeFullFractal() {
    canvas.clear();
    do {
      currentPoint = pointStack.pop();

      for (int j = 0; j < description.getTransforms().size(); j++) {
        Transform2D transform = description.getTransforms().get(j);
        Vector2D newPoint = transform.transform(currentPoint);
        if (canvas.getPixel(newPoint) >= 6) {
          continue;
        }
        pointStack.push(newPoint);
        canvas.putPixel(newPoint);
      }
    } while (!pointStack.isEmpty());
    pointStack.push(new Vector2D(0, 0));
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

  public void setCanvas(ChaosCanvas canvas) {
    this.canvas = canvas;
  }

}
