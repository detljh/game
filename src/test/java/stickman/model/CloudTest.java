package stickman.model;

import org.junit.Before;
import org.junit.Test;
import stickman.collision.CollisionStrategy;
import stickman.controller.CloudController;

import static org.junit.Assert.*;

public class CloudTest {
    private static Cloud cloud;

    @Before
    public void setUp() throws Exception {
        cloud = new Cloud("cloud_1.png", 50.0, 100.0, 3.2);
    }

    @Test
    public void setXPos() {
        cloud.setXPos(400.0);
        assertEquals(400.0, cloud.getXPos(), 0.001);
    }

    @Test
    public void getHeight() {
        assertEquals(0, cloud.getHeight(), 0.001);
    }

    @Test
    public void getWidth() {
        assertEquals(0, cloud.getWidth(), 0.001);
    }

    @Test
    public void getLayer() {
        assertEquals(Entity.Layer.BACKGROUND, cloud.getLayer());
    }

    @Test
    public void getCollisionStrategy() {
        assertTrue(cloud.getCollisionStrategy() != null);
    }

    @Test
    public void getController() {
        assertTrue(cloud.getController() instanceof CloudController);
    }

    @Test
    public void updateImagePath() {
        cloud.updateImagePath("cloud_2.png");
        assertEquals("cloud_2.png", cloud.getImagePath());
    }

    @Test
    public void getImagePath() {
        assertEquals("cloud_1.png", cloud.getImagePath());
    }

    @Test
    public void getXPos() {
        assertEquals(50.0, cloud.getXPos(), 0.001);
    }

    @Test
    public void getYPos() {
        assertEquals(100.0, cloud.getYPos(), 0.001);
    }

    @Test
    public void getDesiredX() {
        cloud.setDesiredX(410.0);
        assertEquals(410.0, cloud.getDesiredX(), 0.001);
    }

    @Test
    public void getDesiredY() {
        cloud.setDesiredY(410.0);
        assertEquals(410.0, cloud.getDesiredY(), 0.001);
    }

    @Test
    public void getXVel() {
        assertEquals(3.2, cloud.getXVel(), 0.001);
    }

    @Test
    public void getYVel() {
        assertEquals(0, cloud.getYVel(), 0.001);
    }

    @Test
    public void setDesiredX() {
        cloud.setDesiredX(10.0);
        assertEquals(10.0, cloud.getDesiredX(), 0.001);
    }

    @Test
    public void setDesiredY() {
        cloud.setDesiredY(-1.0);
        assertEquals(-1.0, cloud.getDesiredY(), 0.001);
    }

    @Test
    public void setXVel() {
        cloud.setXVel(-30.0);
        assertEquals(-30.0, cloud.getXVel(), 0.001);
    }

    @Test
    public void setYVel() {
        cloud.setYVel(-30.0);
        assertEquals(-30.0, cloud.getYVel(), 0.001);
    }

    @Test
    public void setYPos() {
        cloud.setYPos(-10.0);
        assertEquals(-10.0, cloud.getYPos(), 0.001);
    }
}