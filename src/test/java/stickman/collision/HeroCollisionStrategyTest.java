package stickman.collision;

import org.junit.Before;
import org.junit.Test;
import stickman.ReadConfiguration;
import stickman.controller.HeroController;
import stickman.model.*;

import java.util.List;

import static org.junit.Assert.*;

public class HeroCollisionStrategyTest {
    private static HeroCollisionStrategy strategy;
    private static Hero hero;
    private static HeroController hc;
    private GameEngineImpl model;
    private LevelImpl level;

    @Before
    public void setUp() throws Exception {
        ReadConfiguration reader = new ReadConfiguration("test.json");
        model = new GameEngineImpl(reader.getConfig());
        level = (LevelImpl) model.getCurrentLevel();
        hero = createHero(60.0);
        hc = (HeroController) hero.getController();
        hc.setLevel(level);
        strategy = (HeroCollisionStrategy) hero.getCollisionStrategy();
    }

    private static Hero createHero(double xPos) {
        return new Hero("ch_stand1.png", xPos, "normal", 100.0, 300.0);
    }

    private static Enemy createEnemy(double xPos, double yPos) {
        return new Enemy(xPos, yPos, "slimeBa.png", "still");
    }

    @Test
    public void checkCollision() {
        assertFalse(strategy.checkCollision(hero, new Cloud("cloud_1.png", 0.0, 0.0, 3.2)));
        assertFalse(strategy.checkCollision(hero, hero));
        assertTrue(strategy.checkCollision(hero, createHero(70.0)));
        assertFalse(strategy.checkCollision(hero, createHero(75.0)));
        assertTrue(strategy.checkCollision(hero, createHero(74.0)));
        assertFalse(strategy.checkCollision(hero, createHero(120.0)));

        hero.setDesiredY(299);
        assertTrue(strategy.checkCollision(hero, createHero(60.0)));
        hero.setDesiredY(241);
        assertTrue(strategy.checkCollision(hero, createHero(60.0)));
        hero.setDesiredX(90.0);
        assertFalse(strategy.checkCollision(hero, createHero(60.0)));
        hero.setDesiredX(30.0);
        assertFalse(strategy.checkCollision(hero, createHero(60.0)));
    }

    @Test
    public void handleCollision() {
        assertEquals("won", strategy.handleCollision(hero, level.getFinish()));

        double width = hero.getWidth() / 2;

        // to make platform on same yPos as hero, make +20
        Platform platform = new Platform(50.0, hero.getYPos() + 20, "normal", "foot_tile.png");
        assertTrue(strategy.checkCollision(hero, platform));
        strategy.handleCollision(hero, platform);
        assertEquals(0.0, hero.getXVel(), 0.001);
        assertEquals(platform.getXPos() + platform.getWidth(), hero.getDesiredX(), 0.001);

        platform = new Platform(80.0, hero.getYPos() + 20, "normal", "foot_tile.png");
        assertTrue(strategy.checkCollision(hero, platform));
        strategy.handleCollision(hero, platform);
        assertEquals(0.0, hero.getXVel(), 0.001);
        assertEquals(platform.getXPos() - width, hero.getDesiredX(), 0.001);

        platform = new Platform(60.0, hero.getYPos() + 20 + 15, "normal", "foot_tile.png");
        assertTrue(strategy.checkCollision(hero, platform));
        strategy.handleCollision(hero, platform);
        assertEquals(0.0, hero.getYVel(), 0.001);
        assertEquals(platform.getYPos() - hero.getHeight(), hero.getDesiredY(), 0.001);

        platform = new Platform(60.0, hero.getYPos() + 20 - 25, "normal", "foot_tile.png");
        assertTrue(strategy.checkCollision(hero, platform));
        strategy.handleCollision(hero, platform);
        assertEquals(0.0, hero.getYVel(), 0.001);
        assertEquals(platform.getYPos() + platform.getHeight(), hero.getDesiredY(), 0.001);
    }

    @Test
    public void handleDeath() {
        // death by running into enemy on the x axis
        assertNull(strategy.handleCollision(hero, createEnemy(10.0, 300.0)));
        assertEquals(2, hero.getRemainingLives());
        assertEquals(hero.getInitialX(), hero.getXPos(), 0.001);
        assertEquals(hero.getInitialY(), hero.getYPos(), 0.001);
        assertNull(strategy.handleCollision(hero, createEnemy(10.0, 300.0)));
        assertEquals(1, hero.getRemainingLives());
        assertEquals(hero.getInitialX(), hero.getXPos(), 0.001);
        assertEquals(hero.getInitialY(), hero.getYPos(), 0.001);
        assertEquals("lost", strategy.handleCollision(hero, createEnemy(10.0, 300.0)));
        assertEquals(0, hero.getRemainingLives());

        // underneath the enemy
        hero = createHero(40.0);
        hc = (HeroController) hero.getController();
        hc.setLevel(level);
        strategy = new HeroCollisionStrategy(hc);
        assertTrue(strategy.checkCollision(hero, createEnemy(40.0, 271.0)));
        assertNull(strategy.handleCollision(hero, createEnemy(40.0, 271.0)));
        assertEquals(2, hero.getRemainingLives());
        assertEquals(hero.getInitialX(), hero.getXPos(), 0.001);
        assertEquals(hero.getInitialY(), hero.getYPos(), 0.001);
        assertNull(strategy.handleCollision(hero, createEnemy(10.0, 271)));
        assertEquals(1, hero.getRemainingLives());
        assertEquals(hero.getInitialX(), hero.getXPos(), 0.001);
        assertEquals(hero.getInitialY(), hero.getYPos(), 0.001);
        assertEquals("lost", strategy.handleCollision(hero, createEnemy(10.0, 271)));
        assertEquals(0, hero.getRemainingLives());
    }

    @Test
    public void handleKill() {
        List<Entity> entities = level.getEntities();
        Enemy e;
        for (Entity entity : entities) {
            if (entity instanceof Enemy) {
                e = (Enemy) entity;
                hero.setDesiredX(e.getXPos());
                hero.setDesiredY(e.getYPos() - hero.getHeight() + 1);
                assertTrue(strategy.checkCollision(hero, e));
                assertNull(strategy.handleCollision(hero, e));
                break;
            }
        }
    }

    @Test
    public void handlePlatformMovement() {
        List<Entity> entities = level.getEntities();
        int initialSize = entities.size();
        Platform p;
        for (Entity entity : entities) {
            if (entity instanceof Platform) {
                p = (Platform) entity;
                if (p.getType().equals("icy")) {
                    hc.moveLeft();
                    hero.setDesiredX(p.getXPos());
                    hero.setDesiredY(p.getYPos() - hero.getHeight() + 1);
                    assertTrue(strategy.checkCollision(hero, p));
                    assertNull(strategy.handleCollision(hero, p));
                    assertEquals(hero.getXPos() + hero.getXVel() - 0.2, hero.getDesiredX(), 0.001);

                    hc.moveRight();
                    hero.setDesiredX(p.getXPos());
                    hero.setDesiredY(p.getYPos() - hero.getHeight() + 1);
                    assertTrue(strategy.checkCollision(hero, p));
                    assertNull(strategy.handleCollision(hero, p));
                    assertEquals(hero.getXPos() + hero.getXVel() + 0.2, hero.getDesiredX(), 0.001);
                    break;
                }
            }
        }
    }
}