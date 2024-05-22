package model.transformations;

import static org.junit.jupiter.api.Assertions.*;

import model.mathcore.Complex;
import model.mathcore.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Test class for JuliaTransform
 *
 * @author Amund Larsen & Fredrik Nymoen
 * @version v1.0.0
 */
class JuliaTransformTest {
  private JuliaTransform juliaTransformPositive;
  private JuliaTransform juliaTransformNegative;
  private Complex complex;

  /**
   * Sets up a complex number and a JuliaTransform for testing, before each test.
   */
  @BeforeEach
  void setUp() {
    complex = new Complex(1, 2);
    juliaTransformPositive = new JuliaTransform(complex, 1);
    juliaTransformNegative = new JuliaTransform(complex, -1);
  }

  /**
   * Tests the constructor and the getPoint method in the JuliaTransform class.
   * Verifies that the complex point is set correctly.
   */
  @Test
  void testConstructorAndGetPoint() {
    assertEquals(complex, juliaTransformPositive.getPoint(), "The complex point should be equal to the provided point");
    assertEquals(complex, juliaTransformNegative.getPoint(), "The complex point should be equal to the provided point");
  }

  /**
   * Tests the transform method in the JuliaTransform class with a positive sign.
   * The test makes a vector and transforms it with the given JuliaTransform.
   * Then it checks if the result is as expected. For both X0 and X1.
   */
  @Test
  void transformPositive() {
    Vector2D vector = new Vector2D(2, 2);
    Vector2D result = juliaTransformPositive.transform(vector);
    List<Complex> roots = new Complex(1, 0).getFourthRoots();
    Complex expectedRoot = roots.get(1);

    assertEquals(expectedRoot.getX0(), result.getX0(), 0.0001, "The result X0 should be equal to the expected value");
    assertEquals(expectedRoot.getX1(), result.getX1(), 0.0001, "The result X1 should be equal to the expected value");
  }

  /**
   * Tests the transform method in the JuliaTransform class with a negative sign.
   * The test makes a vector and transforms it with the given JuliaTransform.
   * Then it checks if the result is as expected. For both X0 and X1.
   */
  @Test
  void transformNegative() {
    Vector2D vector = new Vector2D(2, 2);
    Vector2D result = juliaTransformNegative.transform(vector);
    List<Complex> roots = new Complex(1, 0).getFourthRoots();
    Complex expectedRoot = roots.get(1); // Assume the same root is chosen for negative sign

    assertEquals(expectedRoot.getX0(), result.getX0(), 0.0001, "The result X0 should be equal to the expected value");
    assertEquals(expectedRoot.getX1(), result.getX1(), 0.0001, "The result X1 should be equal to the expected value");
  }

  /**
   * Tests the transform method in the JuliaTransform class with a different vector.
   * Verifies that the transform method works correctly with different input values.
   */
  @Test
  void transformDifferentVector() {
    Vector2D vector = new Vector2D(4, 6);
    Vector2D result = juliaTransformPositive.transform(vector);
    List<Complex> roots = new Complex(3, 4).getFourthRoots();
    Complex expectedRoot = roots.get(1); // Based on the sign provided during instantiation

    assertEquals(expectedRoot.getX0(), result.getX0(), 0.0001, "The result X0 should be equal to the expected value");
    assertEquals(expectedRoot.getX1(), result.getX1(), 0.0001, "The result X1 should be equal to the expected value");
  }

  /**
   * Tests the transform method in the JuliaTransform class with a point that results in roots with multiple values.
   * Verifies that the correct root is chosen based on the sign.
   */
  @Test
  void transformMultipleRoots() {
    Vector2D vector = new Vector2D(0, 0);
    Vector2D result = juliaTransformPositive.transform(vector);
    List<Complex> roots = new Complex(-1, -2).getFourthRoots();
    Complex expectedRoot = roots.get(1); // Based on the sign provided during instantiation

    assertEquals(expectedRoot.getX0(), result.getX0(), 0.0001, "The result X0 should be equal to the expected value");
    assertEquals(expectedRoot.getX1(), result.getX1(), 0.0001, "The result X1 should be equal to the expected value");
  }

  /**
   * Tests the transform method in the JuliaTransform class with a different sign value.
   * Verifies that the correct root is chosen based on the sign value provided during instantiation.
   */
  @Test
  void transformDifferentSign() {
    JuliaTransform juliaTransform = new JuliaTransform(complex, 2);
    Vector2D vector = new Vector2D(2, 2);
    Vector2D result = juliaTransform.transform(vector);
    List<Complex> roots = new Complex(1, 0).getFourthRoots();
    Complex expectedRoot = roots.get(2);

    assertEquals(expectedRoot.getX0(), result.getX0(), 0.0001, "The result X0 should be equal to the expected value");
    assertEquals(expectedRoot.getX1(), result.getX1(), 0.0001, "The result X1 should be equal to the expected value");
  }
}
