package model.mathcore;

/**
 * A class representing a 2D vector.
 *
 * @author Amund Larsen & Fredrik Nymoen
 * @version v.1.0.0
 *
 */
public class Vector2D{

  private double x0;

  private double x1;

  /**
   * Constructor for the Vector2D class.
   *
   * @param x0 the x-coordinate of the vector
   * @param x1 the y-coordinate of the vector
   */

  public Vector2D(double x0, double x1) {
    this.x0 = x0;
    this.x1 = x1;
  }

  /**
   * Returns the x-coordinate of the vector.
   *
   * @return double the x-coordinate of the vector
   */
  public double getX0() {
    return x0;
  }

  /**
   * Returns the y-coordinate of the vector.
   *
   * @return double the y-coordinate of the vector
   */
  public double getX1() {
    return x1;
  }

  /**
   * Adds two vectors together and returns a new vector with the result.
   * @param other the vector to be added to the vector
   * @return model.mathcore.Vector2D which is a new vector with the result of the addition
   */
  public Vector2D add(Vector2D other) {
    return new Vector2D(x0 + other.x0, x1 + other.x1);
  }

  /**
   * Subtracts one vector from another and returns a new vector with the result.
   * @param other the vector to be subtracted from the vector
   * @return model.mathcore.Vector2D which is a new vector with the result of the subtraction
   */
  public Vector2D subtract(Vector2D other) {
    return new Vector2D(x0 - other.x0, x1 - other.x1);
  }

}
