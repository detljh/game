package stickman.model;

public class Cloud implements Entity {
    private double velocity;
    private String imagePath;
    private double xPos;
    private double yPos;

    public Cloud(String imagePath, double xPos, double yPos, double velocity) {
        this.imagePath = imagePath;
        this.xPos = xPos;
        this.yPos = yPos;
        this.velocity = velocity;
    }

    public void update(double newX, double newY) {
        this.xPos = newX;
        this.yPos = newY;
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
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public Layer getLayer() {
        return Layer.BACKGROUND;
    }
}
