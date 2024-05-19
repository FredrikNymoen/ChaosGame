package model.chaosGame;

import static java.lang.Math.round;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import model.transformations.AffineTransform2D;

/**
 * model.chaosGame.ChaosCanvas class is used to represent a canvas and perform operations on it.
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
  private AffineTransform2D transformIndicesToCoords;


  /**
     * Constructor for the model.chaosGame.ChaosCanvas class.
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
    initializeTransforms();
  }

  private void initializeTransforms() {
    Matrix2x2 matrix = new Matrix2x2(0, (height - 1) / (minCoords.getX1() - maxCoords.getX1()),
        (width - 1) / (maxCoords.getX0() - minCoords.getX0()), 0);
    Vector2D vector = new Vector2D(((height - 1) * maxCoords.getX1()) / (maxCoords.getX1() - minCoords.getX1()),
        ((width - 1) * minCoords.getX0()) / (minCoords.getX0() - maxCoords.getX0()));
    transformCoordsToIndices = new AffineTransform2D(matrix, vector);

    // Calculate the inverse matrix for indices to coordinates transformation
    double a = matrix.geta00();
    double b = matrix.geta01();
    double c = matrix.geta10();
    double d = matrix.geta11();
    double det = a * d - b * c;

    Matrix2x2 inverseMatrix = new Matrix2x2(d / det, -b / det, -c / det, a / det);
    Vector2D subtractVector = new Vector2D(-inverseMatrix.geta00() * vector.getX0() - inverseMatrix.geta01() * vector.getX1(),
        -inverseMatrix.geta10() * vector.getX0() - inverseMatrix.geta11() * vector.getX1());
    transformIndicesToCoords = new AffineTransform2D(inverseMatrix, subtractVector);
  }

    /**
     * Returns a pixel from the canvas. Is only used for testing the putPixel method.
     *
     * @return int the width of the canvas
     */
  public int getPixel(Vector2D point) {
    point = transformCoordsToIndices.transform(point);

    if (isPixelOutsideCanvas(point)) {
      return 1;
    }

    return canvas[(int) Math.round(point.getX0())][(int) Math.round(point.getX1())];
  }

  public Vector2D pixelToCoordinate(Vector2D pixel) {
    return transformIndicesToCoords.transform(pixel);
  }


  public Vector2D coordinateToPixel(Vector2D point) {
    return transformCoordsToIndices.transform(point);
  }


     /**
      * Puts a pixel on the canvas.
      *
      * @param point the point to put the pixel on
      */
  public void putPixel(Vector2D point){
    point = transformCoordsToIndices.transform(point);

    if (isPixelOutsideCanvas(point)) {
      return;
    }

    canvas[(int) Math.round(point.getX0())][(int) Math.round(point.getX1())] += 1;
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


  public boolean isPixelOutsideCanvas(Vector2D point) {
    boolean flag = false;

    if (point.getX0() < 0 || Math.round(point.getX0()) >= height || point.getX1() < 0 || Math.round(point.getX1()) >= width) {
      flag = true;
    }
    return flag;
  }

  public boolean checkIfCoordAsPixelIsOutsideCanvas(Vector2D point) {
    point = transformCoordsToIndices.transform(point);
    boolean flag = false;

    if (point.getX0() < 0 || Math.round(point.getX0()) >= height || point.getX1() < 0 || Math.round(point.getX1()) >= width) {
      flag = true;
    }
    return flag;
  }
}