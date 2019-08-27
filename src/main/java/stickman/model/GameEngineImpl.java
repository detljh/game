package stickman.model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Paths;

public class GameEngineImpl implements GameEngine {
    public JSONObject configuration;

    public GameEngineImpl(String fileName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            JSONParser jp = new JSONParser();
            configuration = (JSONObject) jp.parse(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(configuration);
    }

    @Override
    public Level getCurrentLevel() {
        return null;
    }

    @Override
    public void startLevel() {

    }

    @Override
    public boolean jump() {
        return false;
    }

    @Override
    public boolean moveLeft() {
        return false;
    }

    @Override
    public boolean moveRight() {
        return false;
    }

    @Override
    public boolean stopMoving() {
        return false;
    }

    @Override
    public void tick() {

    }
}
