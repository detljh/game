package stickman.view;

import de.saxsys.javafx.test.JfxRunner;
import javafx.scene.image.ImageView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import stickman.model.Cloud;
import stickman.model.Finish;
import stickman.model.Hero;
import stickman.model.SafeZone;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
public class EntityViewImplTest {
    private EntityViewImpl view;
    private Hero hero;

    @Before
    public void setUp() throws Exception {
        hero = new Hero("ch_stand1.png", 40.0, "normal", 100.0, 300.0);
        view = new EntityViewImpl(hero);
    }

    @Test
    public void update() {
        view.update(10, 10);
        ImageView node = (ImageView) view.getNode();
        assertTrue(node.getImage().getUrl().endsWith("ch_stand1.png"));
        assertEquals(30, node.getX(), 0.001);
        assertEquals(280, node.getY(), 0.001);
        assertEquals(30.0, node.getFitHeight(), 0.001);
        assertEquals(30.0, node.getFitWidth(), 0.001);
        assertFalse(view.isMarkedForDelete());

        hero.updateImagePath("ch_stand2.png");
        view.update(0, 0.0);
        node = (ImageView) view.getNode();
        assertTrue(node.getImage().getUrl().endsWith("ch_stand2.png"));
    }

    @Test
    public void matchesEntity() {
        assertTrue(view.matchesEntity(hero));
    }

    @Test
    public void markForDelete() {
        assertFalse(view.isMarkedForDelete());
        view.markForDelete();
        assertTrue(view.isMarkedForDelete());
    }

    @Test
    public void getViewOrder() {
        EntityViewImpl entityView = new EntityViewImpl(new Cloud("cloud_1.png", 40.0, 40.0, 3));
        ImageView v = (ImageView) entityView.getNode();
        assertEquals(100.0, v.getViewOrder(), 0.001);

        entityView = new EntityViewImpl(new SafeZone(40.0, 40.0, 40.0, 40.0, "safe_zone.png"));
        v = (ImageView) entityView.getNode();
        assertEquals(80.0, v.getViewOrder(), 0.001);

        entityView = new EntityViewImpl(new Finish(40.0, 40.0, "safe_zone.png"));
        v = (ImageView) entityView.getNode();
        assertEquals(60.0, v.getViewOrder(), 0.001);

        entityView = new EntityViewImpl(hero);
        v = (ImageView) entityView.getNode();
        assertEquals(50.0, v.getViewOrder(), 0.001);
    }
}