package stickman.model;

import javafx.geometry.Point2D;
import stickman.collision.CollisionStrategy;
import stickman.controller.Controller;

public interface Entity {
    String getImagePath();
    double getXPos();
    double getYPos();
    double getHeight();
    double getWidth();
    Layer getLayer();

    enum Layer {
        BACKGROUND, FOREGROUND, EFFECT
    }
}
