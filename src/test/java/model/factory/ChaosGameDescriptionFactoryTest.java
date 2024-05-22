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

class ChaosGameDescriptionFactoryTest {

private ChaosGameDescriptionFactory factory;
private Vector2D minCoordsVector;
private Vector2D maxCoordsVector;

    @BeforeEach
    public void setUp() {
      factory = new ChaosGameDescriptionFactory();
      minCoordsVector = new Vector2D(-1, -1);
      maxCoordsVector = new Vector2D(1, 1);
    }

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

    @Test
    void testBarnsley() {
      ChaosGameDescription description = factory.barnsley(minCoordsVector, maxCoordsVector);
      assertNotNull(description);
      // Further assertions depend on the behavior of your ChaosGameDescription class
    }

    @Test
    void testMapleTree(){
      ChaosGameDescription description = factory.mapleTree(minCoordsVector, maxCoordsVector);
      assertNotNull(description);
      // Further assertions depend on the behavior of your ChaosGameDescription class
    }

    @Test
    void testJulia() {
      Complex c = new Complex(0.285, 0.01);
      ChaosGameDescription description = factory.julia(minCoordsVector, maxCoordsVector, c);
      assertNotNull(description);
      // Further assertions depend on the behavior of your ChaosGameDescription class
    }

    @Test
    void testAffine() {
      Matrix2x2 matrix = new Matrix2x2(1, 2, 3, 4);
      Vector2D vector = new Vector2D(5, 6);
      List<Matrix2x2> matrices = List.of(matrix);
      List<Vector2D> vectors = List.of(vector);
      ChaosGameDescription description = factory.affine(matrices, vectors, minCoordsVector,
          maxCoordsVector);
      assertNotNull(description);
      // Further assertions depend on the behavior of your ChaosGameDescription class
    }

  }