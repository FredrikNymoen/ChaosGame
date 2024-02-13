public class AffineTransform2D implements Transform2D{
  private Matrix2x2 matrix;
  private Vector2D vector;

  public AffineTransform2D(Matrix2x2 matrix, Vector2D vector) {
    this.matrix = matrix;
    this.vector = vector;
  }

  @Override
  public Vector2D transform(Vector2D point) {
    return matrix.multiply(point).add(vector);
  }
}
