package model.transformations;

import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;

/**
 * model.transformations.AffineTransform2D class is responsible for the affine transformation of a 2D vector on the form x -> Ax + b.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */

public class AffineTransform2D implements Transform2D {
  private Matrix2x2 matrix;
  private Vector2D vector;

  /**
   * Constructor for model.transformations.AffineTransform2D.
   *
   * @param matrix the matrix
   * @param vector the vector
   */
  public AffineTransform2D(Matrix2x2 matrix, Vector2D vector) {
    this.matrix = matrix;
    this.vector = vector;
  }

  /**
   * Transforms a 2D vector.
   *
   * @param point the point to transform
   * @return the transformed point
   */
  @Override
  public Vector2D transform(Vector2D point) {
    return matrix.multiply(point).add(vector);
  }

  public Matrix2x2 getMatrix() {
    return matrix;
  }

  public Vector2D getVector() {
    return vector;
  }
}
