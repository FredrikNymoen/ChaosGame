package model.mathcore;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Test class for Complex
 *
 * @version v.1.0.0
 *
 * @author Amund Larsen & Fredrik Nymoen
 */
class ComplexTest {

  private Complex complex;
  private Complex expected;

  /**
   * Sets up a complex number for testing, before each test.
   */
  @BeforeEach
  void setUp() {
    complex = new Complex(0, 1);
    expected = new Complex(Math.sqrt(0.5), Math.sqrt(0.5));
  }

  /**
   * Positive test for the sqrt method in the model.mathcore.Complex class.
   */
  @Test
  void positiveSqrt() {
    Complex result = complex.sqrt();
    assertEquals(expected.getX0(), result.getX0(), 0.0001, "Real part of sqrt(0+1i) incorrect");
    assertEquals(expected.getX1(), result.getX1(), 0.0001, "Imaginary part of sqrt(0+1i) incorrect");
  }

  /**
   * Negative test for the sqrt method in the model.mathcore.Complex class.
   */
  @Test
  void negativeSqrt() {
    Complex result = complex.sqrt();
    assertNotEquals(0, result.getX0(), 0.0001, "Real part of sqrt(0+1i) should not be 0");
    assertNotEquals(1, result.getX1(), 0.0001, "Imaginary part of sqrt(0+1i) should not be 1");
  }

  /**
   * Tests the subtract method in the model.mathcore.Complex class.
   */
  @Test
  void testSubtract() {
    Complex other = new Complex(1, 0);
    Complex result = complex.subtract(other);
    Complex expectedSubtraction = new Complex(-1, 1);

    assertEquals(expectedSubtraction.getX0(), result.getX0(), 0.0001, "Real part of subtraction incorrect");
    assertEquals(expectedSubtraction.getX1(), result.getX1(), 0.0001, "Imaginary part of subtraction incorrect");
  }

  /**
   * Tests the constructor and the inherited getX0 and getX1 methods in the model.mathcore.Complex class.
   */
  @Test
  void testConstructorAndGetters() {
    Complex newComplex = new Complex(3, 4);
    assertEquals(3, newComplex.getX0(), 0.0001, "Real part of constructor incorrect");
    assertEquals(4, newComplex.getX1(), 0.0001, "Imaginary part of constructor incorrect");
  }

  /**
   * Tests the polar method indirectly via getFourthRoots method.
   */
  @Test
  void testPolar() {
    List<Complex> roots = complex.getFourthRoots();
    assertEquals(4, roots.size(), "There should be four fourth roots");

    for (Complex root : roots) {
      assertNotNull(root, "Root should not be null");
    }
  }
}
