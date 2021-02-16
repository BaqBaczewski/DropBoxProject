package example.audiohive.app.image;

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

public class Image {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String title;

    @Lob
    private String description;

    @Lob()
    private Blob imageData;

    @ManyToOne()
    private User user;

    private Instant createdAt;

    public Image() {
    }

    public Image(String title, Blob imageData, Instant createdAt, User user, String description) {
        this.title = title;
        this.imageData = imageData;
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

    public Blob getImageData() {
        return imageData;
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
        return "Image{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
