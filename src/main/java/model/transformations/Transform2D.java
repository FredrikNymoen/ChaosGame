package model.transformations;

import model.mathcore.Vector2D;

/**
 * An interface for 2D transformations.
 *
 * @author Amund Larsen & Fredrik Nymoen
 *
 * @version v.1.0.0
 *
 */
public interface Transform2D {

  /**
   * Transform a point in the plane.
   *
   * @param point The point to be transformed.
   * @return The transformed point.
   */
  Vector2D transform(Vector2D point);
}
