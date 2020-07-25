import java.util.ArrayList;
import java.util.List;

public class Tags {
    public final String domain;
    public final String category;
    public final List<String> lockedTags;
    public final List<String> normalTags;
    public final List<String> allTags;

    protected Tags(String domain, String category, List<String> locked, List<String> normal) {
        this.domain = domain;
        this.category = category;
        this.lockedTags = locked;
        this.normalTags = normal;
        this.allTags = new ArrayList<String>();
        this.allTags.add(category);
        this.allTags.addAll(locked);
        this.allTags.addAll(normal);
    }
}
