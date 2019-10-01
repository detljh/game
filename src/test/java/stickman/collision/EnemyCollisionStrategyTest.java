package stickman.collision;

import org.junit.Before;
import org.junit.Test;
import stickman.ReadConfiguration;
import stickman.model.*;

import static org.junit.Assert.*;

public class EnemyCollisionStrategyTest {
    private static EnemyCollisionStrategy strategy;
    private static Enemy e;
    private GameEngineImpl model;
    private LevelImpl level;

    @Before
    public void setUp() throws Exception {
        ReadConfiguration reader = new ReadConfiguration("test.json");
        model = new GameEngineImpl(reader.getConfig());
        level = (LevelImpl) model.getCurrentLevel();
        e = createEnemy(70.0, 300.0);
        e.getController().setLevel(level);
        strategy = (EnemyCollisionStrategy) e.getCollisionStrategy();
    }

    private static Hero createHero(double xPos) {
        return new Hero("ch_stand1.png", xPos, "normal", 100.0, 300.0);
    }

    private static Enemy createEnemy(double xPos, double yPos) {
        return new Enemy(xPos, yPos, "slimeBa.png", "moving");
    }

    @Test
    public void handleCollision() {
        assertFalse(strategy.checkCollision(e, level.getFinish()));
        assertNull(strategy.handleCollision(e, createEnemy(80.0, 300.0)));
        assertNull(strategy.handleCollision(e, createHero(80.0)));

        // make platform +20 enemy to be on same y pos
        Platform platform = new Platform(60.0, 320.0, "normal", "foot_tile.png");
        assertTrue(strategy.checkCollision(e, platform));
        strategy.handleCollision(e, platform);
        assertEquals(0.0, e.getXVel(), 0.001);
        assertEquals(platform.getXPos() + platform.getWidth(), e.getDesiredX(), 0.001);

        platform = new Platform(85.0, 320.0, "normal", "foot_tile.png");
        assertTrue(strategy.checkCollision(e, platform));
        strategy.handleCollision(e, platform);
        assertEquals(0.0, e.getXVel(), 0.001);
        assertEquals(platform.getXPos() - e.getWidth(), e.getDesiredX(), 0.001);

        platform = new Platform(70.0, 335.0, "normal", "foot_tile.png");
        assertTrue(strategy.checkCollision(e, platform));
        strategy.handleCollision(e, platform);
        assertEquals(0.0, e.getYVel(), 0.001);
        assertEquals(platform.getYPos() - e.getHeight(), e.getDesiredY(), 0.001);

        platform = new Platform(70.0, 300.0, "normal", "foot_tile.png");
        assertTrue(strategy.checkCollision(e, platform));
        strategy.handleCollision(e, platform);
        assertEquals(0.0, e.getYVel(), 0.001);
        assertEquals(platform.getYPos() + platform.getHeight(), e.getDesiredY(), 0.001);
    }
}
