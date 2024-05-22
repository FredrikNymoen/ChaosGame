package model.chaosgame;

import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import model.transformations.AffineTransform2D;
import model.transformations.Transform2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the ChaosGame class.
 * This class tests the creation of a ChaosGame and the runSteps method.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
class ChaosGameTest {

    private ChaosGame game;
    private ChaosGameDescription description;
    private int width;
    private int height;
    private Vector2D minCoords;
    private Vector2D maxCoords;

    /**
     * Sets up the ChaosGame with a width, height, min and max coordinates and a list of transformations.
     * This method is called before each test.
     */
    @BeforeEach
    void setUp() {
        width = 100;
        height = 100;
        minCoords = new Vector2D(0, 0);
        maxCoords = new Vector2D(1, 1);

        Transform2D transform = new AffineTransform2D(new Matrix2x2(0.5, 0, 0, 0.5), new Vector2D(0.25, 0.25));
        List<Transform2D> transforms = new ArrayList<>();
        transforms.add(transform);

        description = new ChaosGameDescription(transforms, minCoords, maxCoords);
        game = new ChaosGame(description, width, height);
    }

    /**
     * Tests the ChaosGame creation with a positive test case.
     * It checks if the canvas, transforms, minCoords and maxCoords are not null.
     * It also checks if the width and height of the canvas are 100.
     */
    @Test
    void testChaosGameCreationPositive() {
        assertNotNull(game.getCanvas(), "Canvas should not be null");
        assertNotNull(description.getTransforms(), "Transforms should not be null");
        assertNotNull(description.getMinCoords(), "MinCoords should not be null");
        assertNotNull(description.getMaxCoords(), "MaxCoords should not be null");
        assertEquals(100, game.getCanvas().getCanvasArray().length, "Width should be 100");
        assertEquals(100, game.getCanvas().getCanvasArray()[0].length, "Height should be 100");
    }

    /**
     * Tests the runSteps method of the ChaosGame class with a positive test case.
     * It checks if at least one pixel is set on the canvas after running the game for 100 steps.
     */
    @Test
    void runStepsPositive() {
        game.runSteps(100); // Run the game for 100 steps.
        int[][] canvasArray = game.getCanvas().getCanvasArray();
        boolean pixelSet = false;
        for (int[] row : canvasArray) {
            for (int pixel : row) {
                if (pixel > 0) {
                    pixelSet = true;
                    break;
                }
            }
        }
        assertTrue(pixelSet, "Canvas should have at least one pixel set");
    }

    /**
     * Tests the runSteps method of the ChaosGame class with a negative test case.
     * It checks if no pixel is set on the canvas after running the game for 0 steps.
     */
    @Test
    void runStepsNegative() {
        game.runSteps(0); // Run the game for 0 steps.
        int[][] canvasArray = game.getCanvas().getCanvasArray();
        boolean pixelSet = false;
        for (int[] row : canvasArray) {
            for (int pixel : row) {
                if (pixel > 0) {
                    pixelSet = true;
                    break;
                }
            }
        }
        assertFalse(pixelSet, "Canvas should not have any pixel set");
    }

    /**
     * Tests the runStepsForBarnsley method of the ChaosGame class.
     * It checks if at least one pixel is set on the canvas after running the game for 100 steps.
     */
    @Test
    void runStepsForBarnsleyTest() {
        List<Transform2D> barnsleyTransforms = new ArrayList<>();
        barnsleyTransforms.add(new AffineTransform2D(new Matrix2x2(0, 0, 0, 0.16), new Vector2D(0, 0)));
        barnsleyTransforms.add(new AffineTransform2D(new Matrix2x2(0.85, 0.04, -0.04, 0.85), new Vector2D(0, 1.6)));
        barnsleyTransforms.add(new AffineTransform2D(new Matrix2x2(0.2, -0.26, 0.23, 0.22), new Vector2D(0, 1.6)));
        barnsleyTransforms.add(new AffineTransform2D(new Matrix2x2(-0.15, 0.28, 0.26, 0.24), new Vector2D(0, 0.44)));

        ChaosGameDescription barnsleyDescription = new ChaosGameDescription(barnsleyTransforms, minCoords, maxCoords);
        game = new ChaosGame(barnsleyDescription, width, height);

        game.runStepsForBarnsley(100000);
        int[][] canvasArray = game.getCanvas().getCanvasArray();
        boolean pixelSet = false;
        for (int[] row : canvasArray) {
            for (int pixel : row) {
                if (pixel > 0) {
                    pixelSet = true;
                    break;
                }
            }
        }
        assertTrue(pixelSet, "Canvas should have at least one pixel set");
    }

    /**
     * Tests the fractalWithIterationTransformation method of the ChaosGame class.
     * It checks if at least one pixel is set on the canvas after running the fractal generation.
     */
    @Test
    void fractalWithIterationTransformationTest() {
        List<Transform2D> iterTransforms = new ArrayList<>();
        iterTransforms.add(new AffineTransform2D(new Matrix2x2(0.5, 0, 0, 0.5), new Vector2D(0.5, 0.5)));

        ChaosGameDescription iterDescription = new ChaosGameDescription(iterTransforms, minCoords, maxCoords);
        game = new ChaosGame(iterDescription, width, height);

        game.fractalWithIterationTransformation();
        int[][] canvasArray = game.getCanvas().getCanvasArray();
        boolean pixelSet = false;
        for (int[] row : canvasArray) {
            for (int pixel : row) {
                if (pixel > 0) {
                    pixelSet = true;
                    break;
                }
            }
        }
        assertTrue(pixelSet, "Canvas should have at least one pixel set");
    }

    /**
     * Tests the ChaosGame creation with width and height only constructor.
     * It checks if the canvas and current point are initialized correctly.
     */
    @Test
    void testChaosGameCreationWithWidthAndHeight() {
        game = new ChaosGame(width, height);
        assertNotNull(game.getCanvas(), "Canvas should not be null");
        assertEquals(0, game.getCanvas().getPixel(new Vector2D(0, 0)), "Initial point should be at (0,0)");
    }
}
