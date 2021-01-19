package example.audiohive.app.playback;

import example.audiohive.app.sound.Sound;
import example.audiohive.app.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface PlaybackRepository extends JpaRepository<Playback, String> {
    long countBySound(Sound sound);
    boolean existsBySoundAndUserAndPlayedAtAfter(Sound sound, User user, Instant instant);
}
