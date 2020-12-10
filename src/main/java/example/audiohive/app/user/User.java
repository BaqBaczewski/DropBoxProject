package example.audiohive.app.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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

    public User() {

    }

    public User(String name, String encodedPassword, Role role) {
        this.name = name;
        this.encodedPassword = encodedPassword;
        this.role = role;
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
    }
}
