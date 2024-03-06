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
      int transformIndex = random.nextInt(description.getTransforms().size());
      Transform2D transform = description.getTransforms().get(transformIndex);
      currentPoint = transform.transform(currentPoint);
      canvas.putPixel(currentPoint);
    }
  }

  public void display(){
    /*int[][] canvasArray = getCanvas().getCanvasArray();
    for (int i = 0; i < canvasArray.length; i++) {
      for (int j = 0; j < canvasArray[i].length; j++) {
        if (getCanvas().getPixel(currentPoint) == 0) {
          System.out.print(" ");
        } else {
          //System.out.print("■");
          System.out.print("X");
        }
      }
      System.out.println();
    }*/

    int[][] canvasArray = getCanvas().getCanvasArray();
    for (int i = 0; i < canvasArray.length; i++) {
      for (int j = 0; j < canvasArray[i].length; j++) {
        if (canvasArray[i][j] == 0) {
          System.out.print(" ");
        } else {
          //System.out.print("■");
          System.out.print("X");
        }
      }
      System.out.println();
    }
  }

}
