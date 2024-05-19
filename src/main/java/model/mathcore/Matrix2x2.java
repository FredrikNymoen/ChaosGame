package model.mathcore;

/**
 * A 2x2 matrix class that can multiply a 2D vector with the matrix.
 *
 * @author Amund Larsen & Fredrik Nymoen
 *
 * @version v.1.0.0
 *
 */
public class Matrix2x2 {
  private double a00;
  private double a01;
  private double a10;
  private double a11;

  /**
   * Constructor for the Matrix2x2 class.
   *
   * @param a00 row one, column one in the matrix
   * @param a01 row one, column two in the matrix
   * @param a10 row two, column one in the matrix
   * @param a11 row two, column two in the matrix
   */
  public Matrix2x2(double a00, double a01, double a10, double a11) {
    this.a00 = a00;
    this.a01 = a01;
    this.a10 = a10;
    this.a11 = a11;
  }

  /**
   * Multiplies a 2D vector with the matrix. Then returns a new vector with the result.
   *
   * @param vector the vector to be multiplied with the matrix
   * @return Vector2D the result of the multiplication
   */
  public Vector2D multiply(Vector2D vector){
    double newX = a00 * vector.getX0() + a01 * vector.getX1();
    double newY = a10 * vector.getX0() + a11 * vector.getX1();
    return new Vector2D(newX, newY);
  }

  /**
   * Returns the value of a00.
   *
   * @return double the value of a00
   */
  public double geta00() {
    return a00;
  }

  /**
   * Returns the value of a01.
   *
   * @return double the value of a01
   */
  public double geta01() {
    return a01;
  }

  /**
   * Returns the value of a10.
   *
   * @return double the value of a10
   */
  public double geta10() {
    return a10;
  }

  /**
   * Returns the value of a11.
   *
   * @return double the value of a11
   */
  public double geta11() {
    return a11;
  }

}
