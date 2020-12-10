package example.audiohive.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean userNameTaken(String name) {
        return userRepository.existsByNameIgnoreCase(name);
    }

    public User createUser(String name, String plainPassword, User.Role role) {
        return createUser(name, plainPassword, role, Instant.now());
    }

    public User createUser(String name, String plainPassword, User.Role role, Instant createdAt) {
        User user = new User(name, passwordEncoder.encode(plainPassword), role, createdAt);
        return userRepository.save(user);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }
}
