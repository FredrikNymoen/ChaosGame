import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import mathcore.Complex;
import mathcore.Matrix2x2;
import mathcore.Vector2D;
import transformations.AffineTransform2D;
import transformations.JuliaTransform;
import transformations.Transform2D;

/**
 * Main entry point for the Chaos Game application.
 * This class sets up and executes the Chaos Game, allowing for different configurations
 * based on affine and Julia transformations. It illustrates how fractal patterns can emerge
 * from simple rules applied repeatedly. The main method sets up the game, runs it for a
 * specified number of iterations, and prints the resulting pattern to the console.
 */
public class Main {

  /**
   * Sets up and executes the Chaos Game. This main method allows for running different
   * configurations of the game, based on the provided transformations. It demonstrates
   * the creation of fractal patterns using both affine transformations and Julia sets.
   * The process involves creating a game description, running the game, and then printing
   * the final canvas to the console. Additionally, it includes an example of reading and
   * writing the game configuration to and from a file.
   *
   * @param args Command line arguments, not used in this application.
   */

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



    Complex transformationVector4 = new Complex(-0.74543, 0.11301);
    JuliaTransform transformation4 = new JuliaTransform(transformationVector4, 1);
    List<Transform2D> juliaTransforms = new ArrayList<>();
    juliaTransforms.add(transformation4);




    /*description = new ChaosGameDescription(affineTransforms, minCoordsVector, maxCoordsVector);
    fileHandler.writeToFile(description, "file.csv");

    description = fileHandler.readFromFile("file.csv");*/

    description = new ChaosGameDescription(juliaTransforms, minCoordsVector, maxCoordsVector);
    fileHandler.writeToFile(description, "file.csv");

    description = fileHandler.readFromFile("file.csv");


    ChaosGame game = new ChaosGame(description, 150,
        50);
    game.runSteps(15000);

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

  /**
   * Provides a loop for the Chaos Game to continually ask the user for the number of
   * iterations to run. The loop continues until the user decides to stop the game.
   * This method demonstrates how to create an interactive loop for running the Chaos Game.
   */

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
