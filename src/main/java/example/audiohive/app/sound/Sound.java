package example.audiohive.app.sound;

import example.audiohive.app.user.User;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.InputStream;
import java.sql.Blob;
import java.time.Instant;

@Entity
public class Sound {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String title;

    @Lob()
    private Blob audioData;

    @ManyToOne()
    private User user;

    private Instant createdAt;

    public Sound() {
    }

    public Sound(String title, Blob audioData, Instant createdAt, User user) {
        this.title = title;
        this.audioData = audioData;
        this.createdAt = createdAt;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Blob getAudioData() {
        return audioData;
    }

    public User getUser() {
        return user;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
