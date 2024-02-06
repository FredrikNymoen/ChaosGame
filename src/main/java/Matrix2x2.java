public class Matrix2x2 {
  private double a00;
  private double a01;
  private double a10;
  private double a11;

  private Matrix2x2(double a00, double a01, double a10, double a11) {
    this.a00 = a00;
    this.a01 = a01;
    this.a10 = a10;
    this.a11 = a11;
  }
  private Vector2D multiply(Vector2D vector){
    double newX = a00 * vector.getX0() + a01 * vector.getX1();
    double newY = a10 * vector.getX0() + a11 * vector.getX1();
    return new Vector2D(newX, newY);
  }
}
