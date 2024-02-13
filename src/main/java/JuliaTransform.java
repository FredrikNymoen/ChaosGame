import java.util.Vector;
/**
 * JuliaTransform class is responsible for transforming a Vector2D point on the form z -> ±sqrt(z - c).
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */

public class JuliaTransform implements Transform2D{
  private Complex point;
  private int sign;

  /**
   * Constructor for JuliaTransform.
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
    // Beregner z - c
    Vector2D zMinusC = point.subtract(this.point);
    // Beregner kvadratroten av det komplekse tallet, avhengig av sign
    Complex z = (new Complex(zMinusC.getX0(), zMinusC.getX1())).sqrt();
    if (this.sign < 0) {
      // Hvis sign er negativ, bruk den andre kvadratroten
      z = new Complex(-z.getX0(), -z.getX1());
    }
    // Returnerer resultatet som en Vector2D
    return new Vector2D(z.getX0(), z.getX1());
  }
}
