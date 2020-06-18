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
    static {
        URI value = null;
        try {
            value = new URI("http://ext.nicovideo.jp/api/getthumbinfo/");
        } catch(URISyntaxException ignored) {}
        getThumbInfoURI = value;
    }
    private static final URI snapShotSearchURI;
    static {
        URI value = null;
        try {
            value = new URI("https://api.search.nicovideo.jp/api/v2/snapshot/video/contents/search/");
        } catch(URISyntaxException ignored) {}
        snapShotSearchURI = value;
    }

    private static final HttpClient defaultHttpClient = HttpClient.newHttpClient();
    private static final HttpRequest.Builder defaultHttpBuilder = HttpRequest
            .newBuilder()
            .headers("User-Agent", "JavaJavaDougaAPI");

    public static NicoMap<String, String> getThumbInfo(String videoId)
            throws FailedThumbResponseException, IOException, InterruptedException {
        HttpRequest httpRequest;
        HttpResponse<InputStream> httpResponse;
        InputStream httpInputStream;
        Document document;
        Element rootNode;
        NodeList nodeList;
        NicoMap<String, String> thumbInfo;

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
            System.err.println("Failed to created new Document builder.");
            return null;
        } catch(SAXException e) {
            System.err.println("Failed to parse the XML document.");
            return null;
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
            throw new FailedThumbResponseException(code + ": " + desc);
        }

        thumbInfo = new NicoMap<String, String>();

        nodeList = rootNode.getElementsByTagName("thumb").item(0).getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if(node.getNodeType() == 3) continue;
                thumbInfo.put(node.getNodeName(), node.getTextContent());
        }
        return thumbInfo;
    }



    public static void main(String[] args) {
        try {
            System.out.println(getThumbInfo("sm28242091").getInt("Something that doesn't exist"));
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
