package model.chaosgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import model.mathcore.Vector2D;
import model.transformations.Transform2D;

/**
 * The ChaosGame class is used to represent a chaos game.
 * The chaos game is a mathematical game that generates a fractal using a set of rules and
 * transformations.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class ChaosGame {
  private ChaosCanvas canvas;
  private ChaosGameDescription description;
  private Vector2D currentPoint;
  private Random random;
  private Stack<Vector2D> pointStack;


  /**
   * Constructor for ChaosGame.
   *
   * @param description the description of the chaos game
   * @param width the width of the canvas
   * @param height the height of the canvas
   */
  public ChaosGame(ChaosGameDescription description, int width, int height) {
    this.description = description;
    this.canvas = new ChaosCanvas(width, height, description.getMinCoords(),
        description.getMaxCoords());
    this.currentPoint = new Vector2D(0, 0);
    this.random = new Random();
    this.pointStack = new Stack<>();
    pointStack.push(currentPoint);
  }

  /**
   * Constructor for ChaosGame.
   *
   * @param width the width of the canvas
   * @param height the height of the canvas
   */
  public ChaosGame(int width, int height) {
    this.canvas = new ChaosCanvas(width, height, new Vector2D(-2, -2), new Vector2D(2, 2));
    this.currentPoint = new Vector2D(0, 0);
    this.random = new Random();
    this.pointStack = new Stack<>();
    pointStack.push(currentPoint);
  }

  /**
   * Gets the canvas of the chaos game.
   *
   * @return the canvas of the chaos game
   */
  public ChaosCanvas getCanvas() {
    return canvas;
  }

  /**
   * Runs a specified number of steps in the Chaos Game. Each step a random
   *transformation is selected and applied
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

  /**
   * Runs a specified number of steps in the Barnsley Fern Chaos Game.
   * Each step a transformation is selected based on a probability distribution and applied to the current point,
   *
   * @param steps the number of steps to run the chaos game
   */
  public void runStepsForBarnsley(int steps){
    canvas.clear();
    for (int i = 0; i < steps; i++) {
      List<Double> probabilities = new ArrayList<>();
      probabilities.add(1.0);   // 1% for the first element
      probabilities.add(86.0);  // 85% for the second element (1% + 85%)
      probabilities.add(93.0);  // 7% for the third element (86% + 7%)
      probabilities.add(100.0); // 7% for the fourth element (93% + 7%)

      // Get a random value between 0 and 100
      double randomValue = 100 * random.nextDouble();


      int transformIndex = 0;
      for (int j = 0; j < probabilities.size(); j++) {
        if (randomValue < probabilities.get(j)) {
          transformIndex = j;
          break;
        }
      }

      List<Transform2D> transforms = description.getTransforms();
      Transform2D transform = transforms.get(transformIndex);
      currentPoint = transform.transform(currentPoint);
      canvas.putPixel(currentPoint);
    }
  }

  /**
   * Runs the chaos game with iterative transformation.
   * The chaos game is run by applying all transformations to the current point and plotting the result on the canvas.
   */
  public void fractalWithIterationTransformation() {
    canvas.clear();
    do {
      currentPoint = pointStack.pop();

      for (int j = 0; j < description.getTransforms().size(); j++) {
        Transform2D transform = description.getTransforms().get(j);
        Vector2D newPoint = transform.transform(currentPoint);

        if(canvas.checkIfCoordAsPixelIsOutsideCanvas(newPoint) || canvas.getPixel(newPoint) >= 20){
          continue;
        }

        pointStack.push(newPoint);
        canvas.putPixel(newPoint);
      }
    } while (!pointStack.isEmpty());
    pointStack.push(new Vector2D(0, 0));
  }
}
