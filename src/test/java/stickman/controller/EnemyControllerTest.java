package stickman.controller;

import org.junit.Before;
import org.junit.Test;
import stickman.ReadConfiguration;
import stickman.model.Enemy;
import stickman.model.GameEngineImpl;
import stickman.model.LevelImpl;

import static org.junit.Assert.*;

public class EnemyControllerTest {
    private Enemy enemy;
    private GameEngineImpl model;
    private LevelImpl level;

    @Before
    public void setUp() throws Exception {
        enemy = new Enemy(40.0, 300.0, "slimeBa.png", "moving");
        ReadConfiguration reader = new ReadConfiguration("test.json");
        model = new GameEngineImpl(reader.getConfig());
        level = (LevelImpl) model.getCurrentLevel();
        enemy.getController().setLevel(level);
    }

    @Test
    public void jump() {
        assertFalse(enemy.getController().jump());
    }

    @Test
    public void moveLeft() {
        assertTrue(enemy.getController().moveLeft());
        assertEquals(enemy.getXPos() + (enemy.getXVel() / GameEngineImpl.FPS), enemy.getDesiredX(), 0.001);
    }

    @Test
    public void moveRight() {
        assertTrue(enemy.getController().moveRight());
        assertEquals(enemy.getXPos() + (enemy.getXVel() / GameEngineImpl.FPS), enemy.getDesiredX(), 0.001);
    }

    @Test
    public void stopMoving() {
        assertTrue(enemy.getController().stopMoving());
        assertEquals(0, enemy.getXVel(), 0.0);
    }

    @Test
    public void move() {
        enemy.getController().move();
        assertEquals(enemy.getDesiredX(), enemy.getXPos(), 0.001);
        assertEquals(enemy.getDesiredY(), enemy.getYPos(), 0.001);
    }

    @Test
    public void tick() {
        int tick = GameEngineImpl.FPS;
        while (tick > GameEngineImpl.FPS * 0.5) {
            tick--;
            assertEquals("slimeBa.png", enemy.getImagePath());
            enemy.getController().tick();
        }

        assertEquals("slimeBb.png", enemy.getImagePath());

        tick = GameEngineImpl.FPS;
        while (tick > GameEngineImpl.FPS * 0.5) {
            tick--;
            assertEquals("slimeBb.png", enemy.getImagePath());
            enemy.getController().tick();
        }

        assertEquals("slimeBa.png", enemy.getImagePath());
    }
}