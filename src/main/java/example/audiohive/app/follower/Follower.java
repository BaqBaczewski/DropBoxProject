package example.audiohive.app.follower;

import example.audiohive.app.user.User;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class Follower {

    @EmbeddedId
    private FollowerId id = new FollowerId();

    @ManyToOne
    @MapsId("followedUserId")
    private User followedUser;

    @ManyToOne
    @MapsId("followingUserId")
    private User followingUser;

    public Follower() {}

    public Follower(User followedUser, User followingUser) {
        this.followedUser = followedUser;
        this.followingUser = followingUser;
    }

    public User getFollowedUser() {
        return followedUser;
    }

    public User getFollowingUser() {
        return followingUser;
    }
}