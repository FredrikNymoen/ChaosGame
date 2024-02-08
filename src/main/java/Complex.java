public class Complex extends Vector2D {

  public Complex(double realpart, double imaginarypart) {
    super(realpart, imaginarypart);
  }

  public Complex sqrt() {
    double r = Math.sqrt(getX0());
    double i = Math.sqrt(getX1());
    return new Complex(r, i);
  }

}
