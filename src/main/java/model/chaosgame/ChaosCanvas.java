package model.chaosgame;

import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import model.transformations.AffineTransform2D;

/**
 * The ChaosCanvas class is used to represent a canvas for the chaos game.
 * The canvas is represented as a 2D array of integers, where each integer
 * represents the number of times a pixel has been visited.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */
public class ChaosCanvas {
  private final int[][] canvas;
  private final int width;
  private final int height;
  private final Vector2D minCoords;
  private final Vector2D maxCoords;
  private AffineTransform2D transformCoordsToIndices;
  private AffineTransform2D transformIndicesToCoords;


  /**
   * Constructor for ChaosCanvas.
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

  /**
   * Initializes the transforms from coordinates to indices and vice versa.
   */
  public void initializeTransforms() {
    Matrix2x2 matrix = new Matrix2x2(0, (height - 1)
        / (minCoords.getX1() - maxCoords.getX1()),
        (width - 1) / (maxCoords.getX0() - minCoords.getX0()), 0);
    Vector2D vector = new Vector2D(((height - 1) * maxCoords.getX1())
        / (maxCoords.getX1() - minCoords.getX1()),
        ((width - 1) * minCoords.getX0()) / (minCoords.getX0() - maxCoords.getX0()));
    transformCoordsToIndices = new AffineTransform2D(matrix, vector);

    // Calculate the inverse matrix for indices to coordinates transformation
    double a = matrix.geta00();
    double b = matrix.geta01();
    double c = matrix.geta10();
    double d = matrix.geta11();
    double det = a * d - b * c;

    Matrix2x2 inverseMatrix = new Matrix2x2(d / det, -b / det, -c / det, a / det);
    Vector2D subtractVector = new Vector2D(-inverseMatrix.geta00() * vector.getX0()
        - inverseMatrix.geta01() * vector.getX1(),
        - inverseMatrix.geta10() * vector.getX0() - inverseMatrix.geta11() * vector.getX1());
    transformIndicesToCoords = new AffineTransform2D(inverseMatrix, subtractVector);
  }

  /**
   * Returns the pixel value at a given point.
   *
   * @param point the point to get the pixel value from
   * @return int the pixel value
   */
  public int getPixel(Vector2D point) {
    point = transformCoordsToIndices.transform(point);

    if (checkIfOutOfBounds(point)) {
      return 1;
    }

    return canvas[(int) Math.round(point.getX0())][(int) Math.round(point.getX1())];
  }

  /**
   * Converts a pixel to a coordinate.
   *
   * @param pixel the pixel to convert
   * @return Vector2D the converted pixel to coordinate
   */
  public Vector2D pixelToCoordinate(Vector2D pixel) {
    return transformIndicesToCoords.transform(pixel);
  }

  /**
   * Converts a coordinate to a pixel.
   *
   * @param point the point to convert
   * @return Vector2D the converted point
   */
  public Vector2D coordinateToPixel(Vector2D point) {
    return transformCoordsToIndices.transform(point);
  }

  /**
   * Increments the pixel value at a given point.
   *
   * @param point the point to increment the pixel value at
   */
  public void putPixel(Vector2D point) {
    point = transformCoordsToIndices.transform(point);

    if (checkIfOutOfBounds(point)) {
      return;
    }

    canvas[(int) Math.round(point.getX0())][(int) Math.round(point.getX1())] += 1;
  }

  /**
   * Returns the canvas as an array.
   *
   * @return int[][] the canvas
   */
  public int[][] getCanvasArray() {
    return canvas;
  }

  /**
   * Clears the canvas by going through the canvas and setting all pixels to 0.
   */
  public void clear() {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        canvas[i][j] = 0;
      }
    }
  }


  /**
   * Checks if a coordinate as pixel is outside the canvas.
   *
   * @param point the point to convert to pixel and check
   * @return boolean true if the pixel is outside the canvas, false otherwise
   */
  public boolean checkIfCoordAsPixelIsOutsideCanvas(Vector2D point) {
    point = transformCoordsToIndices.transform(point);

    return checkIfOutOfBounds(point);
  }

  private boolean checkIfOutOfBounds(Vector2D point) {
    boolean flag = false;
    double x0 = point.getX0();
    double x1 = point.getX1();
    boolean outOfBounds = (x0 < 0 || Math.round(x0) >= height || x1 < 0 || Math.round(x1) >= width);
    if (outOfBounds) {
      flag = true;
    }
    return flag;
  }
}