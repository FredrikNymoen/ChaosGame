import mathcore.Matrix2x2;
import mathcore.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transformations.AffineTransform2D;
import transformations.Transform2D;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for the ChaosGame class.
 * This class tests the creation of a ChaosGame and the runSteps method.
 */

class ChaosGameTest {

    private ChaosGame game;
    private ChaosGameDescription description;
    private int width;
    private int height;
    private List<Transform2D> transforms;
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
        transforms = new ArrayList<>();
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
        assertNotNull("Canvas should not be null", game.getCanvas());
        assertNotNull("Transforms should not be null", description.getTransforms());
        assertNotNull("MinCoords should not be null", description.getMinCoords());
        assertNotNull("MaxCoords should not be null", description.getMaxCoords());
        assertEquals("Width should be 100", 100, game.getCanvas().getCanvasArray().length);
        assertEquals("Height should be 100", 100, game.getCanvas().getCanvasArray()[0].length);
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
        for (int i = 0; i < canvasArray.length && !pixelSet; i++) {
            for (int j = 0; j < canvasArray[i].length && !pixelSet; j++) {
                if (canvasArray[i][j] == 1) {
                    pixelSet = true;
                }
            }
        }
        assertTrue("Canvas should have at least one pixel set", pixelSet);
    }

    /**
     * Tests the runSteps method of the ChaosGame class with a negative test case.
     * It checks if no pixel is set on the canvas after running the game for 0 steps.
     */

    @Test
    void runStepsNegative() {
        game.runSteps(0); // Run the game for 100 steps.
        int[][] canvasArray = game.getCanvas().getCanvasArray();
        boolean pixelSet = false;
        for (int i = 0; i < canvasArray.length && !pixelSet; i++) {
            for (int j = 0; j < canvasArray[i].length && !pixelSet; j++) {
                if (canvasArray[i][j] == 1) {
                    pixelSet = true;
                }
            }
        }
        assertFalse("Canvas should not have one pixel set", pixelSet);
    }
}