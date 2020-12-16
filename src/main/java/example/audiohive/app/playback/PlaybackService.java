package example.audiohive.app.playback;

import example.audiohive.app.sound.Sound;
import example.audiohive.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.Instant;

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
        Playback p = new Playback(sound, user, instant);

        return playbackRepository.save(p);
    }
}
