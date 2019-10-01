package stickman.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SafeZoneTest {
    private static SafeZone s;

    @Before
    public void setUp() throws Exception {
        s = new SafeZone(300.0, 300.0, 100.0, 100.0,"safe_zone.png");
    }

    @Test
    public void getImagePath() {
        assertEquals("safe_zone.png", s.getImagePath());
    }

    @Test
    public void getXPos() {
        assertEquals(300.0, s.getXPos(), 0.001);
    }

    @Test
    public void getYPos() {
        assertEquals(300.0 - s.getHeight(), s.getYPos(), 0.001);
    }

    @Test
    public void getHeight() {
        assertEquals(100.0, s.getHeight(), 0.001);
    }

    @Test
    public void getWidth() {
        assertEquals(100.0, s.getWidth(), 0.001);
    }

    @Test
    public void getLayer() {
        assertEquals(Entity.Layer.COLLIDER, s.getLayer());
    }
}