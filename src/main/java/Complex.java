public class Complex extends Vector2D{

  public Complex(double realPart, double imaginaryPart) {
    super(realPart, imaginaryPart);
  }

  public Complex sqrt() {
    double realpart = Math.sqrt(getX0());
    double x1 = Math.sqrt(imaginaryPart);
    return new Complex(r, i);
  }

}
