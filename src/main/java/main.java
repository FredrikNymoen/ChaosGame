import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class main {

  public static void main(String[] args) {
    File file = new File("file.csv");
    String path = file.getAbsolutePath();
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))) {
      writer.write("Hello, orld!\n");
      writer.write("Hello, World!\n");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
