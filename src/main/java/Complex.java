public class Complex extends Vector2D{
  private double realPart;
  private double imaginaryPart;

  public Complex(double realPart, double imaginaryPart) {
    this.realPart = realPart;
    this.imaginaryPart = imaginaryPart;
  }

  public Complex sqrt() {
    double r = Math.sqrt(realPart);
    double i = Math.sqrt(imaginaryPart);
    return new Complex(r, i);
  }

  public double getRealPart() {
    return realPart;
  }
  public double getImaginaryPart() {
    return imaginaryPart;
  }
}
