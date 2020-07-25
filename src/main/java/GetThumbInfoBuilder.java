import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetThumbInfoBuilder {
    private String videoId = null;
    private String userAgent = NicoAPI.defaultUserAgent;
    private List<String> headers = new ArrayList<String>();
    private HttpClient httpClient = null;
    private CookieHandler cookieHandler = null;

    public GetThumbInfoBuilder setVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }

    public GetThumbInfoBuilder setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public GetThumbInfoBuilder setHttpClient(HttpClient client) {
        this.httpClient = client;
        return this;
    }

    public GetThumbInfoBuilder setCookieHandler(CookieHandler handler) {
        this.cookieHandler = handler;
        return this;
    }

    public GetThumbInfoBuilder setHeader(String key, String value) {
        this.headers.add(key);
        this.headers.add(value);
        return this;
    }

    public GetThumbInfo build() throws IOException, InterruptedException, FailedResponseException {
        if (videoId == null)
            throw new RuntimeException("videoId value is not set.");
        this.setHeader("User-Agent", userAgent);

        return new GetThumbInfo(videoId, (String[])headers.toArray(), httpClient, cookieHandler);
    }

    public GetThumbInfoBuilder(){}

    public GetThumbInfoBuilder(String videoId) {
        this.setVideoId(videoId);
    }
}
