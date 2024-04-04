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
 * The main application for the Chaos Game.
 * It allows the user to select between different
 * fractal transformations such as Affine transformations and Julia sets, configure parameters, and then visualize the results.
 * The chaos game is a method of creating fractal structures by iteratively applying a set of affine transformations.
 * This implementation supports generating fractals such as the Sierpinski triangle, the Barnsley fern, and Julia sets.
 */

public class Main {

  ChaosGameFileHandler fileHandler = new ChaosGameFileHandler();
  /**
   * The main method of the application. It initializes the main class and starts the game loop.
   * @param args the command line arguments
   */

  public static void main(String[] args) {


    /*ChaosGameDescription description;
    ChaosGameFileHandler fileHandler = new ChaosGameFileHandler();

    //barnsley
    Vector2D minCoordsVector = new Vector2D(-2, -2);
    Vector2D maxCoordsVector = new Vector2D(10, 10);*/

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

    /*Matrix2x2 transformationMatrix1 = new Matrix2x2(0, 0, 0, 0.16);
    Matrix2x2 transformationMatrix2 = new Matrix2x2(0.85, 0.04, -0.04, 0.85);
    Matrix2x2 transformationMatrix3 = new Matrix2x2(0.2, -0.26, 0.23, 0.22);
    Matrix2x2 transformationMatrix4 = new Matrix2x2(-0.15, 0.28, 0.26, 0.24);
    Vector2D transformationVector1 = new Vector2D(0, 0);
    Vector2D transformationVector2 = new Vector2D(0, 1.6);
    Vector2D transformationVector3 = new Vector2D(0, 0.44);

    AffineTransform2D transformation1 = new AffineTransform2D(transformationMatrix1,
        transformationVector1);
    AffineTransform2D transformation2 = new AffineTransform2D(transformationMatrix2,
        transformationVector2);
    AffineTransform2D transformation3 = new AffineTransform2D(transformationMatrix3,
        transformationVector2);
    AffineTransform2D transformation4 = new AffineTransform2D(transformationMatrix4,
        transformationVector3);
    List<Transform2D> affineTransforms = new ArrayList<>();
    affineTransforms.add(transformation1);
    affineTransforms.add(transformation2);
    affineTransforms.add(transformation3);
    affineTransforms.add(transformation4);*/

//last test of the day

    //Complex transformationVector4 = new Complex(-0.74543, 0.11301);
    /*Complex transformationVector4 = new Complex(0.285, 0.01);
    JuliaTransform transformation4 = new JuliaTransform(transformationVector4, 1);
    List<Transform2D> juliaTransforms = new ArrayList<>();
    juliaTransforms.add(transformation4);*/

    /*description = new ChaosGameDescription(affineTransforms, minCoordsVector, maxCoordsVector);
    fileHandler.writeToFile(description, "file.csv");

    description = fileHandler.readFromFile("file.csv");*/

    /*description = new ChaosGameDescription(juliaTransforms, minCoordsVector, maxCoordsVector);
    fileHandler.writeToFile(description, "file.csv");

    description = fileHandler.readFromFile("file.csv");*/

    /*ChaosGame game = new ChaosGame(description, 250,
        100);
    game.runSteps(10000000);

    game.display();*/
    Main main = new Main();
    main.gameLoop();
  }

  /**
   * Interacts with the user to determine the type of chaos game to play.
   * The user is asked to select between Affine transformations and Julia sets.
   * The user is then asked to select between the Sierpinski triangle and the Barnsley fern for Affine transformations.
   * Based on the user's input, the method returns a ChaosGameDescription object that contains the necessary data to run the chaos game.
   * @return ChaosGameDescription the description of the chaos game
   */

