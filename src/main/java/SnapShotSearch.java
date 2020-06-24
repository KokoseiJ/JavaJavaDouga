import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class SnapShotSearch {
    private static URI uri;
    static {
        URI value = null;
        try {
            value = new URI("https://api.search.nicovideo.jp/api/v2/snapshot/video/contents/search");
        } catch(URISyntaxException ignored) {}
        uri = value;
    }

    public SnapShotSearchBuilder builder;

    private SnapShotSearch(SnapShotSearchBuilder builder)
            throws IOException, InterruptedException, FailedResponseException {
        this.builder = builder;

        HttpClient client;
        HttpRequest request;
        HttpResponse<String> response;
        Charset charset;
        JSONObject json;

        try {
            charset = Charset.forName("ISO8601");
        } catch(UnsupportedCharsetException e) {
            charset = Charset.defaultCharset();
        }

        client = HttpClient.newBuilder().build();
        request = HttpRequest.newBuilder()
                .GET()
                .header("User-Agent", builder.userAgent)
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(charset));
        json = new JSONObject(response.body());

        if(json.getJSONObject("meta").getInt("status") != 200) {
            JSONObject meta = json.getJSONObject("meta");
            String code = meta.getString("errorCode");
            String message = meta.getString("errorMessage");
            throw new FailedResponseException(code + ": " + message);
        }

    }

    public static class SnapShotSearchBuilder {
        private String query = null;
        private String targets = null;
        private String fields = null;
        private String filters = null;
        private String jsonFilter = null;
        private String sort = null;
        private int offset = 0;
        private int limit = 10;
        private String context = "JavaJavaDougaAPI";
        private String userAgent = "JavaJavaDougaAPI";

        public SnapShotSearchBuilder setQuery(String query) {
            this.query = query;
            return this;
        }

        public SnapShotSearchBuilder setTargets(String targets) {
            this.targets = targets;
            return this;
        }

        public SnapShotSearchBuilder setFields(String fields) {
            this.fields = fields;
            return this;
        }

        public SnapShotSearchBuilder setFilters(String filters) {
            this.filters = filters;
            return this;
        }

        public SnapShotSearchBuilder setJsonFilter(String jsonFilter) {
            this.jsonFilter = jsonFilter;
            return this;
        }

        public SnapShotSearchBuilder setSort(String sort) {
            this.sort = sort;
            return this;
        }

        public SnapShotSearchBuilder setOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public SnapShotSearchBuilder setLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public SnapShotSearchBuilder setContext(String context) {
            this.context = context;
            return this;
        }

        public SnapShotSearchBuilder setUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public SnapShotSearchBuilder setAppName(String appName) {
            return this.setContext(appName).setUserAgent(appName);
        }

        public SnapShotSearch build() throws IOException, InterruptedException, FailedResponseException{
            return new SnapShotSearch(this);
        }

    }

}
