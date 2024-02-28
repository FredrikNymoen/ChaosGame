import java.util.List;
import mathcore.Vector2D;
import transformations.Transform2D;

public class ChaosGameDescription {

  private Vector2D minCoords;
  private Vector2D maxCoords;
  private List<Transform2D> transforms;

  public ChaosGameDescription(List<Transform2D> transforms, Vector2D minCoords, Vector2D maxCoords) {
    this.minCoords = minCoords;
    this.maxCoords = maxCoords;
    this.transforms = transforms;
  }
  public ChaosGameDescription(List<Transform2D> transforms) {
    this.transforms = transforms;
  }
  public List<Transform2D> getTransforms() {
    return transforms;
  }
  public Vector2D getMinCoords() {
    return minCoords;
  }
  public Vector2D getMaxCoords() {
    return maxCoords;
  }
}