  public ChaosGameDescription gameQuestions() {
    String transformationChoices = "";
    Scanner scanner = new Scanner(System.in);
    System.out.println("Wich transformation do you want?:");
    System.out.println("Type [a] : Affine");
    System.out.println("Type [j] : Julia");

    switch (scanner.nextLine()) {
      case "a":
        transformationChoices += "a";
        System.out.println("Which affine transformation do you want?:");
        System.out.println("Type [s] : Sierpinski triangle");
        System.out.println("Type [b] : Barnsley fern");

        String transformationChoice = scanner.nextLine(); // Get the user's choice for transformation
        if (transformationChoice.equals("s") || transformationChoice.equals("b")) {
          transformationChoices += "-" + transformationChoice;
        } else {
          System.out.println("Invalid input");
        }
        break;

      case "j":
        transformationChoices += "j";
        break;

      default:
        System.out.println("Invalid input");
        break;
    }
    String[] parts = transformationChoices.split("-");
    if (parts[0].equals("a")){
      if (parts[1].equals("s")){
        Vector2D minCoordsVector = new Vector2D(0, 0);
        Vector2D maxCoordsVector = new Vector2D(1, 1);
        Matrix2x2 transformationMatrix1 = new Matrix2x2(0.5, 0, 0, 0.5);
        Vector2D transformationVector1 = new Vector2D(0, 0);
        Vector2D transformationVector2 = new Vector2D(0.25, 0.5);
        Vector2D transformationVector3 = new Vector2D(0.5, 0);

        AffineTransform2D transformation1 = new AffineTransform2D(transformationMatrix1, transformationVector1);
        AffineTransform2D transformation2 = new AffineTransform2D(transformationMatrix1, transformationVector2);
        AffineTransform2D transformation3 = new AffineTransform2D(transformationMatrix1, transformationVector3);
        List<Transform2D> affineTransforms = new ArrayList<>();
        affineTransforms.add(transformation1);
        affineTransforms.add(transformation2);
        affineTransforms.add(transformation3);
        return new ChaosGameDescription(affineTransforms, minCoordsVector, maxCoordsVector);
      }
      else if (parts[1].equals("b")){
        Vector2D minCoordsVector = new Vector2D(-2, -2);
        Vector2D maxCoordsVector = new Vector2D(10, 10);
        Matrix2x2 transformationMatrix1 = new Matrix2x2(0, 0, 0, 0.16);
        Matrix2x2 transformationMatrix2 = new Matrix2x2(0.85, 0.04, -0.04, 0.85);
        Matrix2x2 transformationMatrix3 = new Matrix2x2(0.2, -0.26, 0.23, 0.22);
        Matrix2x2 transformationMatrix4 = new Matrix2x2(-0.15, 0.28, 0.26, 0.24);
        Vector2D transformationVector1 = new Vector2D(0, 0);
        Vector2D transformationVector2 = new Vector2D(0, 1.6);
        Vector2D transformationVector3 = new Vector2D(0, 0.44);

        AffineTransform2D transformation1 = new AffineTransform2D(transformationMatrix1,
            transformationVector1);
        AffineTransform2D transformation2 = new AffineTransform2D(transformationMatrix2,
            transformationVector2);
        AffineTransform2D transformation3 = new AffineTransform2D(transformationMatrix3,
            transformationVector2);
        AffineTransform2D transformation4 = new AffineTransform2D(transformationMatrix4,
            transformationVector3);
        List<Transform2D> affineTransforms = new ArrayList<>();
        affineTransforms.add(transformation1);
        affineTransforms.add(transformation2);
        affineTransforms.add(transformation3);
        affineTransforms.add(transformation4);
        return new ChaosGameDescription(affineTransforms, minCoordsVector, maxCoordsVector);
      }
    }
    else if (parts[0].equals("j")){
      Vector2D minCoordsVector = new Vector2D(-1.6, -1);
      Vector2D maxCoordsVector = new Vector2D(1.6, 1);
      Complex transformationVector4 = new Complex(0.285, 0.01);
      JuliaTransform transformation4 = new JuliaTransform(transformationVector4, 1);
      List<Transform2D> juliaTransforms = new ArrayList<>();
      juliaTransforms.add(transformation4);
      return new ChaosGameDescription(juliaTransforms, minCoordsVector, maxCoordsVector);
    }
    return null;
  }

  /**
   * Contains the main loop of the game. It repeatedly asks the user for the type of fractal, its configuration, and the
   * number of iterations to run. After each simulation, it asks the user whether they want to play again.
   */

  public void gameLoop() {
    boolean gameRunning = true;
    while (gameRunning) {
      ChaosGameDescription description = gameQuestions();
      fileHandler.writeToFile(description, "file.csv");
      description = fileHandler.readFromFile("file.csv");
      ChaosGame game = new ChaosGame(description, 250, 100);
      int iterations = askForIterations();
      game.runSteps(iterations);
      game.display();

      System.out.println("Do you want to play again? [y/n]");
      Scanner scanner = new Scanner(System.in);
      String answer = scanner.nextLine();
      if (answer.equals("n")){
        gameRunning = false;
      }
    }
  }

  /**
   * Asks the user for the number of iterations to run the chaos game. It reads an integer value from the user.
   * @return The number of iterations for the chaos game as entered by the user.
   */
  private int askForIterations() {
    System.out.println("How many iterations do you want: ");
    Scanner scanner = new Scanner(System.in);
    return scanner.nextInt();
  }
}
