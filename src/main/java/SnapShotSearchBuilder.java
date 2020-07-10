import java.util.HashMap;
import java.util.Map;

public class SnapShotSearchBuilder {
    protected final Map<String, String> parameters = new HashMap<>();
    protected String userAgent = "JavaJavaDougaAPI";

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
        this.userAgent = userAgent;
        return this;
    }

    public SnapShotSearchBuilder setAppName(String appName) {
        return this.setContext(appName).setUserAgent(appName);
    }

    public SnapShotSearchBuilder set(String key, String value) {
        parameters.put(key, value);
        return this;
    }

    public SnapShotSearch build() {
        return new SnapShotSearch(this);
    }

}