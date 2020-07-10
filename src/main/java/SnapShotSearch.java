import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;

public class SnapShotSearch {
    private final static URI snapShotSearchURI;
    static {
        URI value;
        try {
            value = new URI("https://api.search.nicovideo.jp/api/v2/snapshot/video/contents/search");
        } catch(URISyntaxException ignored) {
            throw new RuntimeException("Failed to generate URI object and the god is dead");
        }
        snapShotSearchURI = value;
    }

    private final SnapShotSearchBuilder builder;

    protected SnapShotSearch(SnapShotSearchBuilder builder) {
        this.builder = builder;
    }

    private String getQuery() {
        String value;
        StringBuilder returnString = new StringBuilder();

        for(Map.Entry<String, String> entry: builder.parameters.entrySet()) {
            if((value = entry.getValue()) != null){
                if(returnString.length() > 0) returnString.append("&");
                returnString.append(entry.getKey()).append("=").append(value);
            }
        }

        return returnString.toString();
    }

    private URI getURI() {
        URI uri = snapShotSearchURI;
        String query = getQuery();
        URI returnURI = null;
        try {
            returnURI = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), query, uri.getFragment());
        } catch(URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return returnURI;
    }

    public Map<String, Object> getResult() throws IOException, InterruptedException, FailedResponseException {
        URI uri;
        HttpClient client;
        HttpRequest request;
        HttpResponse<String> response;
        JSONObject json;
        Charset charset;
        try {
            charset = Charset.forName("ISO8601");
        } catch(UnsupportedCharsetException e) {
            charset = Charset.defaultCharset();
        }

        uri = getURI();
        client = NicoAPI.defaultHttpClient;
        request = HttpRequest.newBuilder()
                .GET()
                .header("User-Agent", builder.userAgent)
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(charset));
        json = new JSONObject(response.body());

        if(response.statusCode() != 200) {
            JSONObject meta = json.getJSONObject("meta");
            String code = meta.getString("errorCode");
            String message = meta.getString("errorMessage");
            throw new FailedResponseException(code, message);
        }
        return json.toMap();
    }
}
