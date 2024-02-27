import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import transformations.Transform2D;

public class ChaosGameFileHandler{
  ChaosGameDescription readFromFile(String path){
    return null;
  }
  ChaosGameDescription writeToFile(ChaosGameDescription description, String path){
    try(BufferedWriter writer = Files.newBufferedWriter(Path.of(filename))){
      writer.write(description.getMinCoords().getX0() + " " + description.getMinCoords().getX1() + "\n");
      writer.write(description.getMaxCoords().getX0() + " " + description.getMaxCoords().getX1() + "\n");
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return description;
  }
}
