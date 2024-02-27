import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import mathcore.Matrix2x2;
import transformations.AffineTransform2D;
import transformations.JuliaTransform;
import transformations.Transform2D;

public class ChaosGameFileHandler{
  ChaosGameDescription readFromFile(String path){
    return null;
  }
  void writeToFile(ChaosGameDescription description){
    File file = new File("file.csv");
    String path = file.getAbsolutePath();

    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))) {
      for (Transform2D transform : description.getTransforms()) {
        if(transform instanceof AffineTransform2D) {
          writer.write("Affine2D\n");
          writer.write(
              description.getMinCoords().getX0() + "," + description.getMinCoords().getX1() + "\n");
          writer.write(
              description.getMaxCoords().getX0() + "," + description.getMaxCoords().getX1() + "\n");
          Matrix2x2 matrix = ((AffineTransform2D) transform).getMatrix();
          writer.write(matrix.geta00() + ", " + matrix.geta01() + ", " + matrix.geta10() + ", " + matrix.geta11()
              + ", " + ((AffineTransform2D) transform).getVector().getX0()
              + ", " + ((AffineTransform2D) transform).getVector().getX1() + "\n");
        }
        else{
          writer.write("Julia\n");
          writer.write(((JuliaTransform) transform).getPoint().getX0() + ", " + ((JuliaTransform) transform).getPoint().getX1() + "\n");
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }
}
