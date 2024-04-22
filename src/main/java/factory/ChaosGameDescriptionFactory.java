package factory;

import chaosGame.ChaosGameDescription;
import java.util.ArrayList;
import java.util.List;
import mathcore.Complex;
import mathcore.Matrix2x2;
import mathcore.Vector2D;
import transformations.AffineTransform2D;
import transformations.JuliaTransform;
import transformations.Transform2D;

/**
 * ChaosGameDescriptionFactory class is used to create ChaosGameDescription objects.
 * The class contains methods to create ChaosGameDescription objects for the Sierpinski triangle,
 * Barnsley fern, Julia set and affine transformations.
 */

public class ChaosGameDescriptionFactory {

  /**
   * Creates a ChaosGameDescription object for the Sierpinski triangle.
   * The Sierpinski triangle is created by three affine transformations.
   * @param minCoordsVector the minimum coordinates of the canvas
   * @param maxCoordsVector the maximum coordinates of the canvas
   * @return a ChaosGameDescription object for the Sierpinski triangle
   */

  public ChaosGameDescription sierpinski(Vector2D minCoordsVector, Vector2D maxCoordsVector){
    Matrix2x2 transformationMatrix1 = new Matrix2x2(0.5, 0, 0, 0.5);
    Vector2D transformationVector1 = new Vector2D(0, 0);
    Vector2D transformationVector2 = new Vector2D(0.25, 0.5);
    Vector2D transformationVector3 = new Vector2D(0.5, 0);

    AffineTransform2D transformation1 = new AffineTransform2D(transformationMatrix1, transformationVector1);
    AffineTransform2D transformation2 = new AffineTransform2D(transformationMatrix1, transformationVector2);
    AffineTransform2D transformation3 = new AffineTransform2D(transformationMatrix1, transformationVector3);
    List<Transform2D> affineTransforms = new ArrayList<>();
    affineTransforms.add(transformation1);
    affineTransforms.add(transformation2);
    affineTransforms.add(transformation3);
    return new ChaosGameDescription(affineTransforms, minCoordsVector, maxCoordsVector);
  }

  /**
   * Creates a ChaosGameDescription object for the Barnsley fern.
   * The Barnsley fern is created by four affine transformations.
   * @param minCoordsVector the minimum coordinates of the canvas
   * @param maxCoordsVector the maximum coordinates of the canvas
   * @return a ChaosGameDescription object for the Barnsley fern
   */

  public ChaosGameDescription barnsley(Vector2D minCoordsVector, Vector2D maxCoordsVector){
    Matrix2x2 transformationMatrix1 = new Matrix2x2(0, 0, 0, 0.16);
    Matrix2x2 transformationMatrix2 = new Matrix2x2(0.85, 0.04, -0.04, 0.85);
    Matrix2x2 transformationMatrix3 = new Matrix2x2(0.2, -0.26, 0.23, 0.22);
    Matrix2x2 transformationMatrix4 = new Matrix2x2(-0.15, 0.28, 0.26, 0.24);
    Vector2D transformationVector1 = new Vector2D(0, 0);
    Vector2D transformationVector2 = new Vector2D(0, 1.6);
    Vector2D transformationVector3 = new Vector2D(0, 0.44);

    AffineTransform2D transformation1 = new AffineTransform2D(transformationMatrix1,
        transformationVector1);
    AffineTransform2D transformation2 = new AffineTransform2D(transformationMatrix2,
        transformationVector2);
    AffineTransform2D transformation3 = new AffineTransform2D(transformationMatrix3,
        transformationVector2);
    AffineTransform2D transformation4 = new AffineTransform2D(transformationMatrix4,
        transformationVector3);
    List<Transform2D> affineTransforms = new ArrayList<>();
    affineTransforms.add(transformation1);
    affineTransforms.add(transformation2);
    affineTransforms.add(transformation3);
    affineTransforms.add(transformation4);
    return new ChaosGameDescription(affineTransforms, minCoordsVector, maxCoordsVector);
  }

  /**
   * Creates a ChaosGameDescription object for the Julia set.
   * The Julia set is created by two Julia transformations.
   * @param minCoordsVector the minimum coordinates of the canvas
   * @param maxCoordsVector the maximum coordinates of the canvas
   * @param c the complex number c
   * @return a ChaosGameDescription object for the Julia set
   */

  public ChaosGameDescription julia(Vector2D minCoordsVector, Vector2D maxCoordsVector, Complex c){
    JuliaTransform transformation1 = new JuliaTransform(c, 0);
    JuliaTransform transformation2 = new JuliaTransform(c, 1);
    JuliaTransform transformation3 = new JuliaTransform(c, 2);
    JuliaTransform transformation4 = new JuliaTransform(c, 3);
    List<Transform2D> juliaTransforms = new ArrayList<>();
    juliaTransforms.add(transformation1);
    juliaTransforms.add(transformation2);
    juliaTransforms.add(transformation3);
    juliaTransforms.add(transformation4);
    return new ChaosGameDescription(juliaTransforms, minCoordsVector, maxCoordsVector);
  }

  /**
   * Creates a ChaosGameDescription object for affine transformations.
   * The affine transformations are created by a list of matrices and a list of vectors.
   * @param matrices the list of matrices
   * @param vectors the list of vectors
   * @param minCoordsVector the minimum coordinates of the canvas
   * @param maxCoordsVector the maximum coordinates of the canvas
   * @return a ChaosGameDescription object for affine transformations
   */

  public ChaosGameDescription affine(List<Matrix2x2> matrices, List<Vector2D> vectors, Vector2D minCoordsVector, Vector2D maxCoordsVector){
    List<Transform2D> affineTransforms = new ArrayList<>();
    for (int i = 0; i < matrices.size(); i++) {
      affineTransforms.add(new AffineTransform2D(matrices.get(i), vectors.get(i)));
    }
    return new ChaosGameDescription(affineTransforms, minCoordsVector, maxCoordsVector);
  }


}
