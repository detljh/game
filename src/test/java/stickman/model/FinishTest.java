package stickman.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FinishTest {
    private static Finish finish;

    @Before
    public void setUp() throws Exception {
        finish = new Finish(300.0, 300.0, "finish.png");
    }

    @Test
    public void getImagePath() {
        assertEquals("finish.png", finish.getImagePath());
    }

    @Test
    public void getXPos() {
        assertEquals(300.0, finish.getXPos(), 0.001);
    }

    @Test
    public void getYPos() {
        assertEquals(300.0 - finish.getHeight(), finish.getYPos(), 0.001);
    }

    @Test
    public void getHeight() {
        assertEquals(40.0, finish.getHeight(), 0.001);
    }

    @Test
    public void getWidth() {
        assertEquals(40.0, finish.getWidth(), 0.001);
    }

    @Test
    public void getLayer() {
        assertEquals(Entity.Layer.FINISH, finish.getLayer());
    }
}