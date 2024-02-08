public class JuliaTransform extends Transform2D{
  private Complex point;
  private int sign;

  public JuliaTransform(Complex point, int sign) {
    this.point = point;
    this.sign = sign;
  }

  @Override
  public Vector2D transform(Vector2D point) {
    Complex complex = new Complex(point.getX0(), point.getX1());
    Complex result = complex;
    for (int i = 0; i < 100; i++) {
      result = result.multiply(result).add(this.point);
    }
    return new Vector2D(result.getRealPart(), result.getImaginaryPart());
  }

}
