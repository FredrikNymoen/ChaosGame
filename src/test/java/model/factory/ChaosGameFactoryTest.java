package model.factory;

import model.chaosgame.ChaosCanvas;
import model.chaosgame.ChaosGame;
import model.chaosgame.ChaosGameDescription;
import model.mathcore.Complex;
import model.mathcore.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Utility;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Test class for the ChaosGameFactoryTest class.
 * @author Fredrik Nymoen & Amund Larsen
 * @version v1.0.0
 */

class ChaosGameFactoryTest {

  private ChaosGameFactory chaosGameFactory;
  ChaosGameDescriptionFactory chaosGameDescriptionFactory;

  /**
   * Sets up the ChaosGameFactory and the ChaosGameDescriptionFactory before each test.
   * This method is run before each test.
   */

  @BeforeEach
  public void setUp() {
    chaosGameFactory = new ChaosGameFactory();
    chaosGameDescriptionFactory = new ChaosGameDescriptionFactory();
  }

  /**
   * Tests the createChaosGame method in the ChaosGameFactory class.
   * Verifies that the ChaosGame object is not null and that the canvas is as expected.
   * This method tests the creation of a Sierpinski triangle.
   */

  @Test
  void testCreateChaosGame() {
    int width = 800;
    int height = 600;
    int steps = 1000;
    boolean isBarnsleyTransformation = false;

    ChaosGameDescription description = chaosGameDescriptionFactory.sierpinski(new Vector2D(-1, -1),
        new Vector2D(1, 1));
    ChaosGame chaosGame = chaosGameFactory.createChaosGame(description, width, height, steps,
        isBarnsleyTransformation);

    assertEquals(width, chaosGame.getCanvas().getCanvasArray()[0].length);
    assertEquals(height, chaosGame.getCanvas().getCanvasArray().length);
  }

  /**
   * Tests the createJuliaChaosGame method in the ChaosGameFactory class.
   * Verifies that the ChaosGame object is not null and that the canvas is as expected.
   * This method tests the creation of a Julia set.
   */

  @Test
  void testCreateJuliaChaosGameWithConvergenceMode() {
    Complex c = new Complex(0.355, 0.355);

    ChaosGame chaosGame = chaosGameFactory.createJuliaChaosGameWithConvergenceMode(c);

    assertNotNull(chaosGame);
    ChaosCanvas canvas = chaosGame.getCanvas();
    assertNotNull(canvas);
    assertEquals(Utility.CHAOS_GAME_WIDTH, canvas.getCanvasArray()[0].length);
    assertEquals(Utility.CHAOS_GAME_HEIGHT, canvas.getCanvasArray().length);
  }

  /**
   * Tests the createJuliaChaosGame method in the ChaosGameFactory class.
   * Verifies that the ChaosGame object is not null and that the canvas is as expected.
   * This method tests the creation of a Julia set.
   */

  @Test
  void testCreateMandelbrotChaosGame() {
    ChaosGame chaosGame = chaosGameFactory.createMandelbrotChaosGame();

    assertNotNull(chaosGame);
    ChaosCanvas canvas = chaosGame.getCanvas();
    assertNotNull(canvas);
  }
}