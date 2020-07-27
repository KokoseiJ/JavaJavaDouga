import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.CookieHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class GetThumbInfo {
    private static final URI API_URI;
    static {
        try {
            API_URI = new URI("http://ext.nicovideo.jp/api/getthumbinfo/");
        } catch(URISyntaxException e) {
            throw new RuntimeException("Failed to initialize URI.");
        }
    }

    public final String videoId;
    public final String title;
    public final String description;
    public final URI thumbnailUri;
    public final String firstRetrieve;
    public final String lengthString;
    public final int length;
    public final String movieType;
    public final int sizeHigh;
    public final int sizeLow;
    public final int viewCounter;
    public final int commentNum;
    public final int mylistCounter;
    public final String lastResBody;
    public final URI watchUri;
    public final String thumbType;
    public final boolean embeddable;
    public final boolean noLivePlay;
    public final Tags tags;
    public final String genre;
    public final String userId;
    public final String userNickname;
    public final URI userIconUri;

    public static GetThumbInfo getThumbInfo(String videoId)
            throws IOException, InterruptedException, FailedResponseException {
        return new GetThumbInfoBuilder()
                .setVideoId(videoId)
                .build();
    }

    protected GetThumbInfo(
            String videoId, String[] headers, HttpClient client, CookieHandler cookieHandler)
            throws IOException, InterruptedException, FailedResponseException {
        HttpRequest request;
        HttpResponse<InputStream> response;
        InputStream httpStream;
        Document document;
        Element rootNode;
        Element thumbNode;
        Element tag;
        NodeList tagList;
        String domain;
        String category;
        List<String> lockedTags;
        List<String> normalTags;

        this.videoId = videoId;

        if(client == null) {
            HttpClient.Builder builder = HttpClient.newBuilder();
            if(cookieHandler != null)
                builder.cookieHandler(cookieHandler);
            client = builder.build();
        }

        request = HttpRequest.newBuilder()
                .GET()
                .uri(API_URI.resolve(videoId))
                .headers(headers)
                .build();

        response = client
                .send(request, HttpResponse.BodyHandlers.ofInputStream());

        httpStream = response.body();

        try {
            document = DocumentBuilderFactory
                    .newDefaultInstance()
                    .newDocumentBuilder()
                    .parse(httpStream);
        } catch(ParserConfigurationException e) {
            throw new RuntimeException("ParserConfigurationException has been occurred.");
        } catch(SAXException e) {
            throw new FailedResponseException("Failed to parse the XML file.");
        }
        rootNode = document.getDocumentElement();

        if(rootNode.getAttribute("status").equals("fail")) {
            Element error = (Element) rootNode
                    .getElementsByTagName("error")
                    .item(0);
            String code = error
                    .getElementsByTagName("code")
                    .item(0)
                    .getTextContent();
            String desc = error
                    .getElementsByTagName("description")
                    .item(0)
                    .getTextContent();
            throw new FailedResponseException(code, desc);
        }

        thumbNode = (Element) rootNode.getElementsByTagName("thumb").item(0);

        title = thumbNode.getElementsByTagName("title").item(0).getTextContent();
        description = thumbNode.getElementsByTagName("description").item(0).getTextContent();
        firstRetrieve = thumbNode.getElementsByTagName("first_retrieve").item(0).getTextContent();
        lengthString = thumbNode.getElementsByTagName("length").item(0).getTextContent();
        movieType = thumbNode.getElementsByTagName("movie_type").item(0).getTextContent();
        lastResBody = thumbNode.getElementsByTagName("last_res_body").item(0).getTextContent();
        thumbType = thumbNode.getElementsByTagName("thumb_type").item(0).getTextContent();
        genre = thumbNode.getElementsByTagName("genre").item(0).getTextContent();
        userId = thumbNode.getElementsByTagName("user_id").item(0).getTextContent();
        userNickname = thumbNode.getElementsByTagName("user_nickname").item(0).getTextContent();

        length = Integer.parseInt(lengthString.split(":")[0]) * 60 + Integer.parseInt(lengthString.split(":")[1]);
        sizeHigh = Integer.parseInt(thumbNode.getElementsByTagName("size_high").item(0).getTextContent());
        sizeLow = Integer.parseInt(thumbNode.getElementsByTagName("size_low").item(0).getTextContent());
        viewCounter = Integer.parseInt(thumbNode.getElementsByTagName("view_counter").item(0).getTextContent());
        commentNum = Integer.parseInt(thumbNode.getElementsByTagName("comment_num").item(0).getTextContent());
        mylistCounter = Integer.parseInt(thumbNode.getElementsByTagName("mylist_counter").item(0).getTextContent());

        embeddable = thumbNode.getElementsByTagName("embeddable").item(0).getTextContent().equals("1");
        noLivePlay = thumbNode.getElementsByTagName("no_live_play").item(0).getTextContent().equals("1");
        try {
            thumbnailUri = new URI(thumbNode.getElementsByTagName("thumbnail_url").item(0).getTextContent());
            watchUri = new URI(thumbNode.getElementsByTagName("watch_url").item(0).getTextContent());
            userIconUri = new URI(thumbNode.getElementsByTagName("user_icon_url").item(0).getTextContent());
        } catch(URISyntaxException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();
            throw new RuntimeException("Failed to parse URI from a returned value.\nStack trace: " + stackTrace);
        }

        tag = (Element)thumbNode.getElementsByTagName("tags").item(0);
        tagList = tag.getElementsByTagName("tag");

        domain = tag.getAttribute("domain");
        category = null;
        lockedTags = new ArrayList<>();
        normalTags = new ArrayList<>();

        for(int i = 0; i < tagList.getLength(); i++) {
            Element node = (Element)tagList.item(i);
            if(node.hasAttribute("category"))
                category = node.getTextContent();
            else if(node.hasAttribute("lock"))
                lockedTags.add(node.getTextContent());
            else
                normalTags.add(node.getTextContent());
        }

        if(category == null)
            throw new RuntimeException("Category tag is null");

        tags = new Tags(domain, category, lockedTags, normalTags);
    }
}
