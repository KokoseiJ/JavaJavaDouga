import java.util.HashMap;

public class NicoMap<K, V> extends HashMap<K, V> {
    public int getInt(String key) throws IllegalArgumentException {
        String value = (String) this.get(key);
        if(value == null) throw new IllegalArgumentException(key + " value is null");
        return Integer.parseInt(value);
    }

    public boolean getBool(String key) {
        String value = (String) this.get(key);
        return value.equals("1");
    }
}