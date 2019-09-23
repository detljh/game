package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import stickman.controller.CloudController;
import stickman.controller.HeroController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelImpl implements Level {
    private double gravity;
    private List<Entity> entities;
    private double height;
    private double width;
    public double floorHeight;
    private Hero hero;
    private int tick;
    private int cloudCount;
    private double cloudVelocity;
    private int enemyCount;
    private int platformCount;


    LevelImpl(LevelBuilder builder) {
        this.gravity = builder.gravity;
        this.entities = builder.entities;
        this.height = builder.height;
        this.width = builder.width;
        this.floorHeight = builder.floorHeight;
        this.hero = builder.hero;
        hero.getController().setLevel(this);
        this.cloudCount = builder.cloudCount;
        this.cloudVelocity = builder.cloudVelocity;
        this.enemyCount = builder.enemyCount;
        this.platformCount = builder.platformCount;
        tick = (int) ((width * 4 / 5) / (cloudVelocity / GameEngineImpl.FPS));
    }

    private void addCloud(double velocity) {
        Random rand = new Random();
        // randomize cloud image used
        int cloudType = rand.nextInt(2) + 1;

        /* spawn them on screen, else spawn them off screen so they move in screen from
         * left of screen */
        double xPos = -(width/4) - (rand.nextDouble() * (width));
        System.out.println(xPos);
        // randomise y position of cloud
        double yPos = rand.nextDouble() * (getHeight() / 4.5);
        String path = "cloud_" + cloudType + ".png";
        Cloud c = new Cloud(path, xPos, yPos, velocity);
        CloudController cc = new CloudController(c);
        c.setController(cc);
        cc.setLevel(this);
        entities.add(c);
        cloudCount++;
    }

    public int getCloudCount() {
        return cloudCount;
    }

    public double getGravity() {
        return gravity;
    }

    @Override
    public Hero getHero() {
        return hero;
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
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
    public void tick() {
        for (int i = 0; i < cloudCount; i++) {
            entities.get(i + enemyCount + platformCount + 1).getController().tick();
        }

        // spawn 1-3 clouds if tick is 0 and velocity is positive
        if (cloudVelocity > 0) {

            tick--;
            if (tick > 0) {
                return;
            }
            tick = (int) ((width * 4 / 5) / (cloudVelocity / GameEngineImpl.FPS));

            int spawn = new Random().nextInt(3) + 1;
            for (int i = 0; i < spawn; i++) {
                addCloud(cloudVelocity);
            }
        }
    }

    @Override
    public double getFloorHeight() {
        return floorHeight;
    }

    @Override
    public double getHeroX() {
        return hero.getXPos();
    }

    static class LevelBuilder {
        private double gravity;
        private List<Entity> entities;
        private double height;
        private double width;
        private double floorHeight;
        private Hero hero;
        private int cloudCount;
        private double cloudVelocity;
        private int enemyCount;
        private int platformCount;

        LevelBuilder() {
            cloudCount = 0;
            enemyCount = 0;
            platformCount = 0;
            entities = new ArrayList<>();
            gravity = 9.8 / GameEngineImpl.FPS;
        }

        LevelBuilder setWidth(double width) {
            this.width = width;
            return this;
        }

        LevelBuilder setHeight(double height) {
            this.height = height;
            return this;
        }

        LevelBuilder setFloorHeight(double floorHeight) {
            this.floorHeight = height - floorHeight;
            return this;
        }

        LevelBuilder addHero(JSONObject config) {
            String size = (String) config.get("size");
            double xPos = (double) config.get("x");
            Hero hero = new Hero("ch_stand1.png", xPos, size, 40.0, floorHeight);
            HeroController hc = new HeroController(hero);
            hero.setController(hc);
            entities.add(hero);
            this.hero = hero;
            return this;
        }

        LevelBuilder addEnemy(JSONArray enemies) {
            return this;
        }

        LevelBuilder spawnClouds(double velocity) {
            cloudVelocity = velocity;
            // spawn initial number of clouds
            for (int i = 0; i < (int) width / 40; i++) {
                addCloud(velocity);
            }
            return this;
        }

       private void addCloud(double velocity) {
            Random rand = new Random();
            // randomize cloud image used
            int cloudType = rand.nextInt(2) + 1;

            /* spawn them on screen, else spawn them off screen so they move in screen from
             * left of screen */
            double xPos = (width * 4) - (rand.nextDouble() * (width * 4));

            // randomise y position of cloud
            double yPos = rand.nextDouble() * (height / 4.5);
            String path = "cloud_" + cloudType + ".png";
            Cloud c = new Cloud(path, xPos, yPos, velocity);
            CloudController cc = new CloudController(c);
            c.setController(cc);
            entities.add(c);
            cloudCount++;
        }

        LevelBuilder addPlatform(JSONObject coord) {
            JSONArray x = (JSONArray)coord.get("x");
            JSONArray y = (JSONArray)coord.get("y");
            String path = "foot_tile.png";

            for (int i = 0; i < x.size(); i++) {
                Platform p = new Platform((double) x.get(i), (double) y.get(i), path);
                entities.add(p);
                platformCount++;
            }
            return this;
        }

        LevelImpl build() {
            LevelImpl level = new LevelImpl(this);
            return level;
        }
    }
}
