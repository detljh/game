package stickman.model;

import org.junit.Before;
import org.junit.Test;
import stickman.collision.HeroCollisionStrategy;
import stickman.controller.HeroController;

import static org.junit.Assert.*;

public class HeroTest {
    private Hero hero;

    @Before
    public void setUp() throws Exception {
        hero = new Hero("ch_stand1.png", 40.0, "normal", 100.0, 300.0);
    }

    private static Hero createHero(double xPos, String size) {
        return new Hero("ch_stand1.png", xPos, size, 100.0, 300.0);
    }

    @Test
    public void getInitialX() {
        assertEquals(40.0, hero.getInitialX(), 0.001);
    }

    @Test
    public void getInitialY() {
        assertEquals(300.0 - hero.getHeight(), hero.getInitialY(), 0.001);
    }

    @Test
    public void getJumpForce() {
        assertEquals(hero.getJumpHeight() / 2, hero.getJumpForce(), 0.001);
    }

    @Test
    public void getHorizontalMovement() {
        assertEquals(150.0, hero.getHorizontalMovement(), 0.001);
    }

    @Test
    public void getJumpHeight() {
        assertEquals(100.0, hero.getJumpHeight(), 0.001);
    }

    @Test
    public void getCollisionStrategy() {
        assertTrue(hero.getCollisionStrategy() instanceof HeroCollisionStrategy);
    }

    @Test
    public void getHeight() {
        assertEquals(20.0, createHero(40.0, "tiny").getHeight(), 0.001);
        assertEquals(30.0, hero.getHeight(), 0.001);
        assertEquals(50.0, createHero(40.0, "large").getHeight(), 0.001);
        assertEquals(100.0, createHero(40.0, "giant").getHeight(), 0.001);
        assertEquals(0.0, createHero(40.0, "invalid").getHeight(), 0.001);
    }

    @Test
    public void getWidth() {
        assertEquals(20.0, createHero(40.0, "tiny").getWidth(), 0.001);
        assertEquals(30.0, hero.getWidth(), 0.001);
        assertEquals(50.0, createHero(40.0, "large").getWidth(), 0.001);
        assertEquals(100.0, createHero(40.0, "giant").getWidth(), 0.001);
        assertEquals(0.0, createHero(40.0, "invalid").getWidth(), 0.001);
    }

    @Test
    public void getLayer() {
        assertEquals(Entity.Layer.FOREGROUND, hero.getLayer());
    }

    @Test
    public void getController() {
        assertTrue(hero.getController() instanceof HeroController);
    }

    @Test
    public void decrementLives() {
        assertEquals(3, hero.getRemainingLives());
        hero.decrementLives();
        assertEquals(2, hero.getRemainingLives());
    }

    @Test
    public void getRemainingLives() {
        assertEquals(3, hero.getRemainingLives());
    }

    @Test
    public void updateImagePath() {
        hero.updateImagePath("ch_stand2.png");
        assertEquals("ch_stand2.png", hero.getImagePath());
    }

    @Test
    public void getImagePath() {
        assertEquals("ch_stand1.png", hero.getImagePath());
    }

    @Test
    public void getXPos() {
        assertEquals(40.0, hero.getXPos(), 0.001);
    }

    @Test
    public void getYPos() {
        assertEquals(300.0 - hero.getHeight(), hero.getYPos(), 0.001);
    }

    @Test
    public void getDesiredX() {
        hero.setDesiredX(10.0);
        assertEquals(10.0, hero.getDesiredX(), 0.001);
    }

    @Test
    public void getDesiredY() {
        hero.setDesiredY(-10.0);
        assertEquals(-10.0, hero.getDesiredY(), 0.001);
    }

    @Test
    public void getXVel() {
        assertEquals(0, hero.getXVel(), 0.001);
    }

    @Test
    public void getYVel() {
        assertEquals(0, hero.getYVel(), 0.001);
    }

    @Test
    public void setDesiredX() {
        hero.setDesiredX(-10.0);
        assertEquals(-10.0, hero.getDesiredX(), 0.001);
    }

    @Test
    public void setDesiredY() {
        hero.setDesiredY(1.0);
        assertEquals(1.0, hero.getDesiredY(), 0.001);
    }

    @Test
    public void setXVel() {
        hero.setXVel(-30.0);
        assertEquals(-30.0, hero.getXVel(), 0.001);
    }

    @Test
    public void setYVel() {
        hero.setYVel(-30.0);
        assertEquals(-30.0, hero.getYVel(), 0.001);
    }

    @Test
    public void setXPos() {
        hero.setXPos(-10);
        assertEquals(0, hero.getXPos(), 0.001);
        hero.setXPos(10);
        assertEquals(10, hero.getXPos(), 0.001);
    }

    @Test
    public void setYPos() {
        hero.setYPos(-10.0);
        assertEquals(-10.0, hero.getYPos(), 0.001);
    }
}