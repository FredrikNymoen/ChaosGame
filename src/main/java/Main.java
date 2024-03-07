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

    //barnsley
    Vector2D minCoordsVector = new Vector2D(-2, -2);
    Vector2D maxCoordsVector = new Vector2D(10, 10);

    //affine
    /*Vector2D minCoordsVector = new Vector2D(0, 0);
    Vector2D maxCoordsVector = new Vector2D(1, 1);*/

    //julia
    /*Vector2D minCoordsVector = new Vector2D(-1.6, -1);
    Vector2D maxCoordsVector = new Vector2D(1.6, 1);*/

    //sierpinski
    /*Matrix2x2 transformationMatrix = new Matrix2x2(0.5, 0, 0, 0.5);
    Vector2D transformationVector1 = new Vector2D(0, 0);
    Vector2D transformationVector2 = new Vector2D(0.25, 0.5);
    Vector2D transformationVector3 = new Vector2D(0.5, 0);

    AffineTransform2D transformation1 = new AffineTransform2D(transformationMatrix, transformationVector1);
    AffineTransform2D transformation2 = new AffineTransform2D(transformationMatrix, transformationVector2);
    AffineTransform2D transformation3 = new AffineTransform2D(transformationMatrix, transformationVector3);
    List<Transform2D> affineTransforms = new ArrayList<>();
    affineTransforms.add(transformation1);
    affineTransforms.add(transformation2);
    affineTransforms.add(transformation3);*/

    Matrix2x2 transformationMatrix1 = new Matrix2x2(0, 0, 0, 0.16);
    Matrix2x2 transformationMatrix2 = new Matrix2x2(0.85, 0.04, -0.04, 0.85);
    Matrix2x2 transformationMatrix3 = new Matrix2x2(0.2, -0.26, 0.23, 0.22);
    Matrix2x2 transformationMatrix4 = new Matrix2x2(-0.15, 0.28, 0.26, 0.24);
    Vector2D transformationVector1 = new Vector2D(0, 0);
    Vector2D transformationVector2 = new Vector2D(0, 1.6);
    Vector2D transformationVector3 = new Vector2D(0, 0.44);

    AffineTransform2D transformation1 = new AffineTransform2D(transformationMatrix1, transformationVector1);
    AffineTransform2D transformation2 = new AffineTransform2D(transformationMatrix2, transformationVector2);
    AffineTransform2D transformation3 = new AffineTransform2D(transformationMatrix3, transformationVector2);
    AffineTransform2D transformation4 = new AffineTransform2D(transformationMatrix4, transformationVector3);
    List<Transform2D> affineTransforms = new ArrayList<>();
    affineTransforms.add(transformation1);
    affineTransforms.add(transformation2);
    affineTransforms.add(transformation3);
    affineTransforms.add(transformation4);



//last test of the day


    //Complex transformationVector4 = new Complex(-0.74543, 0.11301);
    /*Complex transformationVector4 = new Complex(0.285, 0.01);
    JuliaTransform transformation4 = new JuliaTransform(transformationVector4, 1);
    List<Transform2D> juliaTransforms = new ArrayList<>();
    juliaTransforms.add(transformation4);*/


    description = new ChaosGameDescription(affineTransforms, minCoordsVector, maxCoordsVector);
    fileHandler.writeToFile(description, "file.csv");

    description = fileHandler.readFromFile("file.csv");

    /*description = new ChaosGameDescription(juliaTransforms, minCoordsVector, maxCoordsVector);
    fileHandler.writeToFile(description, "file.csv");

    description = fileHandler.readFromFile("file.csv");*/


    ChaosGame game = new ChaosGame(description, 250,
        100);
    game.runSteps(10000000);

    game.display();
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
