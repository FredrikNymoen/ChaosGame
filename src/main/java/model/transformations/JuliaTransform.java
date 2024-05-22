package model.transformations;

import java.util.List;
import model.mathcore.Complex;
import model.mathcore.Vector2D;

/**
 * model.transformations.JuliaTransform class is responsible for transforming a
 * Vector2D point on the form z -> ±sqrt(z - c).
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class JuliaTransform implements Transform2D {

  private final Complex point;
  private final int sign;

  /**
   * Constructor for JuliaTransform.
   *
   * @param point the complex point
   * @param sign  determines the sign of the square root
   */
  public JuliaTransform(Complex point, int sign) {
    this.point = point;
    this.sign = sign;
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

    // calculate z - c
    Complex diff = z.subtract(this.point);

    // get all the fourth roots of the difference
    List<Complex> roots = diff.getFourthRoots();

    // choose the root based on the sign
    int index = Math.abs(sign);
    Complex selectedRoot = roots.get(index);

    return new Vector2D(selectedRoot.getX0(), selectedRoot.getX1());
  }

  /**
   * Returns the complex point of the transformation.
   *
   * @return the complex point of the transformation.
   */
  public Complex getPoint() {
    return point;
  }

}
