import static org.junit.jupiter.api.Assertions.*;

class Vector2DTest {

  private Vector2D vector;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    vector = new Vector2D(1, 2);
  }
  @org.junit.jupiter.api.Test
  void getX0Positive() {
    assertEquals(1, vector.getX0(), 0.0001, "The result should be 1");
  }

  @org.junit.jupiter.api.Test
  void getX1Positive() {
    assertEquals(2, vector.getX1(), 0.0001, "The result should be 2");
  }

  @org.junit.jupiter.api.Test
  void addPositive() {
    Vector2D other = new Vector2D(3, 4);
    Vector2D result = vector.add(other);
    assertEquals(4, result.getX0(), 0.0001, "The result should be 4");
    assertEquals(6, result.getX1(), 0.0001, "The result should be 6");
  }
  @org.junit.jupiter.api.Test
  void addNegative() {
    Vector2D other = new Vector2D(1, 2);
    Vector2D result = vector.add(other);
    assertNotEquals(4, result.getX0(),0.0001, "The result should not be 4");
    assertNotEquals(6, result.getX1(),0.0001, "The result should not be 6");
  }

  @org.junit.jupiter.api.Test
  void subtractPositive() {
    Vector2D other = new Vector2D(3, 4);
    Vector2D result = vector.subtract(other);
    assertEquals(-2, result.getX0(), 0.0001, "The result should be -2");
    assertEquals(-2, result.getX1(), 0.0001, "The result should be -2");
  }
  @org.junit.jupiter.api.Test
  void subtractNegative() {
    Vector2D other = new Vector2D(1, 2);
    Vector2D result = vector.subtract(other);
    assertNotEquals(-2, result.getX0(), 0.0001, "The result should not be -2");
    assertNotEquals(-2, result.getX1(), 0.0001, "The result should not be -2");
  }
}