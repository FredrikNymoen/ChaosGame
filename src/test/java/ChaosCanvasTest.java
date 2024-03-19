import mathcore.Vector2D;

import static org.junit.Assert.*;

/**
 * Test class for the ChaosCanvas class.
 * This class tests the putPixel and getPixel methods of the ChaosCanvas class.
 * It also tests the clear method and the boundaries of the canvas.
 */

class ChaosCanvasTest {

    private ChaosCanvas canvas;
    private Vector2D minCoords;
    private Vector2D maxCoords;
    private int width;
    private int height;

    /**
     * Sets up the canvas with a given height, width, min and max coordinates.
     */

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        width = 100;
        height = 100;
        minCoords = new Vector2D(0, 0);
        maxCoords = new Vector2D(1, 1);
        canvas = new ChaosCanvas(width, height, minCoords, maxCoords);
    }

    /**
     * Tests the putPixel and getPixel methods of the ChaosCanvas class with a positive test case.
     * It creates a point within the canvas, puts a pixel on that point and then checks if the pixel is set to 1.
     */


    @org.junit.jupiter.api.Test
    void testPutPixelAndGetPixelPositive() {
        Vector2D point = new Vector2D(0.5, 0.5); // This should be within the canvas bounds
        canvas.putPixel(point);
        int pixelValue = canvas.getPixel(point);
        assertEquals("Pixel should be set to 1", 1, pixelValue);
    }

    /**
     * Tests the putPixel and getPixel methods of the ChaosCanvas class with a negative test case.
     * It creates a point within the canvas, puts a pixel on that point and then checks if the pixel is set to 0.
     */

    @org.junit.jupiter.api.Test
    void testPutPixelAndGetPixelNegative() {
        Vector2D point = new Vector2D(0.5, 0.5); // This should be within the canvas bounds
        canvas.putPixel(point);
        int pixelValue = canvas.getPixel(point);
        assertNotEquals("Pixel should not be set to 0", 0, pixelValue);
    }

    /**
     * Tests the clear method of the ChaosCanvas class with a positive test case.
     * It creates a point within the canvas, puts a pixel on that point and then clears the canvas.
     * It then checks if all the pixels are set to 0.
     */

    @org.junit.jupiter.api.Test
    void testClearCanvasPositive() {
        Vector2D point = new Vector2D(0.5, 0.5); // This should be within the canvas bounds
        canvas.putPixel(point);
        canvas.clear();
        int[][] canvasArray = canvas.getCanvasArray();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                assertEquals("Canvas should be cleared to 0", 0, canvasArray[i][j]);
            }
        }
    }

    /**
     * Tests the clear method of the ChaosCanvas class with a negative test case.
     * It creates a point within the canvas, puts a pixel on that point and then clears the canvas.
     * It then checks if all the pixels are set to 0.
     */

    @org.junit.jupiter.api.Test
    void testClearCanvasNegative() {
        Vector2D point = new Vector2D(0.5, 0.5); // This should be within the canvas bounds
        canvas.putPixel(point);
        canvas.clear();
        int[][] canvasArray = canvas.getCanvasArray();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                assertNotEquals("Canvas should be cleared to 0", 1, canvasArray[i][j]);
            }
        }
    }

    /**
     * Tests the boundaries of the canvas by attempting to put a pixel outside the canvas boundaries.
     * Then expects an ArrayIndexOutOfBoundsException to be thrown.
     */

    @org.junit.jupiter.api.Test
    public void testCanvasOutOfBounderies() {
        Vector2D pointOutside = new Vector2D(-0.1, -0.1); // Outside the defined boundaries
        try {
            canvas.putPixel(pointOutside);
            fail("Expected an ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    /**
     * Tests the boundaries of the canvas by attempting to put a pixel on the maximum boundary of the canvas.
     * Then expects no ArrayIndexOutOfBoundsException to be thrown.
     */

    @org.junit.jupiter.api.Test
    public void testCanvasOnMaxBoundary() {
        Vector2D pointOnBoundary = new Vector2D(1.0, 1.0);
        try {
            canvas.putPixel(pointOnBoundary);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Should not throw ArrayIndexOutOfBoundsException when putting a pixel on the boundary");
        }
    }

}