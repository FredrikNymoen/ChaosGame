package model.transformations;

import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;

/**
 * AffineTransform2D class is responsible for the affine transformation of a 2D vector on the form x
 * -> Ax + b.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class AffineTransform2D implements Transform2D {

  private final Matrix2x2 matrix;
  private final Vector2D vector;

  /**
   * Constructor for AffineTransform2D.
   *
   * @param matrix the matrix of the affine transformation
   * @param vector the vector of the affine transformation
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

  /**
   * Returns the matrix of the affine transformation.
   *
   * @return the matrix of the affine transformation
   */
  public Matrix2x2 getMatrix() {
    return matrix;
  }

  /**
   * Returns the vector of the affine transformation.
   *
   * @return the vector of the affine transformation
   */
  public Vector2D getVector() {
    return vector;
  }
}
