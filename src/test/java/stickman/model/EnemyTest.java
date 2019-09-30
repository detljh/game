package stickman.model;

import org.junit.Before;
import org.junit.Test;
import stickman.collision.CollisionStrategy;
import stickman.collision.EnemyCollisionStrategy;
import stickman.collision.HeroCollisionStrategy;
import stickman.controller.EnemyController;

import static org.junit.Assert.*;

public class EnemyTest {
    private static Enemy moving;
    private static Enemy still;

    @Before
    public void setUp() throws Exception {
        moving = new Enemy(40.0, 300.0, "slimeBa.png", "moving");
        still = new Enemy(40.0, 300.0, "slimeBa.png", "still");
    }

    @Test
    public void getType() {
        assertEquals("moving", moving.getType());
        assertEquals("still", still.getType());
    }

    @Test
    public void getCollisionStrategy() {
        assertTrue(moving.getCollisionStrategy() instanceof EnemyCollisionStrategy);
    }

    @Test
    public void getHeight() {
        assertEquals(20.0, moving.getHeight(), 0.001);
        assertEquals(40.0, still.getHeight(), 0.001);
    }

    @Test
    public void getWidth() {
        assertEquals(20.0, moving.getWidth(), 0.001);
        assertEquals(40.0, still.getWidth(), 0.001);
    }

    @Test
    public void getLayer() {
        assertEquals(Entity.Layer.FOREGROUND, moving.getLayer());
    }

    @Test
    public void getController() {
        assertTrue(moving.getController() instanceof EnemyController);
    }

    @Test
    public void updateImagePath() {
        moving.updateImagePath("slimeBb.png");
        assertEquals("slimeBb.png", moving.getImagePath());
    }

    @Test
    public void getImagePath() {
        assertEquals("slimeBa.png", moving.getImagePath());
    }

    @Test
    public void getXPos() {
        assertEquals(40.0, moving.getXPos(), 0.001);
    }

    @Test
    public void getYPos() {
        assertEquals(300.0 - moving.getHeight(), moving.getYPos(), 0.001);
    }

    @Test
    public void getDesiredX() {
        moving.setDesiredX(10.0);
        assertEquals(10.0, moving.getDesiredX(), 0.001);
    }

    @Test
    public void getDesiredY() {
        moving.setDesiredY(-10.0);
        assertEquals(-10.0, moving.getDesiredY(), 0.001);
    }

    @Test
    public void getXVel() {
        assertEquals(0, moving.getXVel(), 0.001);
    }

    @Test
    public void getYVel() {
        assertEquals(0, moving.getYVel(), 0.001);
    }

    @Test
    public void setDesiredX() {
        moving.setDesiredX(-10.0);
        assertEquals(-10.0, moving.getDesiredX(), 0.001);
    }

    @Test
    public void setDesiredY() {
        moving.setDesiredY(1.0);
        assertEquals(1.0, moving.getDesiredY(), 0.001);
    }

    @Test
    public void setXVel() {
        moving.setXVel(-30.0);
        assertEquals(-30.0, moving.getXVel(), 0.001);
    }

    @Test
    public void setYVel() {
        moving.setYVel(-30.0);
        assertEquals(-30.0, moving.getYVel(), 0.001);
    }

    @Test
    public void setXPos() {
        moving.setXPos(-10);
        assertEquals(0, moving.getXPos(), 0.001);
        moving.setXPos(10);
        assertEquals(10, moving.getXPos(), 0.001);
    }

    @Test
    public void setYPos() {
        moving.setYPos(-10.0);
        assertEquals(-10.0, moving.getYPos(), 0.001);
    }
}