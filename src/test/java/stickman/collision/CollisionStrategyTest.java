package stickman.collision;

import org.junit.Before;
import org.junit.Test;
import stickman.model.Enemy;
import stickman.model.Hero;

import static org.junit.Assert.*;

public class CollisionStrategyTest {
    private static CollisionStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new CollisionStrategy();
    }

    private static Hero createHero(double xPos) {
        return new Hero("ch_stand1.png", xPos, "normal", 100.0, 300.0);
    }

    private static Enemy createEnemy(double xPos) {
        return new Enemy(xPos, 300.0, "slimeBa.png", "still");
    }

    @Test
    public void checkCollision() {
        assertTrue(strategy.checkCollision(createHero(40.0), createEnemy(50.0)));
        assertFalse(strategy.checkCollision(createHero(20.0), createEnemy(50.0)));
    }

    @Test
    public void handleCollision() {
        assertNull(strategy.handleCollision(createHero(0.0), createEnemy(0.0)));
    }
}
