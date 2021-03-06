package example.audiohive.app.badge;

import example.audiohive.app.follower.FollowerService;
import example.audiohive.app.playback.PlaybackService;
import example.audiohive.app.sound.Sound;
import example.audiohive.app.sound.SoundService;
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
    private final FollowerService followerService;
    private final SoundService soundService;

    @Autowired
    public BadgeService(FollowerService followerService, SoundService soundService, PlaybackService playbackService) {
        this.followerService = followerService;
        this.soundService = soundService;
        this.playbackService = playbackService;
    }

    public List<Badge> getUserBadges(User user, Instant now) {
        List<Badge> badges = new LinkedList<>();

        if (user.getCreatedAt().isAfter(now.minus(24, ChronoUnit.HOURS))) {
            badges.add(new Badge("Newbie", "info"));
        }

        if (user.getCreatedAt().isBefore(now.minus(90, ChronoUnit.DAYS)) && soundService.findNewestSoundsByUser(user).getTotalElements() == 0) {
            badges.add(new Badge("Patron", "light"));
        }

        if (followerService.findUsersFollowing(user).size() >= 5) {
            badges.add(new Badge("Influencer", "info"));
        }

        if (followerService.findUsersFollowedBy(user).size() >= 1 && followerService.findUsersFollowing(user).size() < 5) {
            badges.add(new Badge("Fan", "success"));
        }

        if (soundService.findNewestSoundsByUser(user).getTotalElements() >= 1) {
            badges.add(new Badge("Artist", "primary"));
        }

        return badges;
    }

    public List<Badge> getSoundBadges(Sound sound, Instant now) {

        if (sound.getCreatedAt().isAfter(now.minus(24, ChronoUnit.HOURS)) && (playbackService.getPlaybackCount(sound) < 5)) {
            return List.of(new Badge("Fresh", "info"),new Badge("Underground", "dark"));
        }
        if (sound.getCreatedAt().isAfter(now.minus(24, ChronoUnit.HOURS)) && (playbackService.getPlaybackCount(sound) >= 10)) {
            return List.of(new Badge("Hit", "primary"));
        }
        if (sound.getCreatedAt().isAfter(now.minus(24, ChronoUnit.HOURS))) {
            return List.of(new Badge("Fresh", "info"));
        }
        if ((playbackService.getPlaybackCount(sound) < 5) && sound.getCreatedAt().isBefore(now.minus(5, ChronoUnit.DAYS))) {
            return List.of(new Badge("Oldie", "light"), new Badge("Underground", "dark"));
        }

        if (sound.getCreatedAt().isBefore(now.minus(10, ChronoUnit.DAYS)) && (playbackService.getPlaybackCount(sound) >= 25)) {
            return List.of(new Badge("Evergreen", "success"));
        }

        if (sound.getCreatedAt().isBefore(now.minus(5, ChronoUnit.DAYS))) {
            return List.of(new Badge("Oldie", "light"));
        }

        if ((playbackService.getPlaybackCount(sound) < 5)) {
            return List.of(new Badge("Underground", "dark"));
        }
        return List.of();
    }


}
