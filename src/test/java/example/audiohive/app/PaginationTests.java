package example.audiohive.app;

import example.audiohive.app.sound.SoundService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaginationTests {

    @Test
    public void testPaginations() {
        assertThat(SoundService.createPagination(1, 6)).containsExactly(1, 2, 3, 4, -1, 6);
        assertThat(SoundService.createPagination(5, 6)).containsExactly(1, -1, 4, 5, 6);
        assertThat(SoundService.createPagination(5, 25)).containsExactly(1, -1, 4, 5,6,7,8,-1, 25);
        assertThat(SoundService.createPagination(35, 125)).containsExactly(1, -1, 34, 35,36,37,38,-1, 125);

    }

}
