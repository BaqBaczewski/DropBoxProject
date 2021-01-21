package example.audiohive.app;

import example.audiohive.app.sound.SoundService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaginationTests {

    @Test
    public void testPaginations() {
        assertThat(SoundService.createPagination(1, 6)).containsExactly(1, 2, 3, 4, -1, 6);
        assertThat(SoundService.createPagination(5, 6)).containsExactly(1, -1, 4, 5, 6);
    }

}
