package model.transformations;

import static org.junit.jupiter.api.Assertions.*;

import model.mathcore.Complex;
import model.mathcore.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for JuliaTransform
 *
 * @version v.1.0.0
 *
 * @author Amund Larsen & Fredrik Nymoen
 *
 */
class JuliaTransformTest {
  private JuliaTransform juliaTransform;

  /**
   * Sets up a complex number and a JuliaTransform for testing, before each test.
   */

  @BeforeEach
  void setUp() {
    Complex complex = new Complex(1, 2);
    juliaTransform = new JuliaTransform(complex, 1);
  }

  /**
   * Tests the transform method in the JuliaTransform class with a positive test.
   * The test makes a vector and transforms it with the given JuliaTransform.
   * Then it checks if the result is as expected. For both X0 and X1.
   */

  @Test
  void transformPositive() {
    Vector2D vector = new Vector2D(2, 2);
    Vector2D result = juliaTransform.transform(vector);
    assertEquals(0, result.getX0(), 0.0001, "The result should be 1");
    assertEquals(1, result.getX1(), 0.0001, "The result should be 0");
  }

  /**
   * Tests the transform method in the JuliaTransform class with a negative test.
   * The test makes a vector and transforms it with the given JuliaTransform.
   * Then it checks if the result is not as expected. For both X0 and X1.
   */

  @Test
  void transformNegative() {
    Vector2D vector = new Vector2D(4, 6);
    Vector2D result = juliaTransform.transform(vector);
    assertNotEquals(1, result.getX0(), 0.0001, "The result should not be 1");
    assertNotEquals(0, result.getX1(), 0.0001, "The result should not be 0");
  }
}