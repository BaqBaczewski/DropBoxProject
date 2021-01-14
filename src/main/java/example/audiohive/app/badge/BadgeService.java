package example.audiohive.app.badge;

import example.audiohive.app.playback.PlaybackService;
import example.audiohive.app.sound.Sound;
import example.audiohive.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

@Service
public class BadgeService {
    private final PlaybackService playbackService;

    @Autowired
    public BadgeService(PlaybackService playbackService) {
        this.playbackService = playbackService;

    }

    public List<Badge> getUserBadges(User user, Instant now) {
        List<Badge> badges = new LinkedList<>();

        if (user.getCreatedAt().isAfter(now.minus(24, ChronoUnit.HOURS))) {
            badges.add(new Badge("Newbie", "info"));
        }

        return badges;
    }

    public List<Badge> getSoundBadges(Sound sound, Instant now) {
        if (sound.getCreatedAt().isBefore(now.minus(10, ChronoUnit.DAYS)) && (playbackService.getPlaybackCount(sound) >= 25)) {
            return List.of(new Badge("Evergreen", "success"));
        }
        if (sound.getCreatedAt().isBefore(now.minus(5, ChronoUnit.DAYS))) {
            return List.of(new Badge("Oldie", "light"));
        }
        if (sound.getCreatedAt().isAfter(now.minus(24, ChronoUnit.HOURS)) && (playbackService.getPlaybackCount(sound) >= 10)) {
            return List.of(new Badge("Hit", "primary"));
        } else if (sound.getCreatedAt().isAfter(now.minus(24, ChronoUnit.HOURS))) {
            return List.of(new Badge("Fresh", "info"));
        }
        return List.of();
    }


}
