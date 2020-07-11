import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class NicoAPI {
    protected static final HttpClient defaultHttpClient = HttpClient.newHttpClient();
    protected static final String defaultUserAgent = "JavaJavaDougaAPI";

    private static final URI getThumbInfoURI;
    private static final URI myListURI;
    static {
        URI value;
        try {
            value = new URI("http://ext.nicovideo.jp/api/getthumbinfo/");
            getThumbInfoURI = value;
            //https://nvapi.nicovideo.jp/v2/mylists/61011276?page=1&pageSize=25
            //https://www.nicovideo.jp/mylist/24932481
            value = new URI("https://nvapi.nicovideo.jp/v2/mylists/");
            myListURI = value;
        } catch(URISyntaxException ignored) {
            throw new RuntimeException("Failed to generate URI object");
        }
    }


    public static Map<String, String> getThumbInfo(String videoId)
            throws FailedResponseException, IOException, InterruptedException{
        return getThumbInfo(videoId, defaultUserAgent);
    }

    public static Map<String, String> getThumbInfo(String videoId, String userAgent)
            throws FailedResponseException, IOException, InterruptedException {
        HttpRequest httpRequest;
        HttpResponse<InputStream> httpResponse;
        InputStream httpInputStream;
        Document document;
        Element rootNode;
        NodeList nodeList;
        Map<String, String> thumbInfo;

        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(getThumbInfoURI.resolve(videoId))
                .header("User-Agent", userAgent)
                .build();
        httpResponse = defaultHttpClient
                .send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        httpInputStream = httpResponse.body();

        try {
            document = DocumentBuilderFactory
                    .newDefaultInstance()
                    .newDocumentBuilder()
                    .parse(httpInputStream);
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

        thumbInfo = new NicoMap<>();

        nodeList = rootNode.getElementsByTagName("thumb").item(0).getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if(node.getNodeType() == 3) continue;
                thumbInfo.put(node.getNodeName(), node.getTextContent());
        }
        return thumbInfo;
    }

    public static SnapShotSearchBuilder getSnapShotSearchBuilder() {
        return new SnapShotSearchBuilder();
    }

    public static Map<String, Object> getMyList(String myListId)
            throws IOException, InterruptedException, FailedResponseException {
        return getMyList(myListId, defaultUserAgent);
    }

    public static Map<String, Object> getMyList(String myListId, String userAgent)
            throws IOException, InterruptedException, FailedResponseException {
        HttpRequest httpRequest;
        HttpResponse<String> httpResponse;
        URI uri;
        JSONObject json;
        try {
            uri = new URI(
                    myListURI.getScheme(),
                    myListURI.getAuthority(),
                    myListURI.getPath() + myListId,
                    "page=1&pageSize=500",
                    myListURI.getFragment()
            );
        } catch(URISyntaxException ignored) {
            throw new RuntimeException("Failed to generate URI object");
        }

        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("User-Agent", userAgent)
                .header("x-frontend-id", "3") //I have no idea what this "3" means but there's no document
                .build();

        httpResponse = defaultHttpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        json = new JSONObject(httpResponse.body());

        if(httpResponse.statusCode() != 200) {
            String code = json.getJSONObject("meta").getString("status");
            String desc = json.getJSONObject("meta").getString("errorCode");
            throw new FailedResponseException(code, desc);
        }
        return json.toMap();
    }
}
