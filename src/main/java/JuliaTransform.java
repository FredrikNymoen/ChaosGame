public class JuliaTransform extends Transform2D{
  private Complex point;
  private int sign;

  public JuliaTransform(Complex point, int sign) {
    this.point = point;
    this.sign = sign;
  }

  @Override
  public Vector2D transform(Vector2D point) {
    // Her må man konvertere Vector2D til Complex for å utføre komplekse operasjoner,
    // og deretter konvertere tilbake hvis nødvendig.
    // Detaljene for dette vil avhenge av hvordan du velger å løse dette.
    // Dette er et konseptuelt eksempel.
    Complex z = new Complex(point.getX0(), point.getX1());
    Complex sqrtZ = z.sqrt(); // Anta at dette returnerer en av de komplekse kvadratrøttene basert på sign
    return new Vector2D(sqrtZ.getRealPart() - this.point.getRealPart(),
        sqrtZ.getImaginaryPart() - this.point.getImaginaryPart());
  }

}
