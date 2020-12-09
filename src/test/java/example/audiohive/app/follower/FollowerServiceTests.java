package example.audiohive.app.follower;

import example.audiohive.app.user.User;
import example.audiohive.app.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class FollowerServiceTests {

    @Autowired
    private FollowerService followerService;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    void testFollowers() {

        User user1 = userService.createUser("user1", "123", User.Role.USER);
        User user2 = userService.createUser("user2", "123", User.Role.USER);
        User user3 = userService.createUser("user3", "123", User.Role.USER);
        User user4 = userService.createUser("user4", "123", User.Role.USER);

        // nobody follows anybody at the beginning
        assertFalse(followerService.isFollowing(user1, user2));
        assertFalse(followerService.isFollowing(user2, user1));
        assertAll(List.of(user1, user2, user3, user4).stream().map(user -> () -> {
            assertThat(followerService.findUsersFollowedBy(user)).isEmpty();
            assertThat(followerService.findUsersFollowing(user)).isEmpty();
        }));

        // user1 starts following user2
        followerService.setFollowing(user1, user2, true);
        assertTrue(followerService.isFollowing(user1, user2));
        assertThat(followerService.findUsersFollowing(user2)).containsExactly(user1);
        assertThat(followerService.findUsersFollowedBy(user1)).containsExactly(user2);
        assertThat(followerService.findUsersFollowing(user1)).isEmpty();
        assertThat(followerService.findUsersFollowedBy(user2)).isEmpty();

        // user1 starts following user3
        followerService.setFollowing(user1, user3, true);
        assertTrue(followerService.isFollowing(user1, user3));
        assertThat(followerService.findUsersFollowing(user2)).containsExactly(user1);
        assertThat(followerService.findUsersFollowing(user3)).containsExactly(user1);
        assertThat(followerService.findUsersFollowedBy(user1)).containsExactlyInAnyOrder(user2, user3);
        assertThat(followerService.findUsersFollowing(user1)).isEmpty();
        assertThat(followerService.findUsersFollowedBy(user2)).isEmpty();
        assertThat(followerService.findUsersFollowedBy(user3)).isEmpty();

        // user4 starts following user1
        followerService.setFollowing(user4, user1, true);
        assertTrue(followerService.isFollowing(user4, user1));
        assertThat(followerService.findUsersFollowing(user1)).containsExactly(user4);
        assertThat(followerService.findUsersFollowedBy(user1)).containsExactlyInAnyOrder(user2, user3);
        assertThat(followerService.findUsersFollowing(user4)).isEmpty();

        // user1 starts following user4
        followerService.setFollowing(user1, user4, true);
        assertTrue(followerService.isFollowing(user1, user4));
        assertTrue(followerService.isFollowing(user4, user1));
        assertThat(followerService.findUsersFollowing(user1)).containsExactly(user4);
        assertThat(followerService.findUsersFollowing(user4)).containsExactly(user1);
        assertThat(followerService.findUsersFollowedBy(user1)).containsExactlyInAnyOrder(user2, user3, user4);

        // user 1 stops following user3
        followerService.setFollowing(user1, user3, false);
        assertFalse(followerService.isFollowing(user1, user3));
        assertFalse(followerService.isFollowing(user3, user1));
        assertTrue(followerService.isFollowing(user1, user4));
        assertThat(followerService.findUsersFollowedBy(user1)).containsExactlyInAnyOrder(user2, user4);

        // user1 stops following user2 and user4
        followerService.setFollowing(user1, user2, false);
        followerService.setFollowing(user1, user4, false);
        assertThat(followerService.findUsersFollowedBy(user1)).isEmpty();
        assertFalse(followerService.isFollowing(user1, user2));
        assertFalse(followerService.isFollowing(user1, user4));
        assertTrue(followerService.isFollowing(user4, user1));
        assertThat(followerService.findUsersFollowedBy(user4)).containsExactly(user1);

        // user1 starts following user2 again
        followerService.setFollowing(user1, user2, true);
        assertTrue(followerService.isFollowing(user1, user2));
        assertThat(followerService.findUsersFollowing(user2)).containsExactly(user1);
        assertThat(followerService.findUsersFollowedBy(user1)).containsExactly(user2);
        assertThat(followerService.findUsersFollowing(user1)).containsExactly(user4);
        assertThat(followerService.findUsersFollowedBy(user2)).isEmpty();
    }

    @Test
    @Transactional
    void testFollowingTwice() {
        User user1 = userService.createUser("user1", "123", User.Role.USER);
        User user2 = userService.createUser("user2", "123", User.Role.USER);

        // user1 starts following user2 twice
        followerService.setFollowing(user1, user2, true);
        followerService.setFollowing(user1, user2, true);

        // should only be in list once
        assertThat(followerService.findUsersFollowing(user2)).containsExactly(user1);

        // user1 unfollows user2
        followerService.setFollowing(user1, user2, false);
        assertThat(followerService.findUsersFollowing(user2)).isEmpty();

    }
}
