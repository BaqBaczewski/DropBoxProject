package example.audiohive.app.follower;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FollowerId implements Serializable {
    private String followedUserId;

    private String followingUserId;

    public FollowerId() {
    }

    public FollowerId(String followedUserId, String followingUserId) {
        this.followedUserId = followedUserId;
        this.followingUserId = followingUserId;
    }

    public String getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(String followedUserId) {
        this.followedUserId = followedUserId;
    }

    public String getFollowingUserId() {
        return followingUserId;
    }

    public void setFollowingUserId(String followingUserId) {
        this.followingUserId = followingUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowerId that = (FollowerId) o;
        return followedUserId.equals(that.followedUserId) && followingUserId.equals(that.followingUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followedUserId, followingUserId);
    }
}
