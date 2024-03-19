import mathcore.Matrix2x2;
import mathcore.Vector2D;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import transformations.AffineTransform2D;
import transformations.JuliaTransform;
import transformations.Transform2D;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the ChaosGameDescription class.
 * This class tests the getTransforms, getMinCoords and getMaxCoords methods of the ChaosGameDescription class.
 */

class ChaosGameDescriptionTest {

    private ChaosGameDescription description;
    private List<Transform2D> transforms;
    private Vector2D minCoords;
    private Vector2D maxCoords;

    /**
     * Sets up the ChaosGameDescription with a list of transformations, min and max coordinates.
     * This method is called before each test.
     */

    @BeforeEach
    void setUp() {
        // Initialize some mock transforms, assuming you have a mock or fake implementation available
        transforms = new ArrayList<>();
        minCoords = new Vector2D(0, 0);
        maxCoords = new Vector2D(10, 10);

        description = new ChaosGameDescription(transforms, minCoords, maxCoords);
    }
    /**
     * Tests the ChaosGameDescription creation with a positive test case.
     * It checks if the transforms returns the correct list of transformations.
     */

    @Test
    void getTransformsPositive() {
        assertEquals(transforms, description.getTransforms(), "getTransforms should return the correct list of transformations");
    }

    /**
     * Tests the getMinCords with a positive test case.
     * It checks if the minCoords returns the correct minimum coordinates.
     */

    @Test
    void getMinCoordsPositive() {
        assertEquals(minCoords, description.getMinCoords(), "getMinCoords should return the correct minimum coordinates");
    }

    /**
     * Tests the getMinCords with a negative test case.
     * It checks if the minCoords returns the correct minimum coordinates.
     */

    @Test
    void getMinCoordsNegative() {
        Vector2D minCoords = new Vector2D(1, 1);
        assertNotEquals(minCoords, description.getMinCoords(), "getMinCoords should return the correct minimum coordinates");
    }

    /**
     * Tests the getMaxCords with a positive test case.
     * It checks if the maxCoords returns the correct maximum coordinates.
     */

    @Test
    void getMaxCoordsPositive() {
        assertEquals(maxCoords, description.getMaxCoords(), "getMaxCoords should return the correct maximum coordinates");
    }

    /**
     * Tests the getMaxCords with a negative test case.
     * It checks if the maxCoords returns the correct maximum coordinates.
     */

    @Test
    void getMaxCoordsNegative() {
        Vector2D maxCoords = new Vector2D(1, 1);
        assertNotEquals(maxCoords, description.getMaxCoords(), "getMaxCoords should return the correct maximum coordinates");
    }
}