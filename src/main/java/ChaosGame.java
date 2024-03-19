import java.util.Random;
import mathcore.Vector2D;
import transformations.Transform2D;

/**
 * ChaosGame class is used to represent a chaos game where you can generate fractals or other complex structures
 * through repeated application of random transformations. It uses ChaosCanvas where the results of these
 * transformations are plotted.
 */
public class ChaosGame {
  private ChaosCanvas canvas;
  private ChaosGameDescription description;
  private Vector2D currentPoint;
  public Random random;

    /**
     * Constructor for the ChaosGame class. Constructs a new chaos game using a specified set of rules and dimensions
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
  }
  /**
   * Returns the canvas of the chaos game.
   *
   * @return ChaosCanvas the canvas of the chaos game
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
    for (int i = 0; i < steps; i++) {
      Transform2D transform = description.getTransforms().get(random.nextInt(description.getTransforms().size()));
      currentPoint = transform.transform(currentPoint);
      canvas.putPixel(currentPoint);
    }
  }

}
