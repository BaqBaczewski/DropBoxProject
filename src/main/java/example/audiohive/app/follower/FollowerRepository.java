package example.audiohive.app.follower;

import example.audiohive.app.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, FollowerId> {
    List<Follower> findDistinctByFollowedUser(User followedUser);
    List<Follower> findDistinctByFollowingUser(User followedUser);
    Optional<Follower> findByFollowingUserAndFollowedUser(User followingUser, User followedUser);
}
