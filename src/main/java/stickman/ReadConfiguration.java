package stickman;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.net.URI;

class ReadConfiguration {
    private JSONObject configuration;
    ReadConfiguration(String filename) {
        try {
            URI uri = new URI(this.getClass().getResource("/" + filename).toString());
            JSONParser jp = new JSONParser();
            configuration = (JSONObject) jp.parse(new FileReader(new File(uri.getPath())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    JSONObject getConfig() {
        return configuration;
    }
}
