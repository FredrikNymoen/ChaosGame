package model.chaosgame;

import java.util.List;
import model.mathcore.Vector2D;
import model.transformations.Transform2D;

/**
 * The ChaosGameDescription class is used to represent a description of a chaos game. The
 * description contains a list of transformations and the boundary coordinates of the canvas.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class ChaosGameDescription {

  private final Vector2D minCoords;
  private final Vector2D maxCoords;
  private final List<Transform2D> transforms;


  /**
   * Constructor for the ChaosGameDescription class. With specified boundary cords and a list of
   * transformations.
   *
   * @param transforms the list of transformations to be applied
   * @param minCoords  the minimum coordinates of the canvas
   * @param maxCoords  the maximum coordinates of the canvas
   */

  public ChaosGameDescription(List<Transform2D> transforms, Vector2D minCoords,
      Vector2D maxCoords) {
    this.minCoords = minCoords;
    this.maxCoords = maxCoords;
    this.transforms = transforms;
  }

  /**
   * Returns the list of transformations for the chaos game.
   *
   * @return the list of transformations for the chaos game
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
