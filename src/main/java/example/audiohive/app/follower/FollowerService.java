package example.audiohive.app.follower;

import example.audiohive.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FollowerService {

    private final FollowerRepository followerRepository;

    @Autowired
    public FollowerService(FollowerRepository followerRepository) {
        this.followerRepository = followerRepository;
    }

    /**
     * Returns all users followed by {@code user}
     */
    public Collection<User> findUsersFollowedBy(User user) {
        return followerRepository.findDistinctByFollowingUser(user)
                .stream().map(Follower::getFollowedUser)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns all users following {@code user}
     */
    public Collection<User> findUsersFollowing(User user) {
        return followerRepository.findDistinctByFollowedUser(user)
                .stream().map(Follower::getFollowingUser)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns true if the user {@code follower} is following the user {@code followed}
     */
    public boolean isFollowing(User follower, User followed) {
        return followerRepository.findByFollowingUserAndFollowedUser(follower, followed).isPresent();
    }

    /**
     * If {@code isFollowing} is true, sets the user {@code follower} to be following the user {@code followed},
     * otherwise removes the association.
     */
    public void setFollowing(User follower, User followed, boolean isFollowing) {
        followerRepository.findByFollowingUserAndFollowedUser(follower, followed).ifPresentOrElse(
                f -> {
                    if (!isFollowing) {
                        followerRepository.delete(f);
                    }
                },
                () -> {
                    if (isFollowing) {
                        followerRepository.save(
                                new Follower(followed, follower)
                        );
                    }
                }
        );
    }

}
