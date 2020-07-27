import java.io.IOException;
import java.net.CookieHandler;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnapShotSearchBuilder {
    private final Map<String, String> parameters = new HashMap<>();
    private final List<String> headers = new ArrayList<>();
    private HttpClient httpClient;
    private CookieHandler cookieHandler;

    public SnapShotSearchBuilder() {
        String fields = "contentId," +"title," + "description," + "viewCounter," + "mylistCounter," + "lengthSeconds," +
                "thumbnailUrl," + "startTime," + "lastResBody," + "commentCounter," + "lastCommentTime," + "categoryTags," +
                "tags," + "genre";
        this    .setTargets("title,description,tags")
                .setFields(fields)
                .setSort("viewCounter")
                .setAppName(NicoAPI.defaultUserAgent);
    }

    public SnapShotSearchBuilder setHttpClient(HttpClient client) {
        this.httpClient = client;
        return this;
    }

    public SnapShotSearchBuilder setCookieHandler(CookieHandler handler) {
        this.cookieHandler = handler;
        return this;
    }

    public SnapShotSearchBuilder setAppName(String appName) {
        return this.setContext(appName).setUserAgent(appName);
    }

    public SnapShotSearchBuilder setHeader(String key, String value) {
        headers.add(key);
        headers.add(value);
        return this;
    }

    public SnapShotSearchBuilder setQuery(String query) {
        parameters.put("q", query);
        return this;
    }

    public SnapShotSearchBuilder setTargets(String targets) {
        parameters.put("targets", targets);
        return this;
    }

    public SnapShotSearchBuilder setFields(String fields) {
        parameters.put("fields", fields);
        return this;
    }

    public SnapShotSearchBuilder setFilters(String filters) {
        parameters.put("filters", filters);
        return this;
    }

    public SnapShotSearchBuilder setJsonFilter(String jsonFilter) {
        parameters.put("jsonFilter", jsonFilter);
        return this;
    }

    public SnapShotSearchBuilder setSort(String sort) {
        parameters.put("_sort", sort);
        return this;
    }

    public SnapShotSearchBuilder setOffset(int offset) {
        parameters.put("_offset", Integer.toString(offset));
        return this;
    }

    public SnapShotSearchBuilder setOffset(String offset) {
        parameters.put("_offset", offset);
        return this;
    }

    public SnapShotSearchBuilder setLimit(int limit) {
        parameters.put("_limit", Integer.toString(limit));
        return this;
    }

    public SnapShotSearchBuilder setLimit(String limit) {
        parameters.put("_limit", limit);
        return this;
    }

    public SnapShotSearchBuilder setContext(String context) {
        parameters.put("_context", context);
        return this;
    }

    public SnapShotSearchBuilder setUserAgent(String userAgent) {
        return this.setHeader("User-Agent", userAgent);
    }

    public SnapShotSearchBuilder set(String key, String value) {
        parameters.put(key, value);
        return this;
    }

    public SnapShotSearch build() throws InterruptedException, IOException, FailedResponseException {
        return new SnapShotSearch(parameters, (String[]) headers.toArray(), httpClient, cookieHandler);
    }

}