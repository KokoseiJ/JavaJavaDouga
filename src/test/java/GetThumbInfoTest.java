import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GetThumbInfoTest {
    @Test
    @DisplayName("SM video test")
    void smVideoTest() {
        //OrangeStar - Alice in reitouko
        assertDoesNotThrow(() -> NicoAPI.getThumbInfo("sm28242091"));
    }

    @Test
    @DisplayName("NM video test")
    void nmVideoTest() {
        //Agoaniki - Double Lariat
        assertDoesNotThrow(() -> NicoAPI.getThumbInfo("nm6049209"));
    }
}
