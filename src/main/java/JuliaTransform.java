import java.util.Vector;

public class JuliaTransform extends Transform2D{
  private Complex point;
  private int sign;

  public JuliaTransform(Complex point, int sign) {
    this.point = point;
    this.sign = sign;
  }

  @Override
  public Vector2D transform(Vector2D point) {
    // Konverterer Vector2D til et komplekst tall z
    Vector2D c = new Vector2D(this.point.getRealPart(), this.point.getImaginaryPart());
    // Beregner z - c
    Vector2D zMinusC = point.subtract(this.point);
    // Beregner kvadratroten av det komplekse tallet, avhengig av sign
    Complex z = new Complex(zMinusC.getX0(), zMinusC.getX1()).sqrt();
    if (this.sign < 0) {
      // Hvis sign er negativ, bruk den andre kvadratroten
      z = new Complex(-z.getRealPart(), -z.getImaginaryPart());
    }
    // Returnerer resultatet som en Vector2D
    return new Vector2D(z.getRealPart(), z.getImaginaryPart());
  }

}
