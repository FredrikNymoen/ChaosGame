import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import mathcore.Complex;
import mathcore.Matrix2x2;
import mathcore.Vector2D;
import transformations.AffineTransform2D;
import transformations.JuliaTransform;
import transformations.Transform2D;

public class ChaosGameFileHandler {

  ChaosGameDescription readFromFile(String path) {
    File file = new File(path);
    ChaosGameDescription description = null;
    String[] transformationValues;
    List<Transform2D> transforms = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()))) {
      String line = reader.readLine();

      if ("Affine2D".equals(line)) {
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
          System.out.println(line);
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
        line = reader.readLine();
        String[] pointValues = line.split(", ");
        Complex point = new Complex(
            Double.parseDouble(pointValues[0]),
            Double.parseDouble(pointValues[1])
        );
        transforms.add(new JuliaTransform(point, 1));
        description = new ChaosGameDescription(transforms);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return description;
  }

  void writeToFile(ChaosGameDescription description, String path) {
    File file = new File(path);

    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()))) {
      if (description.getTransforms().get(0) instanceof AffineTransform2D) {
        writer.write("Affine2D\n");
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
          writer.write("Julia\n");
          writer.write(transformation.getPoint().getX0() + ", "
              + transformation.getPoint().getX1() + "\n");
        }
    } catch(Exception e){
      System.out.println(e.getMessage());
    }
  }
}
