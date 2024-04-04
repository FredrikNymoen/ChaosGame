package chaosGame;

import static java.lang.Math.round;

import mathcore.Matrix2x2;
import mathcore.Vector2D;
import transformations.AffineTransform2D;

/**
 * chaosGame.ChaosCanvas class is used to represent a canvas and perform operations on it.
 * The canvas is represented as a 2D array of integers.
 * The canvas is used to represent the Mandelbrot set, Julia set, Burning Ship set, Tricorn set and Multibrot set.
 */
public class ChaosCanvas {
  private int[][] canvas;
  private int width;
  private int height;
  private Vector2D minCoords;
  private Vector2D maxCoords;
  private AffineTransform2D transformCoordsToIndices;

    /**
     * Constructor for the chaosGame.ChaosCanvas class.
     *
     * @param width the width of the canvas
     * @param height the height of the canvas
     * @param minCoords the minimum coordinates of the canvas
     * @param maxCoords the maximum coordinates of the canvas
     */

  public ChaosCanvas(int width, int height, Vector2D minCoords, Vector2D maxCoords) {
    this.width = width;
    this.height = height;
    this.minCoords = minCoords;
    this.maxCoords = maxCoords;
    canvas = new int[height][width];
  }

    /**
     * Returns a pixel from the canvas. Is only used for testing the putPixel method.
     *
     * @return int the width of the canvas
     */
  public int getPixel(Vector2D point) {
    Matrix2x2 matrix = new Matrix2x2(0, (height - 1) / (minCoords.getX1() - maxCoords.getX1()), (width - 1) / (maxCoords.getX0() - minCoords.getX0()), 0);
    Vector2D vector = new Vector2D(((height - 1) * maxCoords.getX1()) / (maxCoords.getX1() - minCoords.getX1()), ((width - 1) * minCoords.getX0()) / (minCoords.getX0() - maxCoords.getX0()));
    transformCoordsToIndices = new AffineTransform2D(matrix, vector);
    point = transformCoordsToIndices.transform(point);

    // return canvas[(int) Math.round(point.getX0())][(int) Math.round(point.getX1())];
    return canvas[(int) point.getX0()][(int) point.getX1()];
  }

     /**
      * Puts a pixel on the canvas.
      *
      * @param point the point to put the pixel on
      */
  public void putPixel(Vector2D point){
    Matrix2x2 matrix = new Matrix2x2(0, (height - 1) / (minCoords.getX1() - maxCoords.getX1()), (width - 1) / (maxCoords.getX0() - minCoords.getX0()), 0);
    Vector2D vector = new Vector2D(((height - 1) * maxCoords.getX1()) / (maxCoords.getX1() - minCoords.getX1()), ((width - 1) * minCoords.getX0()) / (minCoords.getX0() - maxCoords.getX0()));
    transformCoordsToIndices = new AffineTransform2D(matrix, vector);
    point = transformCoordsToIndices.transform(point);

    if (point.getX0() < 0 || point.getX0() >= height || point.getX1() < 0 || point.getX1() >= width) {
      return;
    }

    canvas[(int) point.getX0()][(int) point.getX1()] = 1;
  }
  /**
   * Returns the canvas as an array.
   *
   * @return int[][] the canvas
   */

  public int[][] getCanvasArray(){
    return canvas;
  }
    /**
     * Clears the canvas by going through the canvas and setting all pixels to 0.
     */
  public void clear(){
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        canvas[i][j] = 0;
      }
    }
  }
}