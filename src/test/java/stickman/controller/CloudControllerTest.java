package stickman.controller;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import stickman.model.Cloud;
import stickman.model.GameEngineImpl;

import static org.junit.Assert.*;

public class CloudControllerTest {
    private static Cloud cloud;

    @Before
    public void setUp() throws Exception {
        cloud = new Cloud("cloud_1.png", 50.0, 100.0, 3.2);
    }

    @Test
    public void jump() {
        assertFalse(cloud.getController().jump());
    }

    @Test
    public void moveLeft() {
        assertFalse(cloud.getController().moveLeft());
    }

    @Test
    public void moveRight() {
        assertTrue(cloud.getController().moveRight());
        double expected = cloud.getXPos() + (cloud.getXVel() / GameEngineImpl.FPS);
        assertEquals(expected, cloud.getDesiredX(), 0.001);
    }

    @Test
    public void stopMoving() {
        assertFalse(cloud.getController().stopMoving());
    }

    @Test
    public void tick() {
        cloud.getController().tick();
        assertEquals(cloud.getYPos(), cloud.getDesiredY(), 0.001);
    }

    @Test
    public void move() {
        cloud.getController().move();
        assertEquals(cloud.getDesiredY(), cloud.getYPos(), 0.001);
        assertEquals(cloud.getDesiredX(), cloud.getXPos(), 0.001);
    }
}
