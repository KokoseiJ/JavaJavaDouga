import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GetThumbInfoTest {
    @Test
    @DisplayName("SM video test")
    void smVideoTest() {
        //OrangeStar - Alice in reitouko
        assertDoesNotThrow(() -> NicoAPI.getThumbInfo("sm28242091").toString());
    }

    @Test
    @DisplayName("NM video test")
    void nmVideoTest() {
        //Agoaniki - Double Lariat
        assertDoesNotThrow(() -> NicoAPI.getThumbInfo("nm6049209").toString());
    }

    @Test
    @DisplayName("Deleted video")
    void deletedVideoTest() {
        //I don't know what this is tbh, just a random number
        assertThrows(FailedResponseException.class, () -> NicoAPI.getThumbInfo("sm12342321"));
    }
}
