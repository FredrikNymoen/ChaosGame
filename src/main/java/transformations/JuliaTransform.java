package transformations;

import mathcore.Complex;
import mathcore.Vector2D;

/**
 * transformations.JuliaTransform class is responsible for transforming a mathcore.Vector2D point on the form z -> ±sqrt(z - c).
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */

public class JuliaTransform implements Transform2D {
  private Complex point;
  private int sign;
  private int iterations;

  /**
   * Constructor for transformations.JuliaTransform.
   *
   * @param point the complex point
   * @param sign  determines the sign of the square root
   */
  public JuliaTransform(Complex point, int sign) {
    this.point = point;
    this.sign = (int) Math.signum(sign);
  }


  /**
   * Transforms a 2D vector on the form z -> ±sqrt(z - c).
   *
   * @param point the point to transform
   * @return the transformed point
   */
  @Override
  public Vector2D transform(Vector2D point) {
    Complex z = new Complex(point.getX0(), point.getX1());
    // Beregner z - c
    Complex result = z.subtract(this.point).sqrt().multiply(sign);
    //System.out.println(result.getX0() + " " + result.getX1());
    return new Vector2D(result.getX0(), result.getX1());
  }

  public Complex getPoint() {
    return point;
  }
}
