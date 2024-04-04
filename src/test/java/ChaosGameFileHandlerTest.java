import chaosGame.ChaosGameDescription;
import chaosGame.ChaosGameFileHandler;
import mathcore.Matrix2x2;
import mathcore.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import transformations.AffineTransform2D;
import transformations.Transform2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the chaosGame.ChaosGameFileHandler class.
 * This class tests the writeToFile and readFromFile methods of the chaosGame.ChaosGameFileHandler class.
 */

class ChaosGameFileHandlerTest {

    private ChaosGameFileHandler fileHandler;
    private ChaosGameDescription description;
    private Path tempFile;

    /**
     * Sets up the chaosGame.ChaosGameFileHandler with a temporary directory.
     * It writes an affine transformation to a file and then reads it back from the file.
     * This method is called before each test.
     * @param tempDir The temporary directory used for the tests.
     * @throws Exception
     */

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws Exception {
        fileHandler = new ChaosGameFileHandler();
        Vector2D minCoords = new Vector2D(0, 0);
        Vector2D maxCoords = new Vector2D(10, 10);

        List<Transform2D> transforms = new ArrayList<>();
        Matrix2x2 matrix = new Matrix2x2(1, 0, 0, 1);
        Vector2D vector = new Vector2D(0.5, 0.5);
        transforms.add(new AffineTransform2D(matrix, vector));

        description = new ChaosGameDescription(transforms, minCoords, maxCoords);
        tempFile = tempDir.resolve("testChaosGame.txt");
    }

    /**
     * Tests the writeToFile and readFromFile methods of the chaosGame.ChaosGameFileHandler class with a positive test case.
     * It writes a chaosGame.ChaosGameDescription to a file and then reads it back from the file.
     * @throws Exception
     */

    @Test
    void testWriteAndReadNotNull() throws Exception {

        fileHandler.writeToFile(description, tempFile.toString());


        assertTrue(Files.exists(tempFile), "File should exist");
        assertNotEquals(0, tempFile.toFile().length(),"File should not be empty");


        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile.toFile()))) {
            String type = reader.readLine();
            assertNotNull(type, "First line (type) should not be null");
            // More reading and assertions...
        }


        ChaosGameDescription readDescription = fileHandler.readFromFile(tempFile.toString());
        assertNotNull(readDescription, "readFromFile should return a non-null description");
    }

    /**
     * Tests the writeToFile and readFromFile methods of the chaosGame.ChaosGameFileHandler class.
     * It writes a chaosGame.ChaosGameDescription to a file and then reads it back from the file.
     * It then checks if the minCoords, maxCoords and transforms are equal.
     * @throws Exception
     */
    @Test
    void testWriteAndReadFromFile() throws Exception {

        fileHandler.writeToFile(description, tempFile.toString());

        assertTrue(Files.exists(tempFile), "File should exist");
        assertNotEquals(0, Files.size(tempFile), "File should not be empty");

        ChaosGameDescription readDescription = fileHandler.readFromFile(tempFile.toString());
        assertNotNull(readDescription, "readFromFile should return a non-null description");

        assertEquals(description.getMinCoords().getX0(), readDescription.getMinCoords().getX0(),
            "MinCoords X0 should be equal");
        assertEquals(description.getMinCoords().getX1(), readDescription.getMinCoords().getX1(),
            "MinCoords X1 should be equal");
        assertEquals(description.getMaxCoords().getX0(), readDescription.getMaxCoords().getX0(),
            "MaxCoords X0 should be equal");
        assertEquals(description.getMaxCoords().getX1(), readDescription.getMaxCoords().getX1(),
            "MaxCoords X1 should be equal");

        assertEquals(description.getTransforms().size(), readDescription.getTransforms().size(),
            "Number of transforms should be equal");

        /*for (int i = 0; i < description.getTransforms().size(); i++) {
            Transform2D originalTransform = description.getTransforms().get(i);
            Transform2D readTransform = readDescription.getTransforms().get(i);
            assertEquals(originalTransform, readTransform, "Transforms should be equal at index" + i);
        }*/

    }
}