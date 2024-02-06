public class Complex extends Vector2D{
  private double realPart;
  private double imaginaryPart;

  public Complex(double realPart, double imaginaryPart) {
    this.realPart = realPart;
    this.imaginaryPart = imaginaryPart;
  }

  public Complex sqrt() {
    double radius = Math.hypot(realPart, imaginaryPart);
    double theta = Math.atan2(imaginaryPart, realPart) / 2.0;

    double sqrtRadius = Math.sqrt(radius);
    double real = sqrtRadius * Math.cos(theta);
    double imaginary = sqrtRadius * Math.sin(theta);

    return new Complex(real, imaginary);
  }
}
