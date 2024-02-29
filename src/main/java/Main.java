import java.util.ArrayList;
import java.util.List;
import mathcore.Matrix2x2;
import mathcore.Vector2D;
import transformations.AffineTransform2D;
import transformations.Transform2D;

public class Main {

  public static void main(String[] args) {

    ChaosGameDescription description;
    ChaosGameFileHandler fileHandler = new ChaosGameFileHandler();

    Vector2D minCoordsVector = new Vector2D(0, 0);
    Vector2D maxCoordsVector = new Vector2D(1, 1);

    Matrix2x2 transformationMatrix = new Matrix2x2(0.5, 0, 0, 0.5);
    Vector2D transformationVector1 = new Vector2D(0, 0);
    Vector2D transformationVector2 = new Vector2D(0.25, 0.5);
    Vector2D transformationVector3 = new Vector2D(0.5, 0);

    AffineTransform2D transformation1 = new AffineTransform2D(transformationMatrix, transformationVector1);
    AffineTransform2D transformation2 = new AffineTransform2D(transformationMatrix, transformationVector2);
    AffineTransform2D transformation3 = new AffineTransform2D(transformationMatrix, transformationVector3);
    List<Transform2D> transforms = new ArrayList<>();
    transforms.add(transformation1);
    transforms.add(transformation2);
    transforms.add(transformation3);

    description = new ChaosGameDescription(transforms, minCoordsVector, maxCoordsVector);
    fileHandler.writeToFile(description, "file.csv");

    description = fileHandler.readFromFile("file.csv");

    ChaosGame game = new ChaosGame(description, 160, 10);
    game.runSteps(15000);
    int[][] canvasArray = game.getCanvas().getCanvasArray();
    for (int i = 0; i < canvasArray.length; i++) {
      for (int j = 0; j < canvasArray[i].length; j++) {
        if (canvasArray[i][j] == 0) {
          System.out.print(" ");
        } else {
          System.out.print("X");
        }
      }
      System.out.println();
    }
  }
}
