package model.filehandling;

import java.util.logging.Logger;
import model.chaosGame.ChaosGameDescription;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import model.mathcore.Complex;
import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import model.transformations.AffineTransform2D;
import model.transformations.JuliaTransform;
import model.transformations.Transform2D;

/**
 * A class that handles reading and writing to files for the model.chaosGame.ChaosGameDescription class.
 * This class provides methods to serialize the configuration of a Chaos Game into
 * file and to deserialize it back into an object.
 */
public class ChaosGameFileHandler {

  /**
   * Reads a model.chaosGame.ChaosGameDescription from a specified file. This method parses the file
   * assumed to be in a custom format that lists transformations and boundary coordinates
   * for the Chaos Game. The file can describe different types of transformations based
   * on its first line.
   *
   * @param path The path to the file containing the Chaos Game configuration.
   * @return A new model.chaosGame.ChaosGameDescription object initialized with the parameters read from the file.
   */

  public ChaosGameDescription readFromFile(String path) {
    File file = new File(path);
    ChaosGameDescription description = null;
    String[] transformationValues;
    List<Transform2D> transforms = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()))) {
      String line = reader.readLine();
      String typeOfTransformation;
      int commaIndex = line.indexOf(",");
      if (commaIndex != -1) {
        // If a comma is found, extract the substring before the comma
        typeOfTransformation = line.substring(0, commaIndex);
      } else {
        // If no comma is found, use the entire line
        typeOfTransformation = line;
      }

      if ("Affine2D".equals(typeOfTransformation)) {
        String[] minCoordsLine = reader.readLine().split(", ");
        String[] maxCoordsLine = reader.readLine().split(", ");

        // Parsing the values to their respective types
        double minX0 = Double.parseDouble(minCoordsLine[0]);
        double minX1 = Double.parseDouble(minCoordsLine[1]);
        double maxX0 = Double.parseDouble(maxCoordsLine[0]);
        double maxX1 = Double.parseDouble(maxCoordsLine[1]);
        Vector2D minCoordsVector = new Vector2D(minX0, minX1);
        Vector2D maxCoordsVector = new Vector2D(maxX0, maxX1);

        while ((line = reader.readLine()) != null) {
          transformationValues = line.split(", ");
          double a00 = Double.parseDouble(transformationValues[0]);
          double a01 = Double.parseDouble(transformationValues[1]);
          double a10 = Double.parseDouble(transformationValues[2]);
          double a11 = Double.parseDouble(transformationValues[3]);
          double b0 = Double.parseDouble(transformationValues[4]);
          double b1 = Double.parseDouble(transformationValues[5]);
          Matrix2x2 matrix = new Matrix2x2(a00, a01, a10, a11);
          Vector2D vector = new Vector2D(b0, b1);
          transforms.add(new AffineTransform2D(matrix, vector));
        }

        description = new ChaosGameDescription(transforms, minCoordsVector,
            maxCoordsVector);

      } else {
        String[] minCoordsLine = reader.readLine().split(", ");
        String[] maxCoordsLine = reader.readLine().split(", ");

        // Parsing the values to their respective types
        double minX0 = Double.parseDouble(minCoordsLine[0]);
        double minX1 = Double.parseDouble(minCoordsLine[1]);
        double maxX0 = Double.parseDouble(maxCoordsLine[0]);
        double maxX1 = Double.parseDouble(maxCoordsLine[1]);
        Vector2D minCoordsVector = new Vector2D(minX0, minX1);
        Vector2D maxCoordsVector = new Vector2D(maxX0, maxX1);

        line = reader.readLine();
        String[] pointValues = line.split(", ");
        Complex point = new Complex(
            Double.parseDouble(pointValues[0]),
            Double.parseDouble(pointValues[1])
        );
        transforms.add(new JuliaTransform(point, 1));
        description = new ChaosGameDescription(transforms,minCoordsVector,maxCoordsVector);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return description;
  }

  /**
   * Writes a given model.chaosGame.ChaosGameDescription to a specified file. This method serializes the
   * Chaos Game configuration into a custom format, allowing it to be read and reconstructed
   * later. The format includes information on transformations and boundary coordinates.
   *
   * @param description The model.chaosGame.ChaosGameDescription object to be written to the file.
   * @param path The path where the file will be created or overwritten.
   */

  public void writeToFile(ChaosGameDescription description, String path, String transformationtype) {
    File file = new File(path);

    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()))) {
      if (description.getTransforms().get(0) instanceof AffineTransform2D) {
        writer.write("Affine2D, " + transformationtype + "\n");
        writer.write(
            description.getMinCoords().getX0() + ", " + description.getMinCoords().getX1() + "\n");
        writer.write(
            description.getMaxCoords().getX0() + ", " + description.getMaxCoords().getX1() + "\n");

        for (Transform2D transformation : description.getTransforms()) {
          Matrix2x2 matrix = ((AffineTransform2D) transformation).getMatrix();
          writer.write(matrix.geta00() + ", " + matrix.geta01() + ", " + matrix.geta10() + ", "
              + matrix.geta11()
              + ", " + ((AffineTransform2D) transformation).getVector().getX0()
              + ", " + ((AffineTransform2D) transformation).getVector().getX1() + "\n");
        }
      } else{
        JuliaTransform transformation = (JuliaTransform) description.getTransforms().get(0);
        writer.write("Julia, " + transformationtype + "\n");
        writer.write(
            description.getMinCoords().getX0() + ", " + description.getMinCoords().getX1() + "\n");
        writer.write(
            description.getMaxCoords().getX0() + ", " + description.getMaxCoords().getX1() + "\n");
        writer.write(transformation.getPoint().getX0() + ", "
            + transformation.getPoint().getX1() + "\n");
        }
    } catch(Exception e){
      System.out.println(e.getMessage());
    }
  }


  public String readTransformationType(String path) {
    File file = new File(path);
    String transformationType = null;

    try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()))) {
      String line = reader.readLine();
      int commaIndex = line.indexOf(",");
      transformationType = line.substring(commaIndex + 2);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return transformationType;
  }


  public void writeLineToFile(String path, String line) {
    File file = new File(path);
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()))) {
      writer.write(line);
    } catch (Exception e) {

    }
  }

  public boolean checkForMandelbrot(String path) {
    boolean flag = false;
    File file = new File(path);
    String line = null;
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()))) {
      line = reader.readLine();
      if(line.equals("Mandelbrot")) {
        flag = true;
      }
    } catch (Exception e) {
    }
    return flag;
  }
}
