package model.transformations;

import java.util.List;
import model.mathcore.Complex;
import model.mathcore.Vector2D;

/**
 * model.transformations.JuliaTransform class is responsible for transforming a
 * model.mathcore.Vector2D point on the form z -> ±sqrt(z - c).
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

    // Beregn z - c
    Complex diff = z.subtract(this.point);

    // Få alle fire fjerderøtter
    List<Complex> roots = diff.getFourthRoots();

    // Velg en rot basert på et kriterium
    int index = Math.abs(sign); // Sign bestemme hvilken rot som velges

    Complex selectedRoot = roots.get(index); // Velg rot basert på indeksen

    // Returner Vector2D som representerer den valgte roten
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
