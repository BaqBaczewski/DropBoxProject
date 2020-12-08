package example.audiohive.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        User user = new User(name, passwordEncoder.encode(plainPassword), role);
        return userRepository.save(user);
    }
}
