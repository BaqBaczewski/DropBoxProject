package example.audiohive.app.badge;

import example.audiohive.app.user.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

@Service
public class BadgeService {

    public List<Badge> getUserBadges(User user, Instant now) {
        List<Badge> badges = new LinkedList<>();

        if (user.getCreatedAt().isAfter(now.minus(24, ChronoUnit.HOURS))) {
            badges.add(new Badge("Newbie", "info"));
        }

        return badges;
    }

}
