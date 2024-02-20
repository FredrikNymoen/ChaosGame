package transformations;

import static org.junit.jupiter.api.Assertions.*;

import mathcore.Matrix2x2;
import mathcore.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transformations.AffineTransform2D;

/**
 * Test class for transformations.AffineTransform2D
 *
 * @author Amund Larsen & Fredrik Nymoen
 * @version v.1.0.0
 */
class AffineTransform2DTest {

  private Matrix2x2 matrix;
  private Vector2D vector;

  /**
   * Sets up a matrix and a vector for testing, before each test.
   */
  @BeforeEach
  void setUp() {
    matrix = new Matrix2x2(1, 0, 0, 1);
    vector = new Vector2D(1, 1);
  }

  /**
   * Tests the transform method in the transformations.AffineTransform2D class with a positive test.
   * The test makes a point and transforms it with the transformations.AffineTransform2D.
   * Then it checks if the result is as expected. For both X0 and X1.
   */
  @Test
  void positiveTransform() {
    AffineTransform2D affineTransform2D = new AffineTransform2D(matrix, vector);
    Vector2D point = new Vector2D(1, 1);
    Vector2D result = affineTransform2D.transform(point);
    assertEquals(2, result.getX0(), 0.0001, "The result should be 2");
    assertEquals(2, result.getX1(), 0.0001, "The result should be 2");
  }

  /**
   * Tests the transform method in the transformations.AffineTransform2D class with a negative test.
   * The test makes a point and transforms it with the transformations.AffineTransform2D.
   * Then it checks if the result is not as expected. For both X0 and X1.
   */
  @Test
  void negativeTransform() {
    Matrix2x2 matrix = new Matrix2x2(1, 0, 0, 1);
    Vector2D vector = new Vector2D(1, 1);
    AffineTransform2D affineTransform2D = new AffineTransform2D(matrix, vector);
    Vector2D point = new Vector2D(1, 1);
    Vector2D result = affineTransform2D.transform(point);
    assertNotEquals(1, result.getX0(), 0.0001, "The result should not be 1");
    assertNotEquals(1, result.getX1(), 0.0001, "The result should not be 1");
  }

}