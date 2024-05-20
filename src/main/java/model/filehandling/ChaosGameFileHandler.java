package model.filehandling;

import exception.FileEmptyException;
import java.io.IOException;
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
 * The ChaosGameFileHandler class is used to read and write Chaos Game configurations to and from files.
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class ChaosGameFileHandler {
  private final String fileName;

  /**
   * Constructor for ChaosGameFileHandler.
   * @param fileName the name of the file to be read from or written to
   */
  public ChaosGameFileHandler(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Reads a Chaos Game configuration from a specified file. This method deserializes the
   * Chaos Game configuration from a custom format, allowing it to be reconstructed as a
   * ChaosGameDescription object.
   * @throws Exception if the file is not found, empty, or if an error occurs while reading the file
   * @return a ChaosGameDescription object representing the Chaos Game configuration
   */
  public ChaosGameDescription readFromFile() throws Exception{
    File file = new File(fileName);
    ChaosGameDescription description = null;
    String[] transformationValues;
    List<Transform2D> transforms = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()))) {
      String line = reader.readLine();
      if (line == null) {
        throw new FileEmptyException("Chaos game file is empty.");
      }

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
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (FileEmptyException e) {
      throw new FileEmptyException("File is empty.");
    } catch (Exception e) {
      throw new Exception("Error reading file.");
    }


    return description;
  }


  /**
   * Writes a Chaos Game configuration to a specified file.
   * @param description the ChaosGameDescription object to be written to the file.
   * @param transformationtype the type of transformation.
   * @throws Exception if the file is not found or an error occurs while writing to the file.
   */
  public void writeToFile(ChaosGameDescription description, String transformationtype) throws Exception{
    File file = new File(fileName);

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
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (Exception e) {
      throw new Exception("Error writing to file.");
    }
  }

  /**
   * Reads the transformation type from a specified file.
   * @return the transformation type
   * @throws Exception if the file is not found or an error occurs while reading the file.
   */
  public String readTransformationType() throws Exception{
    File file = new File(fileName);
    String transformationType = null;

    try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()))) {
      String line = reader.readLine();
      int commaIndex = line.indexOf(",");
      transformationType = line.substring(commaIndex + 2);
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (Exception e) {
      throw new Exception("Error reading file.");
    }

    return transformationType;
  }

  /**
   * Writes a line to a specified file.
   * @param line the line to be written to the file
   * @throws Exception if the file is not found or an error occurs while writing to the file.
   */
  public void writeLineToFile(String line) throws Exception{
    File file = new File(fileName);
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()))) {
      writer.write(line);
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (Exception e) {
      throw new Exception("Error writing to file.");
    }
  }

  /**
   * Checks if the file contains a Mandelbrot configuration.
   * @return true if the file contains a Mandelbrot configuration, false otherwise
   * @throws Exception if the file is not found or an error occurs while reading the file.
   */
  public boolean checkForMandelbrot() throws Exception{
    boolean flag = false;
    File file = new File(fileName);
    String line = null;
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()))) {
      line = reader.readLine();
      if(line.equals("Mandelbrot")) {
        flag = true;
      }
    }
    catch (IOException e) {
      throw new IOException("File not found.");
    } catch (Exception e) {
      throw new Exception("Error reading file.");
    }

    return flag;
  }
}
