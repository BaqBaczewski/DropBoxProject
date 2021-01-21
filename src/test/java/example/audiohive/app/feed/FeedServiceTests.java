package example.audiohive.app.feed;

import example.audiohive.app.follower.FollowerService;
import example.audiohive.app.sound.Sound;
import example.audiohive.app.sound.SoundService;
import example.audiohive.app.user.User;
import example.audiohive.app.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

import static example.audiohive.app.TestHelper.dummyAudioData;
import static example.audiohive.app.TestHelper.later;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FeedServiceTests {

    @Autowired
    private FollowerService followerService;

    @Autowired
    private UserService userService;

    @Autowired
    private SoundService soundService;

    @Autowired
    private FeedService feedService;

    @Test
    @Transactional
    public void testFeeds() {

        User user1 = userService.createUser("user1", "123", User.Role.USER);
        User user2 = userService.createUser("user2", "123", User.Role.USER);
        User user3 = userService.createUser("user3", "123", User.Role.USER);
        User user4 = userService.createUser("user4", "123", User.Role.USER);

        Sound u1s1 = soundService.create("U1S1", dummyAudioData(), 4, user1, later(), "new description");
        Sound u2s1 = soundService.create("U2S1", dummyAudioData(), 4, user2, later(), "new description");

        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).isEmpty();
        assertThat(feedService.getFeed(user2, PageRequest.of(0, 10))).isEmpty();

        // user1 starts following user2
        followerService.setFollowing(user1, user2, true);
        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u2s1);
        assertThat(feedService.getFeed(user2, PageRequest.of(0, 10))).isEmpty();

        // user2 starts following user1
        followerService.setFollowing(user2, user1, true);
        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u2s1);
        assertThat(feedService.getFeed(user2, PageRequest.of(0, 10))).containsExactly(u1s1);

        // user1 starts following user3
        followerService.setFollowing(user1, user3, true);
        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u2s1);

        Sound u3s1 = soundService.create("U3S1", dummyAudioData(), 4, user3, later(), "new description");

        // new sound should be first in list
        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u3s1, u2s1);

        Sound u2s2 = soundService.create("U2S2", dummyAudioData(), 4, user2, later(), "new description");
        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u2s2, u3s1, u2s1);

        Sound u4s1 = soundService.create("U4S1", dummyAudioData(), 4, user4, later(), "new description");
        Sound u4s2 = soundService.create("U4S2", dummyAudioData(), 4, user4, later(), "new description");
        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u2s2, u3s1, u2s1);
        followerService.setFollowing(user1, user4, true);
        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u4s2, u4s1, u2s2, u3s1, u2s1);

        // user 1 stops following user 2
        followerService.setFollowing(user1, user2, false);
        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u4s2, u4s1, u3s1);

        // more sounds by user2
        Sound u2s3 = soundService.create("U2S3", dummyAudioData(), 4, user2, later(), "new description");
        Sound u2s4 = soundService.create("U2S4", dummyAudioData(), 4, user2, later(), "new description");
        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u4s2, u4s1, u3s1);

        // user 1 starts following user 2 again
        followerService.setFollowing(user1, user2, true);
        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u2s4, u2s3, u4s2, u4s1, u2s2, u3s1, u2s1);

        // check paging
        Page<Sound> p1 = feedService.getFeed(user1, PageRequest.of(0, 3));
        assertThat(p1).containsExactly(u2s4, u2s3, u4s2);
        assertThat(p1.getTotalElements()).isEqualTo(7);
        assertThat(p1.getTotalPages()).isEqualTo(3);
        Page<Sound> p2 = feedService.getFeed(user1, PageRequest.of(1, 3));
        assertThat(p2.getTotalElements()).isEqualTo(7);
        assertThat(p2.getTotalPages()).isEqualTo(3);
        assertThat(p2.getPageable().getPageNumber()).isEqualTo(1);
        assertThat(p2).containsExactly(u4s1, u2s2, u3s1);

    }

    @Test
    @Transactional
    public void testFeedsDeleteSound() {

        User user1 = userService.createUser("user1", "123", User.Role.USER);
        User user2 = userService.createUser("user2", "123", User.Role.USER);

        Sound u1s1 = soundService.create("U1S1", dummyAudioData(), 4, user1, later(), "new description");
        Sound u2s1 = soundService.create("U2S1", dummyAudioData(), 4, user2, later(), "new description");
        Sound u2s2 = soundService.create("U2S2", dummyAudioData(), 4, user2, later(), "new description");

        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).isEmpty();
        assertThat(feedService.getFeed(user2, PageRequest.of(1, 10))).isEmpty();

        followerService.setFollowing(user1, user2, true);
        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u2s2, u2s1);
        assertThat(feedService.getFeed(user2, PageRequest.of(1, 10))).isEmpty();

        followerService.setFollowing(user2, user1, true);
        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u2s2, u2s1);
        assertThat(feedService.getFeed(user2, PageRequest.of(0, 10))).containsExactly(u1s1);

        soundService.delete(u2s1);

        assertThat(feedService.getFeed(user1, PageRequest.of(0, 10))).containsExactly(u2s2);

    }

}
