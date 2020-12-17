package example.audiohive.app;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;

public class TestHelper {

    private TestHelper() {
    }

    /**
     * @return A new InputStream of length 4
     */
    public static InputStream dummyAudioData() {
        return new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
    }

    private static Instant instantMovingLater = Instant.EPOCH;

    /**
     * Helper to create sequential dates
     *
     * @return an instant later then the previous one
     */
    public static Instant later() {
        instantMovingLater = instantMovingLater.plusSeconds(1);
        return instantMovingLater;
    }


}
