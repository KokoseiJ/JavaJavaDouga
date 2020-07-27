import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnapShotSearch {
    private final static URI snapShotSearchURI;
    static {
        URI value;
        try {
            value = new URI("https://api.search.nicovideo.jp/api/v2/snapshot/video/contents/search");
        } catch (URISyntaxException ignored) {
            throw new RuntimeException("Failed to generate URI object and the god is dead");
        }
        snapShotSearchURI = value;
    }

    private final JSONObject json;

    protected SnapShotSearch(
            Map<String, String> parameters, String[] headers, HttpClient client, CookieHandler handler)
            throws IOException, InterruptedException, FailedResponseException {
        URI uri;
        HttpRequest request;
        HttpResponse<String> response;
        Charset charset;
        try {
            charset = Charset.forName("ISO8601");
        } catch (UnsupportedCharsetException e) {
            charset = Charset.defaultCharset();
        }

        if (client == null) {
            HttpClient.Builder builder = HttpClient.newBuilder();
            if (handler != null)
                builder.cookieHandler(handler);
            client = builder.build();
        }

        uri = getURI(parameters);
        request = HttpRequest.newBuilder()
                .GET()
                .headers(headers)
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(charset));
        json = new JSONObject(response.body());

        if (response.statusCode() != 200) {
            JSONObject meta = json.getJSONObject("meta");
            String code = meta.getString("errorCode");
            String message = meta.getString("errorMessage");
            throw new FailedResponseException(code, message);
        }
    }

    private String getQuery(Map<String, String> parameters) {
        String value;
        StringBuilder returnString = new StringBuilder();

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if ((value = entry.getValue()) != null) {
                if (returnString.length() > 0) returnString.append("&");
                returnString.append(entry.getKey()).append("=").append(value);
            }
        }

        return returnString.toString();
    }

    private URI getURI(Map<String, String> parameters) {
        URI uri = snapShotSearchURI;
        String query = getQuery(parameters);
        URI returnURI = null;
        try {
            returnURI = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), query, uri.getFragment());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return returnURI;
    }
}