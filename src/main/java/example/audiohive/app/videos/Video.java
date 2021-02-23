package example.audiohive.app.videos;

import example.audiohive.app.user.User;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.sql.Blob;
import java.time.Instant;

@Entity
@Table(
        indexes = {
                @Index(columnList = "createdAt")
        }
)

public class Video {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String title;

    @Lob
    private String description;

    @Lob()
    private Blob videoData;

    @ManyToOne()
    private User user;

    private Instant createdAt;

    public Video() {
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {this.title = title;}

    public Video(String title, Blob videoData, Instant createdAt, User user, String description) {
        this.title = title;
        this.videoData = videoData;
        this.createdAt = createdAt;
        this.user = user;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Blob getVideoData() {
        return videoData;
    }

    public User getUser() {
        return user;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Sound{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}


