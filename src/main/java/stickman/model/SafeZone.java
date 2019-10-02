package stickman.model;

public class SafeZone implements Entity {
    private double xPos;
    private double yPos;
    private double height;
    private double width;
    private String imagePath;

    public SafeZone(double xPos, double yPos, double height, double width, String imagePath) {
        this.height = height;
        this.width = width;
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
        return height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public Layer getLayer() {
        return Layer.COLLIDER;
    }
}
