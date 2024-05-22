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

class ChaosGameFactoryTest {

  private ChaosGameFactory chaosGameFactory;
  ChaosGameDescriptionFactory chaosGameDescriptionFactory;

  @BeforeEach
  public void setUp() {
    chaosGameFactory = new ChaosGameFactory();
    chaosGameDescriptionFactory = new ChaosGameDescriptionFactory();
  }

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
    // Additional assertions based on the expected state of chaosGame after creation
  }

  @Test
  void testCreateJuliaChaosGameWithConvergenceMode() {
    Complex c = new Complex(0.355, 0.355);

    ChaosGame chaosGame = chaosGameFactory.createJuliaChaosGameWithConvergenceMode(c);

    assertNotNull(chaosGame);
    ChaosCanvas canvas = chaosGame.getCanvas();
    assertNotNull(canvas);
    assertEquals(Utility.CHAOS_GAME_WIDTH, canvas.getCanvasArray()[0].length);
    assertEquals(Utility.CHAOS_GAME_HEIGHT, canvas.getCanvasArray().length);
    // Additional assertions based on the expected state of canvas after creation
  }

  @Test
  void testCreateMandelbrotChaosGame() {
    ChaosGame chaosGame = chaosGameFactory.createMandelbrotChaosGame();

    assertNotNull(chaosGame);
    ChaosCanvas canvas = chaosGame.getCanvas();
    assertNotNull(canvas);
  }
}