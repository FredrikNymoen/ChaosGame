import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for Complex
 *
 * @author Amund Larsen & Fredrik Nymoen
 * @version v.1.0.0
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
   * Positive test for the sqrt method in the Complex class.
   */
  @Test
  void positiveSqrt() {
      Complex result1 = complex.sqrt();
      assertEquals(expected.getX0(), result1.getX0(), 0.0001, "Real part of sqrt(0+1i) incorrect");
      assertEquals(expected.getX1(), result1.getX1(), 0.0001, "Imaginary part of sqrt(0+1i) incorrect");
  }

  /**
   * Negative test for the sqrt method in the Complex class.
   */
  @Test
  void negativeSqrt() {
      Complex result2 = complex.sqrt();
      assertNotEquals(0, result2.getX0(), 0.0001, "Real part of sqrt(0+1i) incorrect");
      assertNotEquals(1, result2.getX1(), 0.0001, "Imaginary part of sqrt(0+1i) incorrect");
  }

}