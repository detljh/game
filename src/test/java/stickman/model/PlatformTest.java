package stickman.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlatformTest {
    private static Platform platform;

    @Before
    public void setUp() throws Exception {
        platform = new Platform(300.0, 300.0, "normal", "foot_tile.png");
    }

    @Test
    public void getType() {
        assertEquals("normal", platform.getType());
    }

    @Test
    public void getImagePath() {
        assertEquals("foot_tile.png", platform.getImagePath());
    }

    @Test
    public void getXPos() {
        assertEquals(300.0, platform.getXPos(), 0.001);
    }

    @Test
    public void getYPos() {
        assertEquals(300.0 - platform.getHeight(), platform.getYPos(), 0.001);
    }

    @Test
    public void getHeight() {
        assertEquals(20.0, platform.getHeight(), 0.001);
    }

    @Test
    public void getWidth() {
        assertEquals(20.0, platform.getWidth(), 0.001);
    }

    @Test
    public void getLayer() {
        assertEquals(Entity.Layer.FOREGROUND, platform.getLayer());
    }
}