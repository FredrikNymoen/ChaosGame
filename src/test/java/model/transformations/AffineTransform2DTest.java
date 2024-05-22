package model.transformations;

import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the AffineTransform2D class.
 * This class tests the constructor, transform method, and the getters of the AffineTransform2D class.
 * @author Amund Larsen & Fredrik Nymoen
 * @version v1.0.0
 */
class AffineTransform2DTest {

  private Matrix2x2 matrix;
  private Vector2D vector;
  private AffineTransform2D affineTransform2D;

  /**
   * Sets up a matrix and a vector for testing, before each test.
   */
  @BeforeEach
  void setUp() {
    matrix = new Matrix2x2(1, 0, 0, 1);
    vector = new Vector2D(1, 1);
    affineTransform2D = new AffineTransform2D(matrix, vector);
  }

  /**
   * Tests the constructor of the model.transformations.AffineTransform2D class.
   * It verifies that the matrix and vector are set correctly.
   */
  @Test
  void testConstructor() {
    assertNotNull(affineTransform2D, "AffineTransform2D should not be null");
    assertEquals(matrix, affineTransform2D.getMatrix(), "Matrix should be equal to the provided matrix");
    assertEquals(vector, affineTransform2D.getVector(), "Vector should be equal to the provided vector");
  }

  /**
   * Tests the transform method in the model.transformations.AffineTransform2D class with a positive test.
   * The test makes a point and transforms it with the model.transformations.AffineTransform2D.
   * Then it checks if the result is as expected. For both X0 and X1.
   */
  @Test
  void positiveTransform() {
    Vector2D point = new Vector2D(1, 1);
    Vector2D result = affineTransform2D.transform(point);
    assertEquals(2, result.getX0(), 0.0001, "The result should be 2");
    assertEquals(2, result.getX1(), 0.0001, "The result should be 2");
  }

  /**
   * Tests the transform method in the model.transformations.AffineTransform2D class with a negative test.
   * The test makes a point and transforms it with the model.transformations.AffineTransform2D.
   * Then it checks if the result is not as expected. For both X0 and X1.
   */
  @Test
  void negativeTransform() {
    Vector2D point = new Vector2D(1, 1);
    Vector2D result = affineTransform2D.transform(point);
    assertNotEquals(1, result.getX0(), 0.0001, "The result should not be 1");
    assertNotEquals(1, result.getX1(), 0.0001, "The result should not be 1");
  }

  /**
   * Tests the getMatrix method of the model.transformations.AffineTransform2D class.
   * It verifies that the matrix is returned correctly.
   */
  @Test
  void testGetMatrix() {
    assertEquals(matrix, affineTransform2D.getMatrix(), "Matrix should be equal to the provided matrix");
  }

  /**
   * Tests the getVector method of the model.transformations.AffineTransform2D class.
   * It verifies that the vector is returned correctly.
   */
  @Test
  void testGetVector() {
    assertEquals(vector, affineTransform2D.getVector(), "Vector should be equal to the provided vector");
  }
}
