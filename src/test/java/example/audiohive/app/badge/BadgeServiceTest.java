package example.audiohive.app.badge;

import example.audiohive.app.TestHelper;
import example.audiohive.app.sound.SoundService;
import example.audiohive.app.user.User;
import example.audiohive.app.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BadgeServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private SoundService soundService;

    @Autowired
    private BadgeService badgeService;

    @Test
    public void testUserBadgeNewbie() {
        Instant now = Instant.now();

        User oldUser = userService.createUser("old user", "123", User.Role.USER, now.minus(4, ChronoUnit.DAYS));
        User newUser = userService.createUser("new user", "123", User.Role.USER, now.minus(2, ChronoUnit.HOURS));

        List<Badge> oldUserBadges = badgeService.getUserBadges(oldUser, now);
        List<Badge> newUserBadges = badgeService.getUserBadges(newUser, now);

        assertThat(oldUserBadges).isEmpty();

        assertThat(newUserBadges).hasSize(1);
        assertThat(newUserBadges.get(0).getLabel()).isEqualTo("Newbie");
        assertThat(newUserBadges.get(0).getColor()).isEqualTo("info");
    }

    @Test
    public void testUserBadgeVeryOldUser() {

        Instant now = Instant.now();

        User user = userService.createUser("very old user", "123", User.Role.USER, now.minus(120, ChronoUnit.DAYS));

        List<Badge> badges = badgeService.getUserBadges(user, now);

        assertThat(badges).isEmpty();
    }

    @Test
    public void testUserBadgeNewbieWithSound() {

        Instant now = Instant.now();

        User newUser1 = userService.createUser("user1", "123", User.Role.USER, now.minus(3, ChronoUnit.HOURS));
        User newUser2 = userService.createUser("user2", "123", User.Role.USER, now.minus(2, ChronoUnit.HOURS));

        soundService.create("new sound", TestHelper.dummyAudioData(), 4, newUser1, now);

        List<Badge> user1Badges = badgeService.getUserBadges(newUser1, now);
        List<Badge> user2Badges = badgeService.getUserBadges(newUser2, now);

        assertThat(user1Badges).hasSize(1);
        assertThat(user1Badges.get(0).getLabel()).isEqualTo("Newbie");
        assertThat(user1Badges.get(0).getColor()).isEqualTo("info");

        assertThat(user2Badges).hasSize(1);
        assertThat(user2Badges.get(0).getLabel()).isEqualTo("Newbie");
        assertThat(user2Badges.get(0).getColor()).isEqualTo("info");

    }

}
