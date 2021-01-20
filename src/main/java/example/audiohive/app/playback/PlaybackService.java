package example.audiohive.app.playback;

import example.audiohive.app.sound.Sound;
import example.audiohive.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class PlaybackService {

    private final PlaybackRepository playbackRepository;

    @Autowired
    public PlaybackService(PlaybackRepository playbackRepository) {
        this.playbackRepository = playbackRepository;
    }

    public long getPlaybackCount(Sound sound) {
        return playbackRepository.countBySound(sound);
    }

    public Playback savePlayback(Sound sound, @Nullable User user, Instant instant) {
        if (playbackRepository.existsBySoundAndUserAndPlayedAtAfter(sound, user, instant.minus(24, ChronoUnit.HOURS))) {
            return null;
        }
        if (user == null) {
            return null;
        }
        Playback p = new Playback(sound, user, instant);

        return playbackRepository.save(p);
    }
}
