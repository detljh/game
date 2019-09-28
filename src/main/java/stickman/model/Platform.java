package stickman.model;

public class Platform implements Entity {
    private double xPos;
    private double yPos;
    private String imagePath;

    Platform(double xPos, double yPos, String imagePath) {
        this.xPos = xPos;
        this.yPos = yPos - getHeight();
        this.imagePath = imagePath;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public double getXPos() {
        return xPos;
    }

    @Override
    public double getYPos() {
        return yPos;
    }

    @Override
    public double getHeight() {
        return 20.0;
    }

    @Override
    public double getWidth() {
        return 20.0;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }
}
