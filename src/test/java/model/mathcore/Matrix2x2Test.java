package model.mathcore;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for Matrix2x2
 *
 * @author Amund Larsen & Fredrik Nymoen
 * @version v1.0.0
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
    assertNotEquals(1, result.getX0(), 0.0001, "The result should not be 1");
    assertNotEquals(2, result.getX1(), 0.0001, "The result should not be 2");
  }

  /**
   * Tests the getter methods for each element in the matrix.
   */
  @Test
  void testGetters() {
    assertEquals(1, matrix.geta00(), 0.0001, "The value of a00 should be 1");
    assertEquals(2, matrix.geta01(), 0.0001, "The value of a01 should be 2");
    assertEquals(3, matrix.geta10(), 0.0001, "The value of a10 should be 3");
    assertEquals(4, matrix.geta11(), 0.0001, "The value of a11 should be 4");
  }

  /**
   * Tests the constructor by verifying the values of the matrix elements.
   */
  @Test
  void testConstructor() {
    Matrix2x2 matrix = new Matrix2x2(5, 6, 7, 8);
    assertEquals(5, matrix.geta00(), 0.0001, "The value of a00 should be 5");
    assertEquals(6, matrix.geta01(), 0.0001, "The value of a01 should be 6");
    assertEquals(7, matrix.geta10(), 0.0001, "The value of a10 should be 7");
    assertEquals(8, matrix.geta11(), 0.0001, "The value of a11 should be 8");
  }
}
