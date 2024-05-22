package model.chaosgame;

import model.mathcore.Vector2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the ChaosCanvas class.
 * This class tests the various methods of the ChaosCanvas class.
 * It also tests the clear method and the boundaries of the canvas.
 */
class ChaosCanvasTest {

    private ChaosCanvas canvas;
    private int width;
    private int height;

    /**
     * Sets up the canvas with a given height, width, min and max coordinates.
     */
    @BeforeEach
    void setUp() {
        width = 10;
        height = 10;
        Vector2D minCoords = new Vector2D(0, 0);
        Vector2D maxCoords = new Vector2D(10, 10);
        canvas = new ChaosCanvas(width, height, minCoords, maxCoords);
    }

    /**
     * Tests the putPixel and getPixel methods of the ChaosCanvas class with a positive test
     * case. It creates a point within the canvas, puts a pixel on that point and then checks if the
     * pixel is set to 1.
     */
    @Test
    void testPutPixelPositive() {
        Vector2D point = new Vector2D(5.0, 9.0);
        canvas.putPixel(point);
        int pixelValue = canvas.getPixel(point);
        Assertions.assertEquals(1, pixelValue, "Pixel should be set to 1");
    }

    /**
     * Tests the putPixel and getPixel methods of the ChaosCanvas class with a negative test
     * case. It creates a point within the canvas, puts a pixel on that point and then checks if the
     * pixel is set to 0.
     */
    @Test
    void testPutPixelNegative() {
        Vector2D point = new Vector2D(5, 9);
        canvas.putPixel(point);
        int pixelValue = canvas.getPixel(point);
        Assertions.assertNotEquals(0, pixelValue, "Pixel should not be set to 0");
    }

    /**
     * Tests the clear method of the ChaosCanvas class with a positive test case. It creates
     * a point within the canvas, puts a pixel on that point and then clears the canvas. It then
     * checks if all the pixels are set to 0.
     */
    @Test
    void testClearCanvasPositive() {
        Vector2D point = new Vector2D(0.5, 0.5);
        canvas.putPixel(point);
        canvas.clear();
        int[][] canvasArray = canvas.getCanvasArray();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Assertions.assertEquals(0, canvasArray[i][j], "Canvas should be cleared to 0");
            }
        }
    }

    /**
     * Tests the clear method of the ChaosCanvas class with a negative test case. It creates
     * a point within the canvas, puts a pixel on that point and then clears the canvas. It then
     * checks if all the pixels are set to 0.
     */
    @Test
    void testClearCanvasNegative() {
        Vector2D point = new Vector2D(0.5, 0.5); // This should be within the canvas bounds
        canvas.putPixel(point);
        canvas.clear();
        int[][] canvasArray = canvas.getCanvasArray();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Assertions.assertNotEquals(1, canvasArray[i][j], "Canvas should be cleared to 0");
            }
        }
    }

    /**
     * Tests the boundaries of the canvas by attempting to put a pixel on the maximum boundary of the
     * canvas. Then expects no ArrayIndexOutOfBoundsException to be thrown.
     */
    @Test
    void testCanvasOnMaxBoundary() {
        Vector2D pointOnBoundary = new Vector2D(9, 9);
        try {
            canvas.putPixel(pointOnBoundary);
            canvas.getPixel(pointOnBoundary);
        } catch (ArrayIndexOutOfBoundsException e) {
            Assertions.fail("Should not throw ArrayIndexOutOfBoundsException when putting a pixel on the boundary");
        }
    }


    /**
     * Tests the checkIfCoordAsPixelIsOutsideCanvas method of the ChaosCanvas class
     * with a point that is outside the canvas.
     */
    @Test
    void testCheckIfCoordAsPixelIsOutsideCanvas() {
        Vector2D pointOutside = new Vector2D(11, 11);
        boolean isOutside = canvas.checkIfCoordAsPixelIsOutsideCanvas(pointOutside);
        Assertions.assertTrue(isOutside, "Point should be outside the canvas");
    }

    /**
     * Tests the checkIfCoordAsPixelIsOutsideCanvas method of the ChaosCanvas class
     * with a point that is inside the canvas.
     */
    @Test
    void testCheckIfCoordAsPixelIsInsideCanvas() {
        Vector2D pointInside = new Vector2D(5, 5);
        boolean isInside = canvas.checkIfCoordAsPixelIsOutsideCanvas(pointInside);
        Assertions.assertFalse(isInside, "Point should be inside the canvas");
    }

    /**
     * Tests the getPixel method with a point that is outside the canvas boundaries.
     */
    @Test
    void testGetPixelOutsideBounds() {
        Vector2D pointOutside = new Vector2D(-1, -1);
        int pixelValue = canvas.getPixel(pointOutside);
        Assertions.assertEquals(1, pixelValue, "Pixel value for out of bounds should be 1");
    }

    /**
     * Tests the putPixel method with a point that is outside the canvas boundaries.
     */
    @Test
    void testPutPixelOutsideBounds() {
        Vector2D pointOutside = new Vector2D(-1, -1);
        canvas.putPixel(pointOutside);
        // No exception should be thrown, and no changes should be made to the canvas
        int[][] canvasArray = canvas.getCanvasArray();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Assertions.assertEquals(0, canvasArray[i][j], "Canvas should not change for out of bounds point");
            }
        }
    }
}
