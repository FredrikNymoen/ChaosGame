public class Vector2D{

  private double x0;

  private double x1;

  public Vector2D(double x0, double x1) {
    this.x0 = x0;
    this.x1 = x1;
  }

  public double getX0() {
    return x0;
  }

  public double getX1() {
    return x1;
  }

  public Vector2D add(Vector2D other) {
    return new Vector2D(x0 + other.x0, x1 + other.x1);
  }

  public Vector2D subtract(Vector2D other) {
    return new Vector2D(x0 - other.x0, x1 - other.x1);
  }
}
