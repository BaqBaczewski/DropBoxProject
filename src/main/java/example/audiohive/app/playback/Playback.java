package example.audiohive.app.playback;

import example.audiohive.app.sound.Sound;
import example.audiohive.app.user.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
public class Playback {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @ManyToOne()
    private Sound sound;

    @ManyToOne()
    private User user;

    private Instant playedAt;

    public Playback() {
    }

    public Playback(Sound sound, User user, Instant playedAt) {
        this.sound = sound;
        this.user = user;
        this.playedAt = playedAt;
    }

    public String getId() {
        return id;
    }

    public Sound getSound() {
        return sound;
    }

    public User getUser() {
        return user;
    }

    public Instant getPlayedAt() {
        return playedAt;
    }
}
