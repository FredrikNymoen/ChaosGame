package model.filehandling;

import exception.UnexpectedException;
import model.chaosgame.ChaosGameDescription;
import model.mathcore.Complex;
import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import model.transformations.AffineTransform2D;
import model.transformations.JuliaTransform;
import model.transformations.Transform2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the model.filehandling.ChaosGameFileHandler class.
 * This class tests the writeToFile and readFromFile methods of the model.filehandling.ChaosGameFileHandler class.
 * author Fredrik Nymoen & Amund Larsen
 * version v1.0.0
 */
class ChaosGameFileHandlerTest {

    private ChaosGameFileHandler fileHandler;
    private ChaosGameDescription description;
    private Path tempFile;

    /**
     * Sets up the model.filehandling.ChaosGameFileHandler with a temporary directory.
     * It writes an affine transformation to a file and then reads it back from the file.
     * This method is called before each test.
     *
     * @param tempDir The temporary directory used for the tests.
     */
    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        tempFile = tempDir.resolve("testChaosGame.txt");
        fileHandler = new ChaosGameFileHandler(tempFile.toString());

        Vector2D minCoords = new Vector2D(0, 0);
        Vector2D maxCoords = new Vector2D(10, 10);

        List<Transform2D> transforms = new ArrayList<>();
        Matrix2x2 matrix = new Matrix2x2(1, 0, 0, 1);
        Vector2D vector = new Vector2D(0.5, 0.5);
        transforms.add(new AffineTransform2D(matrix, vector));

        description = new ChaosGameDescription(transforms, minCoords, maxCoords);
    }

    /**
     * Tests the writeToFile and readFromFile methods of the model.filehandling.ChaosGameFileHandler class with a positive test case.
     * It writes a model.chaosgame.ChaosGameDescription to a file and then reads it back from the file.
     */
    @Test
    void testWriteAndReadNotNull() throws Exception {
        fileHandler.writeToFile(description, "Affine2D");

        assertTrue(Files.exists(tempFile), "File should exist");
        assertNotEquals(0, tempFile.toFile().length(), "File should not be empty");

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile.toFile()))) {
            String type = reader.readLine();
            assertNotNull(type, "First line (type) should not be null");
            // Additional reading and assertions can be done here...
        }

        ChaosGameFileHandler readFileHandler = new ChaosGameFileHandler(tempFile.toString());
        ChaosGameDescription readDescription = readFileHandler.readFromFile();
        assertNotNull(readDescription, "readFromFile should return a non-null description");
    }

    /**
     * Tests the writeToFile and readFromFile methods of the model.filehandling.ChaosGameFileHandler class.
     * It writes a model.chaosgame.ChaosGameDescription to a file and then reads it back from the file.
     * It then checks if the minCoords, maxCoords and transforms are equal.
     */
    @Test
    void testWriteAndReadFromFile() throws Exception {
        fileHandler.writeToFile(description, "Affine2D");

        assertTrue(Files.exists(tempFile), "File should exist");
        assertNotEquals(0, Files.size(tempFile), "File should not be empty");

        ChaosGameFileHandler readFileHandler = new ChaosGameFileHandler(tempFile.toString());
        ChaosGameDescription readDescription = readFileHandler.readFromFile();
        assertNotNull(readDescription, "readFromFile should return a non-null description");

        assertEquals(description.getMinCoords().getX0(), readDescription.getMinCoords().getX0(), "MinCoords X0 should be equal");
        assertEquals(description.getMinCoords().getX1(), readDescription.getMinCoords().getX1(), "MinCoords X1 should be equal");
        assertEquals(description.getMaxCoords().getX0(), readDescription.getMaxCoords().getX0(), "MaxCoords X0 should be equal");
        assertEquals(description.getMaxCoords().getX1(), readDescription.getMaxCoords().getX1(), "MaxCoords X1 should be equal");

        assertEquals(description.getTransforms().size(), readDescription.getTransforms().size(), "Number of transforms should be equal");
    }

    /**
     * Tests handling of an unsupported transformation type.
     */
    @Test
    void testReadUnsupportedTransformationType() throws Exception {
        Files.write(tempFile, "Unsupported, Type\n0, 0\n10, 10\n".getBytes());

        ChaosGameFileHandler readFileHandler = new ChaosGameFileHandler(tempFile.toString());

        assertThrows(UnexpectedException.class, readFileHandler::readFromFile);
    }

    /**
     * Tests writing and reading a Julia transformation.
     */
    @Test
    void testWriteAndReadJuliaTransformation() throws Exception {
        List<Transform2D> transforms = new ArrayList<>();
        transforms.add(new JuliaTransform(new Complex(0.355, 0.355), 1));
        ChaosGameDescription juliaDescription = new ChaosGameDescription(transforms, new Vector2D(0, 0), new Vector2D(10, 10));

        fileHandler.writeToFile(juliaDescription, "Julia");

        ChaosGameFileHandler readFileHandler = new ChaosGameFileHandler(tempFile.toString());
        ChaosGameDescription readDescription = readFileHandler.readFromFile();
        assertNotNull(readDescription, "readFromFile should return a non-null description");

        assertEquals(juliaDescription.getTransforms().size(), readDescription.getTransforms().size(), "Number of transforms should be equal");
    }

    /**
     * Tests the readTransformationType method of the model.filehandling.ChaosGameFileHandler class.
     * It writes a transformation type to a file and then reads it back.
     */
    @Test
    void testReadTransformationType() throws Exception {
        Files.write(tempFile, "Affine2D, TransformationType\n0, 0\n10, 10\n".getBytes());

        ChaosGameFileHandler readFileHandler = new ChaosGameFileHandler(tempFile.toString());
        String transformationType = readFileHandler.readTransformationType();
        assertEquals("TransformationType", transformationType, "Transformation type should be 'TransformationType'");
    }

    /**
     * Tests the writeLineToFile method of the model.filehandling.ChaosGameFileHandler class.
     * It writes a line to a file and then checks if the line is present in the file.
     */
    @Test
    void testWriteLineToFile() throws Exception {
        fileHandler.writeLineToFile("Test Line");

        List<String> lines = Files.readAllLines(tempFile);
        assertTrue(lines.contains("Test Line"), "File should contain 'Test Line'");
    }

    /**
     * Tests the checkForMandelbrot method of the model.filehandling.ChaosGameFileHandler class.
     * It writes a Mandelbrot configuration to a file and then checks if the file contains a Mandelbrot configuration.
     */
    @Test
    void testCheckForMandelbrot() throws Exception {
        Files.write(tempFile, "Mandelbrot\n".getBytes());

        ChaosGameFileHandler readFileHandler = new ChaosGameFileHandler(tempFile.toString());
        assertTrue(readFileHandler.checkForMandelbrot(), "File should contain 'Mandelbrot'");
    }

    /**
     * Tests checkForMandelbrot method with a file that does not contain a Mandelbrot configuration.
     */
    @Test
    void testCheckForMandelbrotFalse() throws Exception {
        Files.write(tempFile, "Affine2D\n0, 0\n10, 10\n".getBytes());

        ChaosGameFileHandler readFileHandler = new ChaosGameFileHandler(tempFile.toString());
        assertFalse(readFileHandler.checkForMandelbrot(), "File should not contain 'Mandelbrot'");
    }
}
