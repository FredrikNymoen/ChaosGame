package chaosGame;

import static java.lang.Math.round;

import java.math.BigDecimal;
import java.math.MathContext;
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
  private AffineTransform2D transformIndicesToCoords;
  private Vector2D coord;

  private BigDecimal ia;
  private BigDecimal ib;
  private BigDecimal ic;
  private BigDecimal id;
  private BigDecimal subtractX;
  private BigDecimal subtractY;
  private MathContext mc;

  private BigDecimal a;
  private BigDecimal b;
  private BigDecimal c;
  private BigDecimal d;

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


    int precision = 15; // Define the precision level
    mc = new MathContext(precision);

    BigDecimal heightMinusOne = new BigDecimal(height - 1, mc);
    BigDecimal widthMinusOne = new BigDecimal(width - 1, mc);

    BigDecimal minY = new BigDecimal(minCoords.getX1(), mc);
    BigDecimal maxY = new BigDecimal(maxCoords.getX1(), mc);
    BigDecimal minX = new BigDecimal(minCoords.getX0(), mc);
    BigDecimal maxX = new BigDecimal(maxCoords.getX0(), mc);

    b = heightMinusOne.divide(maxY.subtract(minY), mc);
    c = widthMinusOne.divide(maxX.subtract(minX), mc);

    // These are zero as per your original code, but using BigDecimal for consistency
    a = BigDecimal.ZERO;
    d = BigDecimal.ZERO;

    BigDecimal det = a.multiply(d, mc).subtract(b.multiply(c, mc));

    if (det.compareTo(BigDecimal.ZERO) == 0) {
      throw new RuntimeException("Matrix is singular and cannot be inverted");
    }

    // Inverse calculations
    ia = d.divide(det, mc);
    ib = b.negate().divide(det, mc);
    ic = c.negate().divide(det, mc);
    id = a.divide(det, mc);


    // Vector calculations using BigDecimals
    BigDecimal vectorX = heightMinusOne.multiply(maxY, mc).divide(maxY.subtract(minY), mc);
    BigDecimal vectorY = widthMinusOne.multiply(minX, mc).divide(minX.subtract(maxX), mc);

    subtractX = ia.multiply(vectorX, mc).add(ib.multiply(vectorY, mc)).negate();
    subtractY = ic.multiply(vectorX, mc).add(id.multiply(vectorY, mc)).negate();
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
    if (point.getX0() < 0 || point.getX0() >= height || point.getX1() < 0 || point.getX1() >= width) {
      return 1;
    }

    //return canvas[(int) point.getX0()][(int) point.getX1()];
    return canvas[(int) Math.round(point.getX0())][(int) Math.round(point.getX1())];
  }

  public Vector2D pixelToCoordinate(Vector2D pixel) {
    // Extract original transformation components
    Matrix2x2 matrix = new Matrix2x2(0, (height - 1) / (minCoords.getX1() - maxCoords.getX1()), (width - 1) / (maxCoords.getX0() - minCoords.getX0()), 0);
    Vector2D vector = new Vector2D(((height - 1) * maxCoords.getX1()) / (maxCoords.getX1() - minCoords.getX1()), ((width - 1) * minCoords.getX0()) / (minCoords.getX0() - maxCoords.getX0()));

    // Calculate the inverse matrix
    double a = matrix.geta00(); // 0
    double b = matrix.geta01(); // (height - 1) / (minCoords.getX1() - maxCoords.getX1())
    double c = matrix.geta10(); // (width - 1) / (maxCoords.getX0() - minCoords.getX0())
    double d = matrix.geta11(); // 0

    double det = a * d - b * c;

    if (det == 0) {
      throw new RuntimeException("Matrix is singular and cannot be inverted");
    }

    Matrix2x2 inverseMatrix = new Matrix2x2(d / det, -b / det, -c / det, a / det);
    double ia = inverseMatrix.geta00();
    double ib = inverseMatrix.geta01();
    double ic = inverseMatrix.geta10();
    double id = inverseMatrix.geta11();

    Vector2D subtractVector = new Vector2D(-ia * vector.getX0() - ib * vector.getX1(), -ic * vector.getX0() - id * vector.getX1());

    transformIndicesToCoords = new AffineTransform2D(inverseMatrix, subtractVector);
    this.coord = transformIndicesToCoords.transform(pixel);

    return this.coord;
  }


  public Vector2D coordinateToPixel(Vector2D point) {
    Matrix2x2 matrix = new Matrix2x2(0, (height - 1) / (minCoords.getX1() - maxCoords.getX1()), (width - 1) / (maxCoords.getX0() - minCoords.getX0()), 0);
    Vector2D vector = new Vector2D(((height - 1) * maxCoords.getX1()) / (maxCoords.getX1() - minCoords.getX1()), ((width - 1) * minCoords.getX0()) / (minCoords.getX0() - maxCoords.getX0()));
    transformCoordsToIndices = new AffineTransform2D(matrix, vector);
    Vector2D transformedVector = transformCoordsToIndices.transform(point);

    return transformedVector;
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

    if (point.getX0() < 0 || Math.round(point.getX0()) >= height || point.getX1() < 0 || Math.round(point.getX1()) >= width) {
      return;
    }

    //canvas[(int) point.getX0()][(int) point.getX1()] += 1;
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
}