import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchResult {
    public final List<SearchEntry> entryList;

    protected SearchResult(List<SearchEntry> entryList) {
        this.entryList = entryList;
    }

    public SearchEntry[] toArray() {
        return (SearchEntry[]) this.entryList.toArray();
    }

    public Object[][] getArrayOf(String... keys) {
        List<List<Object>> list = new ArrayList<>();
        entryList.forEach((entry) -> {
            List<Object> nestedList = new ArrayList<>();
            Arrays.asList(keys).forEach((key) -> nestedList.add(entry.get(key)));
            list.add(nestedList);
        });
        return (Object[][]) list.toArray();
    }

    public int[][] getArrayOfInt(String... keys) {
        List<List<Integer>> list = new ArrayList<>();
        entryList.forEach((entry) -> {
            List<Integer> nestedList = new ArrayList<>();
            Arrays.asList(keys).forEach((key) -> nestedList.add(entry.getInt(key)));
            list.add(nestedList);
        });
        return (int[][]) list.toArray();
    }

    public String[][] getArrayOfString(String... keys) {
        List<List<String>> list = new ArrayList<>();
        entryList.forEach((entry) -> {
            List<String> nestedList = new ArrayList<>();
            Arrays.asList(keys).forEach((key) -> nestedList.add(entry.getString(key)));
            list.add(nestedList);
        });
        return (String [][]) list.toArray();
    }


}
