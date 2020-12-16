package example.audiohive.app.playback;

import example.audiohive.app.sound.Sound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaybackRepository extends JpaRepository<Playback, String> {
    long countBySound(Sound sound);
}
