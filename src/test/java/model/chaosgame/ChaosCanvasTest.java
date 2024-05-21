package model.chaosgame;

import model.mathcore.Vector2D;
import org.junit.jupiter.api.Assertions;


/**
 * Test class for the model.chaosgame.ChaosCanvas class.
 * This class tests the putPixel and getPixel methods of the model.chaosgame.ChaosCanvas class.
 * It also tests the clear method and the boundaries of the canvas.
 */

class ChaosCanvasTest {

    private ChaosCanvas canvas;
    private int width;
    private int height;

    /**
     * Sets up the canvas with a given height, width, min and max coordinates.
     */

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        width = 10;
        height = 10;
        Vector2D minCoords = new Vector2D(0, 0);
        Vector2D maxCoords = new Vector2D(10, 10);
        canvas = new ChaosCanvas(width, height, minCoords, maxCoords);
    }

    /**
     * Tests the putPixel and getPixel methods of the model.chaosgame.ChaosCanvas class with a positive test
     * case. It creates a point within the canvas, puts a pixel on that point and then checks if the
     * pixel is set to 1.
     */
    @org.junit.jupiter.api.Test
    void testPutPixelPositive() {
        Vector2D point = new Vector2D(5.0, 9.0);
        canvas.putPixel(point);
        int pixelValue = canvas.getPixel(point);
        Assertions.assertEquals(1, pixelValue, "Pixel should be set to 1");
    }

    /**
     * Tests the putPixel and getPixel methods of the model.chaosgame.ChaosCanvas class with a negative test
     * case. It creates a point within the canvas, puts a pixel on that point and then checks if the
     * pixel is set to 0.
     */


    @org.junit.jupiter.api.Test
    void testPutPixelNegative() {
        Vector2D point = new Vector2D(5, 9);
        canvas.putPixel(point);
        int pixelValue = canvas.getPixel(point);
        Assertions.assertNotEquals(0, pixelValue, "Pixel should not be set to 1");
    }

    /**
     * Tests the clear method of the model.chaosgame.ChaosCanvas class with a positive test case. It creates
     * a point within the canvas, puts a pixel on that point and then clears the canvas. It then
     * checks if all the pixels are set to 0.
     */

    @org.junit.jupiter.api.Test
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
     * Tests the clear method of the model.chaosgame.ChaosCanvas class with a negative test case. It creates
     * a point within the canvas, puts a pixel on that point and then clears the canvas. It then
     * checks if all the pixels are set to 0.
     */

    @org.junit.jupiter.api.Test
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

    @org.junit.jupiter.api.Test
    void testCanvasOnMaxBoundary() {
        Vector2D pointOnBoundary = new Vector2D(9, 9);
        try {
            canvas.putPixel(pointOnBoundary);
            canvas.getPixel(pointOnBoundary);
        } catch (ArrayIndexOutOfBoundsException e) {
            Assertions.fail(
                "Should not throw ArrayIndexOutOfBoundsException when putting a pixel on the boundary");
        }
    }

    @org.junit.jupiter.api.Test
    void testPixelToCoordinate() {
        ChaosGame chaosGame = new ChaosGame(900, 750);
        ChaosCanvas chaosCanvas = chaosGame.getCanvas();
        Vector2D vector = new Vector2D(4.5, -1.5);

        Vector2D pixel = chaosCanvas.coordinateToPixel(vector);
        Vector2D coords = chaosCanvas.pixelToCoordinate(pixel);

        Assertions.assertEquals(vector.getX0(), coords.getX0(), 0.000001);
        Assertions.assertEquals(vector.getX1(), coords.getX1(), 0.000001);
    }

    @org.junit.jupiter.api.Test
    void testCoordinateToPixel() {
        ChaosGame chaosGame = new ChaosGame(900, 750);
        ChaosCanvas chaosCanvas = chaosGame.getCanvas();
        Vector2D vector = new Vector2D(4.5, -10.5);

        Vector2D pixel = chaosCanvas.coordinateToPixel(vector);

        Vector2D pixel2 = chaosCanvas.coordinateToPixel(vector);
        Vector2D coord = chaosCanvas.pixelToCoordinate(pixel2);
        pixel2 = chaosCanvas.coordinateToPixel(coord);


        Assertions.assertEquals(pixel2.getX0(), pixel.getX0(), 0.000001);
        Assertions.assertEquals(pixel2.getX1(), pixel.getX1(), 0.000001);
    }
}