package stickman.controller;

import org.junit.Before;
import org.junit.Test;
import stickman.ReadConfiguration;
import stickman.model.GameEngineImpl;
import stickman.model.Hero;
import stickman.model.LevelImpl;

import static org.junit.Assert.*;

public class HeroControllerTest {
    private static Hero hero;
    private static HeroController hc;
    private GameEngineImpl model;
    private LevelImpl level;

    @Before
    public void setUp() throws Exception {
        ReadConfiguration reader = new ReadConfiguration("test.json");
        model = new GameEngineImpl(reader.getConfig());
        level = (LevelImpl) model.getCurrentLevel();
        hero = new Hero("ch_stand1.png", 40.0, "normal", 100.0, 300.0);
        hc = (HeroController) hero.getController();
    }

    @Test
    public void jump() {
        hc.setJump(false);
        hc.setOnFloor(false);
        assertFalse(hc.jump());

        hc.setOnFloor(true);
        double jumpForce = hero.getJumpForce() / GameEngineImpl.FPS;
        double yVel = hero.getYVel() / (GameEngineImpl.FPS / 15);
        double beforeJumpY = hero.getDesiredY();

        assertTrue(hc.jump());
        assertEquals(yVel + jumpForce, hero.getYVel(), 0.001);
        assertEquals(beforeJumpY - hero.getYVel(), hero.getDesiredY(), 0.001);

        double maxJumpHeight = beforeJumpY - hero.getJumpHeight();
        hc.setOnFloor(false);
        while (hero.getDesiredY() > maxJumpHeight) {
            assertFalse(String.format("Max jump height: %f\nCurrent height: %f\n", maxJumpHeight, hero.getDesiredY()), hc.jump());
        }
    }

    @Test
    public void moveLeft() {
        hc.moveRight();
        assertTrue(hero.getXVel() > 0);
        double xVel = hero.getXVel() / GameEngineImpl.FPS;
        double initialXPos = hero.getXPos();
        double hozMov = hero.getHorizontalMovement() / GameEngineImpl.FPS;

        hc.moveLeft();
        assertTrue(hero.getXVel() < 0);
        assertEquals(-xVel - hozMov, hero.getXVel(), 0.001);
        assertEquals(initialXPos + hero.getXVel(), hero.getDesiredX(), 0.001);
    }

    @Test
    public void moveRight() {
        double xVel = hero.getXVel() / GameEngineImpl.FPS;
        double initialXPos = hero.getXPos();
        double hozMov = hero.getHorizontalMovement() / GameEngineImpl.FPS;

        hc.moveRight();
        assertTrue(hero.getXVel() > 0);
        assertEquals(xVel + hozMov, hero.getXVel(), 0.001);
        assertEquals(initialXPos + hero.getXVel(), hero.getDesiredX(), 0.001);
    }

    @Test
    public void stopMoving() {
        hc.stopMoving();
        assertTrue(hero.getXVel() == 0);
    }

    @Test
    public void move() {
        assertEquals(hero.getXPos(), hero.getDesiredX(), 0.001);
        assertEquals(hero.getYPos(), hero.getDesiredY(), 0.001);
    }

    @Test
    public void restartHero() {
        hc.restartHero();
        assertEquals(hero.getInitialX(), hero.getDesiredX(), 0.001);
        assertEquals(hero.getInitialY(), hero.getDesiredY(), 0.001);
    }

    @Test
    public void platformMovement() {
        hc.moveLeft();
        double xVel = hero.getXVel();
        double initialXPos = hero.getXPos();
        hc.platformMovement("icy");
        assertEquals(initialXPos + xVel - 0.2, hero.getDesiredX(), 0.001);

        hc.moveRight();
        xVel = hero.getXVel();
        initialXPos = hero.getXPos();
        hc.platformMovement("icy");
        assertEquals(initialXPos + xVel + 0.2, hero.getDesiredX(), 0.001);
    }

    @Test
    public void tick() {
        int tick = (int) (GameEngineImpl.FPS * 0.75);
        level.getHero().getController().moveLeft();
        while (tick > (GameEngineImpl.FPS * 0.65)) {
            tick--;
            assertEquals("ch_stand1.png", level.getHero().getImagePath());
            level.getHero().getController().tick();
        }
        assertEquals("ch_walk5.png", level.getHero().getImagePath());

        tick = (int) (GameEngineImpl.FPS * 0.75);
        level.getHero().getController().stopMoving();
        while (tick > 0) {
            tick--;
            assertEquals("ch_walk5.png", level.getHero().getImagePath());
            level.getHero().getController().tick();
        }
        assertEquals("ch_stand4.png", level.getHero().getImagePath());

        tick = (int) (GameEngineImpl.FPS * 0.75);
        level.getHero().getController().moveRight();
        while (tick > (GameEngineImpl.FPS * 0.65)) {
            tick--;
            assertEquals("ch_stand4.png", level.getHero().getImagePath());
            level.getHero().getController().tick();
        }
        assertEquals("ch_walk2.png", level.getHero().getImagePath());

        tick = (int) (GameEngineImpl.FPS * 0.75);
        level.getHero().getController().stopMoving();
        while (tick > 0) {
            tick--;
            assertEquals("ch_walk2.png", level.getHero().getImagePath());
            level.getHero().getController().tick();
        }
        assertEquals("ch_stand2.png", level.getHero().getImagePath());



        assertTrue(level.getHero().getController().jump());
        double jumpForce = level.getHero().getJumpForce() / GameEngineImpl.FPS;
        double yVel = level.getHero().getYVel() / (GameEngineImpl.FPS / 15);
        double beforeJumpY = level.getHero().getDesiredY();

        level.getHero().getController().tick();
        assertEquals(yVel + jumpForce, level.getHero().getYVel(), 0.001);
        assertEquals(beforeJumpY - level.getHero().getYVel(), level.getHero().getDesiredY(), 0.001);
    }
}