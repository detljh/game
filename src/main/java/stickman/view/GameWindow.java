package stickman.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import stickman.controller.HeroController;
import stickman.model.Entity;
import stickman.model.GameEngine;
import java.util.ArrayList;
import java.util.List;

public class GameWindow {
    private final int width;
    private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews;
    private BackgroundDrawer backgroundDrawer;
    private Text time;
    private Text lives;
    private Timeline timeline;

    private double xViewportOffset = 0.0;
    private static double VIEWPORT_MARGIN;

    public GameWindow(GameEngine model, int width, int height) {
        this.model = model;
        this.pane = new Pane();
        this.width = width;
        this.scene = new Scene(pane, width, height);
        this.VIEWPORT_MARGIN = width * 2/5;

        time = new Text();
        time.setFill(Color.BLACK);
        time.setX(0.0);
        time.setY(10.0);
        time.setViewOrder(0.0);
        pane.getChildren().add(time);

        lives = new Text();
        lives.setFill(Color.BLACK);
        lives.setX(width - 50.0);
        lives.setY(10.0);
        lives.setViewOrder(0.0);
        pane.getChildren().add(lives);

        this.entityViews = new ArrayList<>();

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler((HeroController) model.getCurrentLevel().getHero().getController());

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        this.backgroundDrawer = new ParallaxBackground();

        backgroundDrawer.draw(model, pane);
    }

    public Scene getScene() {
        return this.scene;
    }

    public void run() {
        timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setTimer() {
        time.setText("Elapsed time: " + model.getTime());
    }

    private void setLives() {
        lives.setText("Lives: " + model.getLives());
    }

    private void draw() {
        try {
            model.tick();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        if (model.finish().equals("won")) {
            for (Node n : pane.getChildren()) {
                n.setOpacity(0.5);
            }
            Text gameOverText = new Text("Congratulations!\n You have won!");
            gameOverText.setFill(Color.BLACK);
            gameOverText.setFont(Font.font(20));
            gameOverText.setViewOrder(0.0);
            gameOverText.setX(width / 3);
            gameOverText.setY(scene.getHeight() / 2);
            pane.getChildren().add(gameOverText);
            timeline.stop();
        } else if (model.finish().equals("lost")) {
            for (Node n : pane.getChildren()) {
                n.setOpacity(0.5);
            }
            Text gameOverText = new Text("Game Over!");
            gameOverText.setFill(Color.BLACK);
            gameOverText.setFont(Font.font(20));
            gameOverText.setViewOrder(0.0);
            pane.getChildren().add(gameOverText);
            timeline.stop();
        } else {
            setTimer();
            setLives();
        }

        List<Entity> entities = model.getCurrentLevel().getEntities();

        for (EntityView entityView: entityViews) {
            entityView.markForDelete();
        }

        double heroXPos = model.getCurrentLevel().getHeroX();
        heroXPos -= xViewportOffset;

        if (heroXPos < VIEWPORT_MARGIN) {

            if (xViewportOffset >= 0) { // Don't go further left than the start of the level
                xViewportOffset -= VIEWPORT_MARGIN - heroXPos;
                if (xViewportOffset < 0) {
                    xViewportOffset = 0;
                }
            }
        } else if (heroXPos > width - VIEWPORT_MARGIN) {
            xViewportOffset += heroXPos - (width - VIEWPORT_MARGIN);
        }

        backgroundDrawer.update(xViewportOffset);

        for (Entity entity : entities) {
            boolean notFound = true;
            for (EntityView view: entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (EntityView entityView: entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }
        entityViews.removeIf(EntityView::isMarkedForDelete);
    }
}
