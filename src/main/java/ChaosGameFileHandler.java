import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import transformations.AffineTransform2D;
import transformations.Transform2D;

public class ChaosGameFileHandler{
  ChaosGameDescription readFromFile(String path){
    return null;
  }
  void writeToFile(ChaosGameDescription description){
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("../../../../file.csv"))) {
      for (Transform2D transform : description.getTransforms()) {
        if(transform instanceof AffineTransform2D) {
          writer.write("Affine2D\n");
          writer.write(
              description.getMinCoords().getX0() + "," + description.getMinCoords().getX1() + "\n");
          writer.write(
              description.getMaxCoords().getX0() + "," + description.getMaxCoords().getX1() + "\n");
          writer.write(transform.toString() + "\n");
        }
        else{
          writer.write("Julia\n");
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }
}
