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
    private static final URI getThumbInfoURI;
    private static final URI myListURI;
    static {
        URI value;
        try {
            value = new URI("http://ext.nicovideo.jp/api/getthumbinfo/");
            getThumbInfoURI = value;
            value = new URI("https://nvapi.nicovideo.jp/v2/mylists/");
            myListURI = value;
        } catch(URISyntaxException ignored) {
            throw new RuntimeException("Failed to generate URI object");
        }
    }

    private static final HttpClient defaultHttpClient = HttpClient.newHttpClient();
    private static final HttpRequest.Builder defaultHttpBuilder = HttpRequest
            .newBuilder()
            .headers("User-Agent", "JavaJavaDougaAPI");

    public static Map<String, String> getThumbInfo(String videoId)
            throws FailedResponseException, IOException, InterruptedException {
        HttpRequest httpRequest;
        HttpResponse<InputStream> httpResponse;
        InputStream httpInputStream;
        Document document;
        Element rootNode;
        NodeList nodeList;
        Map<String, String> thumbInfo;

        httpRequest = defaultHttpBuilder
                .GET()
                .uri(getThumbInfoURI.resolve(videoId))
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
            throw new RuntimeException("ParserConfigurationException has occurred.");
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
            throw new FailedResponseException(code + ": " + desc, code, desc);
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

    public static SnapShotSearch.SnapShotSearchBuilder getSnapShotSearchBuilder() {
        return new SnapShotSearch.SnapShotSearchBuilder();
    }
}
