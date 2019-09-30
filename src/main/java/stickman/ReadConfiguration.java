package stickman;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.security.util.Debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ReadConfiguration {
    private JSONObject configuration;
    public ReadConfiguration(String filename) {
        try {
            URI uri = new URI(this.getClass().getResource("/" + filename).toString());
            JSONParser jp = new JSONParser();
            configuration = (JSONObject) jp.parse(new FileReader(new File(uri.getPath())));
        } catch (URISyntaxException | IOException | ParseException | NullPointerException e) {
            System.err.println("Invalid file.");
        }
    }

    public JSONObject getConfig() {
        return configuration;
    }
}
