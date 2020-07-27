import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class SearchEntry {
    private final JSONObject json;

    protected SearchEntry(JSONObject json) {
        this.json = json;
    }

    public Object get(String key) {
        return json.get(key);
    }

    public String getString(String key) {
        return (String) json.get(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(this.getString(key));
    }

    public URI getURI(String key) throws URISyntaxException {
        return new URI(this.getString(key));
    }


}
