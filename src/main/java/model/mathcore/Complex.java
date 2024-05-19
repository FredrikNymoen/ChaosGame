package model.mathcore;

import java.util.ArrayList;
import java.util.List;

/**
 * model.mathcore.Complex class is used to represent complex numbers and perform sqrt operation on them.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class Complex extends Vector2D {

  /**
   * Constructor for model.mathcore.Complex.
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

  public Complex multiply(int sign) {
    return new Complex(sign * getX0(), sign * getX1());
  }

  @Override
  public Complex subtract(Vector2D other) {
    return new Complex(getX0() - other.getX0(), getX1() - other.getX1());
  }

  public List<Complex> getFourthRoots() {
    List<Complex> roots = new ArrayList<>();

    double r = getModulus(); // Modulus of the complex number
    double theta = getArgument(); // Argument of the complex number

    double rootModulus = Math.pow(r, 1/4.0); // Modulus for the fourth roots

    for (int k = 0; k < 4; k++) {
      double rootArgument = (theta + 2 * Math.PI * k) / 4; // Angle for each root
      Complex root = polar(rootModulus, rootArgument); // Compute the complex number from polar coordinates
      roots.add(root);
    }

    return roots; // Return all four roots
  }

  private Complex polar(double rootModulus, double rootArgument) {
    double realPart = rootModulus * Math.cos(rootArgument); // Real part of the complex number
    double imaginaryPart = rootModulus * Math.sin(rootArgument); // Imaginary part of the complex number

    return new Complex(realPart, imaginaryPart);
  }

  private double getArgument() {
    return Math.atan2(getX1(), getX0());
  }

  private double getModulus() {
    return Math.sqrt(Math.pow(getX0(), 2) + Math.pow(getX1(), 2));
  }

}
