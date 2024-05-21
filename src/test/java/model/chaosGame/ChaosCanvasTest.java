package model.chaosGame;

import model.chaosGame.ChaosCanvas;
import model.chaosGame.ChaosGame;
import model.mathcore.Vector2D;

import static org.junit.Assert.*;

/**
 * Test class for the model.chaosGame.ChaosCanvas class.
 * This class tests the putPixel and getPixel methods of the model.chaosGame.ChaosCanvas class.
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
        width = 10;
        height = 10;
        minCoords = new Vector2D(0, 0);
        maxCoords = new Vector2D(10, 10);
        canvas = new ChaosCanvas(width, height, minCoords, maxCoords);
    }

    /**
     * Tests the putPixel and getPixel methods of the model.chaosGame.ChaosCanvas class with a positive test
     * case. It creates a point within the canvas, puts a pixel on that point and then checks if the
     * pixel is set to 1.
     */

    //Noe rart med getPixel metoden. Vet ikke helt hva som er galt, eller putPixel metoden.
    //Klarer hvertfall ikke å finne point i canvaset.
    @org.junit.jupiter.api.Test
    void testPutPixelPositive() {
        Vector2D point = new Vector2D(5.0, 9.0);
        canvas.putPixel(point);
        int pixelValue = canvas.getPixel(point);
        assertEquals("Pixel should be set to 1", 1, pixelValue);
    }

    /**
     * Tests the putPixel and getPixel methods of the model.chaosGame.ChaosCanvas class with a negative test
     * case. It creates a point within the canvas, puts a pixel on that point and then checks if the
     * pixel is set to 0.
     */


    @org.junit.jupiter.api.Test
    void testPutPixelNegative() {
        Vector2D point = new Vector2D(5, 9);
        canvas.putPixel(point);
        int pixelValue = canvas.getPixel(point);
        assertNotEquals("Pixel should not be set to 1", 0, pixelValue);
    }

    /**
     * Tests the clear method of the model.chaosGame.ChaosCanvas class with a positive test case. It creates
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
                assertEquals("Canvas should be cleared to 0", 0, canvasArray[i][j]);
            }
        }
    }

    /**
     * Tests the clear method of the model.chaosGame.ChaosCanvas class with a negative test case. It creates
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
                assertNotEquals("Canvas should be cleared to 0", 1, canvasArray[i][j]);
            }
        }
    }

    /**
     * Tests the boundaries of the canvas by attempting to put a pixel on the maximum boundary of the
     * canvas. Then expects no ArrayIndexOutOfBoundsException to be thrown.
     */

    @org.junit.jupiter.api.Test
    public void testCanvasOnMaxBoundary() {
        Vector2D pointOnBoundary = new Vector2D(9, 9);
        try {
            canvas.putPixel(pointOnBoundary);
            canvas.getPixel(pointOnBoundary);
            System.out.println(canvas.getPixel(pointOnBoundary));
            System.out.println(pointOnBoundary.getX0() + " " + pointOnBoundary.getX1());
        } catch (ArrayIndexOutOfBoundsException e) {
            fail(
                "Should not throw ArrayIndexOutOfBoundsException when putting a pixel on the boundary");
        }
    }

    @org.junit.jupiter.api.Test
    public void testPixelToCoordinate() {
        ChaosGame chaosGame = new ChaosGame(900, 750);
        ChaosCanvas chaosCanvas = chaosGame.getCanvas();
        Vector2D vector = new Vector2D(4.5, -1.5);

        Vector2D pixel = chaosCanvas.coordinateToPixel(vector);
        Vector2D coords = chaosCanvas.pixelToCoordinate(pixel);

        assertEquals(vector.getX0(),coords.getX0(),0.000001);
        assertEquals(vector.getX1(),coords.getX1(),0.000001);
    }

    @org.junit.jupiter.api.Test
    public void testCoordinateToPixel() {
        ChaosGame chaosGame = new ChaosGame(900, 750);
        ChaosCanvas chaosCanvas = chaosGame.getCanvas();
        Vector2D vector = new Vector2D(4.5, -10.5);

        Vector2D pixel = chaosCanvas.coordinateToPixel(vector);

        Vector2D pixel2 = chaosCanvas.coordinateToPixel(vector);
        Vector2D coord = chaosCanvas.pixelToCoordinate(pixel2);
        pixel2 = chaosCanvas.coordinateToPixel(coord);


        assertEquals(pixel2.getX0(),pixel.getX0(),0.000001);
        assertEquals(pixel2.getX1(),pixel.getX1(),0.000001);
    }


}