package mathcore;

import static org.junit.jupiter.api.Assertions.*;

import mathcore.Vector2D;

/**
 * Test class for mathcore.Vector2D
 *
 * @author Amund Larsen & Fredrik Nymoen
 *
 * @version v.1.0.0
 *
 */

class Vector2DTest {

  private Vector2D vector;

  /**
   * Sets up a vector for testing, before each test.
   */

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    vector = new Vector2D(1, 2);
  }

  /**
   * Tests the getX0 method in the mathcore.Vector2D class.
   */

  @org.junit.jupiter.api.Test
  void getX0Positive() {
    assertEquals(1, vector.getX0(), 0.0001, "The result should be 1");
  }

  /**
   * Tests the getX1 method in the mathcore.Vector2D class.
   */

  @org.junit.jupiter.api.Test
  void getX1Positive() {
    assertEquals(2, vector.getX1(), 0.0001, "The result should be 2");
  }

  /**
   * Tests the add method in the mathcore.Vector2D class with a positive test.
   * The test makes an additional vector and adds it to the original vector.
   * Then it checks if the result is as expected. For both X0 and X1.
   */

  @org.junit.jupiter.api.Test
  void addPositive() {
    Vector2D other = new Vector2D(3, 4);
    Vector2D result = vector.add(other);
    assertEquals(4, result.getX0(), 0.0001, "The result should be 4");
    assertEquals(6, result.getX1(), 0.0001, "The result should be 6");
  }

  /**
   * Tests the add method in the mathcore.Vector2D class with a negative test.
   * The test makes an additional vector and adds it to the original vector.
   * Then it checks if the result is not as expected. For both X0 and X1.
   */

  @org.junit.jupiter.api.Test
  void addNegative() {
    Vector2D other = new Vector2D(1, 2);
    Vector2D result = vector.add(other);
    assertNotEquals(4, result.getX0(),0.0001, "The result should not be 4");
    assertNotEquals(6, result.getX1(),0.0001, "The result should not be 6");
  }

  /**
   * Tests the subtract method in the mathcore.Vector2D class with a positive test.
   * The test makes an additional vector and subtracts it from the original vector.
   * Then it checks if the result is as expected. For both X0 and X1.
   */

  @org.junit.jupiter.api.Test
  void subtractPositive() {
    Vector2D other = new Vector2D(3, 4);
    Vector2D result = vector.subtract(other);
    assertEquals(-2, result.getX0(), 0.0001, "The result should be -2");
    assertEquals(-2, result.getX1(), 0.0001, "The result should be -2");
  }

  /**
   * Tests the subtract method in the mathcore.Vector2D class with a negative test.
   * The test makes an additional vector and subtracts it from the original vector.
   * Then it checks if the result is not as expected. For both X0 and X1.
   */

  @org.junit.jupiter.api.Test
  void subtractNegative() {
    Vector2D other = new Vector2D(1, 2);
    Vector2D result = vector.subtract(other);
    assertNotEquals(-2, result.getX0(), 0.0001, "The result should not be -2");
    assertNotEquals(-2, result.getX1(), 0.0001, "The result should not be -2");
  }
}