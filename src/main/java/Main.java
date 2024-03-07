import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import mathcore.Complex;
import mathcore.Matrix2x2;
import mathcore.Vector2D;
import transformations.AffineTransform2D;
import transformations.JuliaTransform;
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
    List<Transform2D> affineTransforms = new ArrayList<>();
    affineTransforms.add(transformation1);
    affineTransforms.add(transformation2);
    affineTransforms.add(transformation3);
//last test of the day



    Complex transformationVector4 = new Complex(-0.74543, 0.11301);
    JuliaTransform transformation4 = new JuliaTransform(transformationVector4, 1);
    List<Transform2D> juliaTransforms = new ArrayList<>();
    juliaTransforms.add(transformation4);




    description = new ChaosGameDescription(affineTransforms, minCoordsVector, maxCoordsVector);
    fileHandler.writeToFile(description, "file.csv");

    description = fileHandler.readFromFile("file.csv");

    /*description = new ChaosGameDescription(juliaTransforms, minCoordsVector, maxCoordsVector);
    fileHandler.writeToFile(description, "file.csv");

    description = fileHandler.readFromFile("file.csv");*/


    ChaosGame game = new ChaosGame(description, 300,
        100);
    game.runSteps(150000);

    int[][] canvasArray = game.getCanvas().getCanvasArray();
    for (int i = 0; i < canvasArray.length; i++) {
      for (int j = 0; j < canvasArray[i].length; j++) {
        if (canvasArray[i][j] == 0) {
          System.out.print(" ");
        } else {
          System.out.print("■");
        }
      }
      System.out.println();
    }
  }

  public void gameLoop(){
    boolean gameActive = true;
    Scanner scanner = new Scanner(System.in);
    while(gameActive){
      System.out.println("How many iterations do you want: ");
      int iterations = scanner.nextInt();
      gameActive = false;
    }

  }
}
