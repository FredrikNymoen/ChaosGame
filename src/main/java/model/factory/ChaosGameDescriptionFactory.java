package model.factory;

import model.chaosgame.ChaosGameDescription;
import java.util.ArrayList;
import java.util.List;
import model.mathcore.Complex;
import model.mathcore.Matrix2x2;
import model.mathcore.Vector2D;
import model.transformations.AffineTransform2D;
import model.transformations.JuliaTransform;
import model.transformations.Transform2D;

/**
 * The ChaosGameDescriptionFactory class is used to create ChaosGameDescription objects for different fractals.
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
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

    List<Transform2D> affineTransforms = createAffineTransforms(
        new Matrix2x2[]{transformationMatrix1, transformationMatrix1, transformationMatrix1},
        new Vector2D[]{transformationVector1, transformationVector2, transformationVector3}
    );

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

    List<Transform2D> affineTransforms = createAffineTransforms(
        new Matrix2x2[]{transformationMatrix1, transformationMatrix2, transformationMatrix3, transformationMatrix4},
        new Vector2D[]{transformationVector1, transformationVector2, transformationVector2, transformationVector3}
    );

    return new ChaosGameDescription(affineTransforms, minCoordsVector, maxCoordsVector);
  }

  /**
   * Creates a ChaosGameDescription object for the maple tree.
   * The maple tree is created by four affine transformations.
   * @param minCoordsVector the minimum coordinates of the canvas
   * @param maxCoordsVector the maximum coordinates of the canvas
   * @return a ChaosGameDescription object for the maple tree
   */
  public ChaosGameDescription mapleTree(Vector2D minCoordsVector, Vector2D maxCoordsVector){
    Matrix2x2 transformationMatrix1 = new Matrix2x2(-0.04, 0, -0.23, -0.65);
    Matrix2x2 transformationMatrix2 = new Matrix2x2(0.61, 0, 0, 0.31);
    Matrix2x2 transformationMatrix3 = new Matrix2x2(0.65, 0.29, 0, 0.48);
    Matrix2x2 transformationMatrix4 = new Matrix2x2(0.64, -0.3, 0.16, 0.56);
    Vector2D transformationVector1 = new Vector2D(-0.08, 0.26);
    Vector2D transformationVector2 = new Vector2D(0.07, 3.5);
    Vector2D transformationVector3 = new Vector2D(0.74, 1.39);
    Vector2D transformationVector4 = new Vector2D(-0.56, 0.60);

    List<Transform2D> affineTransforms = createAffineTransforms(
        new Matrix2x2[]{transformationMatrix1, transformationMatrix2,
            transformationMatrix3, transformationMatrix4},
        new Vector2D[]{transformationVector1, transformationVector2,
            transformationVector3, transformationVector4});

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

  /**
   * Creates a list of affine transformations.
   * @param matrices the list of matrices
   * @param vectors the list of vectors
   * @return a list of affine transformations
   */
  public List<Transform2D> createAffineTransforms(Matrix2x2[] matrices, Vector2D[] vectors) {
    List<Transform2D> affineTransforms = new ArrayList<>();
    for (int i = 0; i < matrices.length; i++) {
      affineTransforms.add(new AffineTransform2D(matrices[i], vectors[i]));
    }
    return affineTransforms;
  }

}
