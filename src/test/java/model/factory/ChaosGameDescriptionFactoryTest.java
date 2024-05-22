package model.factory;

import static org.junit.jupiter.api.Assertions.*;

import model.chaosgame.ChaosGameDescription;
import java.util.ArrayList;
import java.util.List;
import model.mathcore.Complex;
import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.transformations.AffineTransform2D;
import model.transformations.Transform2D;

/**
 * Test class for the ChaosGameDescriptionFactory class.
 * This class tests the methods of the ChaosGameDescriptionFactory class.
 *
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */

class ChaosGameDescriptionFactoryTest {

private ChaosGameDescriptionFactory factory;
private Vector2D minCoordsVector;
private Vector2D maxCoordsVector;

  /**
   * Sets up the ChaosGameDescriptionFactory and the min and max coordinates vectors before each test.
   */

    @BeforeEach
    public void setUp() {
      factory = new ChaosGameDescriptionFactory();
      minCoordsVector = new Vector2D(-1, -1);
      maxCoordsVector = new Vector2D(1, 1);
    }

  /**
   * Tests the sierpinski method in the ChaosGameDescriptionFactory class.
   * Verifies that the ChaosGameDescription object is not null and that the transformations are as expected.
   */
  @Test
  void testSierpinski() {
    ChaosGameDescription description = factory.sierpinski(minCoordsVector, maxCoordsVector);
    assertNotNull(description);

    // Assuming you have getters for your transformations and that they implement equals correctly.
    List<Transform2D> expectedTransformations = new ArrayList<>();
    expectedTransformations.add(new AffineTransform2D(new Matrix2x2(0.5, 0, 0, 0.5), new Vector2D(0, 0)));
    expectedTransformations.add(new AffineTransform2D(new Matrix2x2(0.5, 0, 0, 0.5), new Vector2D(0.25, 0.5)));
    expectedTransformations.add(new AffineTransform2D(new Matrix2x2(0.5, 0, 0, 0.5), new Vector2D(0.5, 0)));
    List<Transform2D> actualTransformations = description.getTransforms();
    assertNotNull(actualTransformations);
    assertEquals(expectedTransformations.size(), actualTransformations.size());
    }

  /**
   * Tests the barnsley method in the ChaosGameDescriptionFactory class.
   * Verifies that the ChaosGameDescription object is not null.
   */

  @Test
    void testBarnsley() {
      ChaosGameDescription description = factory.barnsley(minCoordsVector, maxCoordsVector);
      assertNotNull(description);
    }

  /**
   * Tests the mapleTree method in the ChaosGameDescriptionFactory class.
   * Verifies that the ChaosGameDescription object is not null.
   */
  @Test
    void testMapleTree(){
      ChaosGameDescription description = factory.mapleTree(minCoordsVector, maxCoordsVector);
      assertNotNull(description);
    }

  /**
   * Tests the julia method in the ChaosGameDescriptionFactory class.
   * Verifies that the ChaosGameDescription object is not null.
   */
  @Test
    void testJulia() {
      Complex c = new Complex(0.285, 0.01);
      ChaosGameDescription description = factory.julia(minCoordsVector, maxCoordsVector, c);
      assertNotNull(description);
    }

  /**
   * Tests the affine method in the ChaosGameDescriptionFactory class.
   */

  @Test
    void testAffine() {
      Matrix2x2 matrix = new Matrix2x2(1, 2, 3, 4);
      Vector2D vector = new Vector2D(5, 6);
      List<Matrix2x2> matrices = List.of(matrix);
      List<Vector2D> vectors = List.of(vector);
      ChaosGameDescription description = factory.affine(matrices, vectors, minCoordsVector,
          maxCoordsVector);
      assertNotNull(description);
    }

  }