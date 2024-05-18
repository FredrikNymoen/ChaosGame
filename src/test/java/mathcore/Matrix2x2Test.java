package mathcore;

import static org.junit.jupiter.api.Assertions.*;

import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for Matrix2x2
 *
 * @version v.1.0.0
 *
 * @author Amund Larsen & Fredrik Nymoen
 *
 */
class Matrix2x2Test {

  private Matrix2x2 matrix;

  /**
   * Sets up a matrix for testing, before each test.
   */
  @BeforeEach
  void setUp() {
    matrix = new Matrix2x2(1, 2, 3, 4);
  }

  /**
   * Tests the multiply method in the Matrix2x2 class with a positive test.
   * The test makes a vector and multiplies it with the given matrix.
   * Then it checks if the result is as expected. For both X0 and X1.
   */

  @Test
  void multiplyPositive() {
    Vector2D vector = new Vector2D(1, 2);
    Vector2D result = matrix.multiply(vector);
    assertEquals(5, result.getX0(), 0.0001, "The result should be 5");
    assertEquals(11, result.getX1(), 0.0001, "The result should be 11");
  }

  /**
   * Tests the multiply method in the Matrix2x2 class with a negative test.
   * The test makes a vector and multiplies it with the given matrix.
   * Then it checks if the result is not as expected. For both X0 and X1.
   */

  @Test
  void multiplyNegative() {
    Vector2D vector = new Vector2D(6, 9);
    Vector2D result = matrix.multiply(vector);
    assertNotEquals(1, result.getX0(), 0.0001, "The result should be 5");
    assertNotEquals(2, result.getX1(), 0.0001, "The result should be 11");
  }
}