public class Complex extends Vector2D {

  public Complex(double realpart, double imaginarypart) {
    super(realpart, imaginarypart);
  }

  public Complex sqrt() {
    double magnitude = Math.sqrt(Math.pow(getX0(), 2) + Math.pow(getX1(), 2)); // Length of the vector
    double realPart = Math.sqrt((magnitude + getX0()) / 2); // Real part of result of square root
    double imaginaryPart = Math.signum(getX1()) * Math.sqrt((magnitude - getX0()) / 2); // Imaginary part of result of square root

    return new Complex(realPart, imaginaryPart);
  }

}
