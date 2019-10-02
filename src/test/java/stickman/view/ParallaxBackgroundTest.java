package stickman.view;

import de.saxsys.javafx.test.JfxRunner;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import stickman.ReadConfiguration;
import stickman.model.GameEngineImpl;
import stickman.model.LevelImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
public class ParallaxBackgroundTest {
    private ParallaxBackground bg;
    private Pane pane;

    @Before
    public void setUp() throws Exception {
        pane = new Pane();
        bg = new ParallaxBackground();
    }

    @Test
    public void update() {
        for (Node n : pane.getChildren()) {
            if (n instanceof Rectangle) {
                double initialY = n.getTranslateY();
                bg.update(10, 10);
                assertEquals(initialY + 10, n.getTranslateY(), 0.001);
            }
        }
    }
}