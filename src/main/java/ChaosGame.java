import java.util.Random;
import mathcore.Vector2D;
import transformations.Transform2D;

public class ChaosGame {
  private ChaosCanvas canvas;
  private ChaosGameDescription description;
  private Vector2D currentPoint;
  public Random random;

  public ChaosGame(ChaosGameDescription description, int width, int height) {
    this.description = description;
    this.canvas = new ChaosCanvas(width, height, description.getMinCoords(), description.getMaxCoords());
    this.currentPoint = new Vector2D(0, 0);
    this.random = new Random();
  }
  public ChaosCanvas getCanvas() {
    return canvas;
  }

  public void runSteps(int steps) {
    for (int i = 0; i < steps; i++) {
      Transform2D transform = description.getTransforms().get(random.nextInt(description.getTransforms().size()));
      currentPoint = transform.transform(currentPoint);
      canvas.putPixel(currentPoint);
    }
  }

}
