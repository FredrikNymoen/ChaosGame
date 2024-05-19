package model.chaosGame;

import java.util.List;
import model.mathcore.Vector2D;
import model.transformations.Transform2D;

/**
 * Provides the configuration for a Chaos Game.
 * This includes the geometric transformations to be applied and the boundaries for plotting the results.
 * This description encapsulates
 * all necessary data to set up and run a specific instance of a Chaos Game.
 */
public class ChaosGameDescription {

  private Vector2D minCoords;
  private Vector2D maxCoords;
  private List<Transform2D> transforms;


  /**
   * Constructor for the model.chaosGame.ChaosGameDescription class. With specified boundary cords and a list of transformations.
   * @param transforms the list of transformations to be applied
   * @param minCoords the minimum coordinates of the canvas
   * @param maxCoords the maximum coordinates of the canvas
   */

  public ChaosGameDescription(List<Transform2D> transforms, Vector2D minCoords, Vector2D maxCoords) {
    this.minCoords = minCoords;
    this.maxCoords = maxCoords;
    this.transforms = transforms;
  }

  /**
   * Returns the list of transformations for the chaos game.
   *
   * @return List<Transform2D> the list of transformations for the chaos game
   */

  public List<Transform2D> getTransforms() {
    return transforms;
  }

    /**
     * Returns the minimum coordinates of the canvas.
     *
     * @return Vector2D the minimum coordinates of the canvas
     */
  public Vector2D getMinCoords() {
    return minCoords;
  }

    /**
     * Returns the maximum coordinates of the canvas.
     *
     * @return Vector2D the maximum coordinates of the canvas
     */
  public Vector2D getMaxCoords() {
    return maxCoords;
  }

}
