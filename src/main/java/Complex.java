
/**
 * Complex class is used to represent complex numbers and perform sqrt operation on them.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class Complex extends Vector2D {

  /**
   * Constructor for Complex.
   *
   * @param realpart       the real part of the complex number
   * @param imaginarypart  the imaginary part of the complex number
   */
  public Complex(double realpart, double imaginarypart) {
    super(realpart, imaginarypart);
  }

  /**
   * Returns the square root of the complex number.
   *
   * @return the square root of the complex number
   */
  public Complex sqrt() {
    double magnitude = Math.sqrt(Math.pow(getX0(), 2) + Math.pow(getX1(), 2)); // Length of the vector
    double realPart = Math.sqrt((magnitude + getX0()) / 2); // Real part of result of square root
    double imaginaryPart = Math.signum(getX1()) * Math.sqrt((magnitude - getX0()) / 2); // Imaginary part of result of square root

    return new Complex(realPart, imaginaryPart);
  }

}
