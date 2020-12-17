package example.audiohive.app.user;

import example.audiohive.app.follower.Follower;
import example.audiohive.app.sound.Sound;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {User_.NAME})
)
public class User {

    public enum Role {
        USER, ADMIN,
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String name;

    private String encodedPassword;

    private Role role;

    private Instant createdAt;

    @OneToMany(mappedBy = "user")
    private Set<Sound> sounds = Collections.emptySet();

    @OneToMany(mappedBy = "followedUser")
    private Set<Follower> followers = Collections.emptySet();

    @OneToMany(mappedBy = "followingUser")
    private Set<Follower> following = Collections.emptySet();

    public User() {
    }

    public User(String name, String encodedPassword, Role role, Instant createdAt) {
        this.name = name;
        this.encodedPassword = encodedPassword;
        this.role = role;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public Role getRole() {
        return role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Set<Sound> getSounds() {
        return sounds;
    }

    public Set<Follower> getFollowers() {
        return followers;
    }

    public Set<Follower> getFollowing() {
        return following;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
    }
}
