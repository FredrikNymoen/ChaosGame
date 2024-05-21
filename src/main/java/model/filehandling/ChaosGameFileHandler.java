package model.filehandling;

import exception.FileEmptyException;
import exception.UnexpectedException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import model.chaosgame.ChaosGameDescription;
import model.mathcore.Complex;
import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import model.transformations.AffineTransform2D;
import model.transformations.JuliaTransform;
import model.transformations.Transform2D;

/**
 * The ChaosGameFileHandler class is used to read and write Chaos Game configurations to and from
 * files.
 * author Fredrik Nymoen & Amund Larsen
 * version v1.0.0
 */
public class ChaosGameFileHandler {

  private static final String FILE_NOT_FOUND_MESSAGE = "File not found.";
  private static final String FILE_EMPTY_MESSAGE = "Chaos game file is empty.";
  private static final String ERROR_READING_FILE_MESSAGE = "Error reading file.";
  private static final String ERROR_WRITING_FILE_MESSAGE = "Error writing to file.";
  private static final String AFFINE2D = "Affine2D";
  private static final String JULIA = "Julia";
  private static final String MANDELBROT = "Mandelbrot";

  private final String fileName;

  /**
   * Constructor for ChaosGameFileHandler.
   *
   * @param fileName the name of the file to be read from or written to
   */
  public ChaosGameFileHandler(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Reads a Chaos Game configuration from a specified file. This method deserializes the Chaos Game
   * configuration from a custom format, allowing it to be reconstructed as a ChaosGameDescription
   * object.
   *
   * @return a ChaosGameDescription object representing the Chaos Game configuration
   * @throws IOException if an I/O error occurs while reading the file
   * @throws FileEmptyException if the file is empty
   */
  public ChaosGameDescription readFromFile() throws IOException, UnexpectedException, FileEmptyException {
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(new File(fileName).getAbsolutePath()))) {
      String line = reader.readLine();
      if (line == null) {
        throw new FileEmptyException(FILE_EMPTY_MESSAGE);
      }

      String typeOfTransformation = getTypeOfTransformation(line);
      Vector2D minCoordsVector = parseVector2D(reader.readLine());
      Vector2D maxCoordsVector = parseVector2D(reader.readLine());

      List<Transform2D> transforms = new ArrayList<>();
      if (AFFINE2D.equals(typeOfTransformation)) {
        parseAffine2DTransforms(reader, transforms);
        return new ChaosGameDescription(transforms, minCoordsVector, maxCoordsVector);
      } else if (JULIA.equals(typeOfTransformation)) {
        parseJuliaTransform(reader, transforms);
        return new ChaosGameDescription(transforms, minCoordsVector, maxCoordsVector);
      } else {
        throw new UnexpectedException("Unsupported transformation type: " + typeOfTransformation);
      }
    } catch (IOException e) {
      throw new IOException(FILE_NOT_FOUND_MESSAGE, e);
    } catch (FileEmptyException e) {
      throw new FileEmptyException(FILE_EMPTY_MESSAGE);
    } catch (Exception e) {
      throw new UnexpectedException(ERROR_READING_FILE_MESSAGE);
    }
  }

  private void parseAffine2DTransforms(BufferedReader reader, List<Transform2D> transforms) throws IOException {
    String line;
    while ((line = reader.readLine()) != null) {
      String[] transformationValues = line.split(", ");
      Matrix2x2 matrix = new Matrix2x2(
          Double.parseDouble(transformationValues[0]),
          Double.parseDouble(transformationValues[1]),
          Double.parseDouble(transformationValues[2]),
          Double.parseDouble(transformationValues[3])
      );
      Vector2D vector = new Vector2D(
          Double.parseDouble(transformationValues[4]),
          Double.parseDouble(transformationValues[5])
      );
      transforms.add(new AffineTransform2D(matrix, vector));
    }
  }

  private void parseJuliaTransform(BufferedReader reader, List<Transform2D> transforms) throws IOException {
    String line = reader.readLine();
    String[] pointValues = line.split(", ");
    Complex point = new Complex(
        Double.parseDouble(pointValues[0]),
        Double.parseDouble(pointValues[1])
    );
    transforms.add(new JuliaTransform(point, 1));
  }

  private String getTypeOfTransformation(String line) {
    int commaIndex = line.indexOf(",");
    return (commaIndex != -1) ? line.substring(0, commaIndex) : line;
  }

  private Vector2D parseVector2D(String coordsLine) {
    String[] coords = coordsLine.split(", ");
    return new Vector2D(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
  }

  /**
   * Writes a Chaos Game configuration to a specified file.
   *
   * @param description        the ChaosGameDescription object to be written to the file.
   * @param transformationType the type of transformation.
   * @throws IOException if an I/O error occurs while writing to the file
   */
  public void writeToFile(ChaosGameDescription description, String transformationType)
      throws IOException, UnexpectedException {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(new File(fileName).getAbsolutePath()))) {
      if (description.getTransforms().get(0) instanceof AffineTransform2D) {
        writeAffine2DConfiguration(writer, description, transformationType);
      } else if (description.getTransforms().get(0) instanceof JuliaTransform) {
        writeJuliaConfiguration(writer, description, transformationType);
      }
    } catch (IOException e) {
      throw new IOException(FILE_NOT_FOUND_MESSAGE, e);
    } catch (Exception e) {
      throw new UnexpectedException(ERROR_WRITING_FILE_MESSAGE);
    }
  }

  private void writeAffine2DConfiguration(BufferedWriter writer, ChaosGameDescription description, String transformationType) throws IOException {
    writer.write(AFFINE2D + ", " + transformationType + "\n");
    writeCoords(writer, description);
    for (Transform2D transformation : description.getTransforms()) {
      Matrix2x2 matrix = ((AffineTransform2D) transformation).getMatrix();
      Vector2D vector = ((AffineTransform2D) transformation).getVector();
      writer.write(matrix.geta00() + ", " + matrix.geta01() + ", " + matrix.geta10() + ", "
          + matrix.geta11() + ", " + vector.getX0() + ", " + vector.getX1() + "\n");
    }
  }

  private void writeJuliaConfiguration(BufferedWriter writer, ChaosGameDescription description, String transformationType) throws IOException {
    JuliaTransform transformation = (JuliaTransform) description.getTransforms().get(0);
    writer.write(JULIA + ", " + transformationType + "\n");
    writeCoords(writer, description);
    Complex point = transformation.getPoint();
    writer.write(point.getX0() + ", " + point.getX1() + "\n");
  }

  private void writeCoords(BufferedWriter writer, ChaosGameDescription description) throws IOException {
    writer.write(description.getMinCoords().getX0() + ", " + description.getMinCoords().getX1() + "\n");
    writer.write(description.getMaxCoords().getX0() + ", " + description.getMaxCoords().getX1() + "\n");
  }

  /**
   * Reads the transformation type from a specified file.
   *
   * @return the transformation type
   * @throws IOException if an I/O error occurs while reading the file
   */
  public String readTransformationType() throws IOException, UnexpectedException {
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(new File(fileName).getAbsolutePath()))) {
      String line = reader.readLine();
      return line.substring(line.indexOf(",") + 2);
    } catch (IOException e) {
      throw new IOException(FILE_NOT_FOUND_MESSAGE, e);
    } catch (Exception e) {
      throw new UnexpectedException(ERROR_READING_FILE_MESSAGE);
    }
  }

  /**
   * Writes a line to a specified file.
   *
   * @param line the line to be written to the file
   * @throws IOException if an I/O error occurs while writing to the file
   */
  public void writeLineToFile(String line) throws IOException, UnexpectedException {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(new File(fileName).getAbsolutePath()))) {
      writer.write(line);
    } catch (IOException e) {
      throw new IOException(FILE_NOT_FOUND_MESSAGE, e);
    } catch (Exception e) {
      throw new UnexpectedException(ERROR_WRITING_FILE_MESSAGE);
    }
  }

  /**
   * Checks if the file contains a Mandelbrot configuration.
   *
   * @return true if the file contains a Mandelbrot configuration, false otherwise
   * @throws IOException if an I/O error occurs while reading the file
   */
  public boolean checkForMandelbrot() throws IOException, UnexpectedException {
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(new File(fileName).getAbsolutePath()))) {
      String line = reader.readLine();
      return MANDELBROT.equals(line);
    } catch (IOException e) {
      throw new IOException(FILE_NOT_FOUND_MESSAGE, e);
    } catch (Exception e) {
      throw new UnexpectedException(ERROR_READING_FILE_MESSAGE);
    }
  }
}
