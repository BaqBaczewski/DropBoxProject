package example.audiohive.app.user;

import example.audiohive.app.videos.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
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

    public Page<User> findUser(Pageable pageable) { return userRepository.findAll(pageable); }

    public static List<Integer> createPagination(int currentPage, int numberOfPages) {

        List<Integer> pages = new LinkedList<>();

        pages.add(1);
        pages.add(2);
        pages.add(3);
        pages.add(4);
        pages.add(5);
        pages.add(-1);
        pages.add(numberOfPages);

        System.out.println(pages);

        // only 1 page active
        if (currentPage == 0 && numberOfPages == 1) {
            return List.of(1);
        }
        // only 2 pages active
        if (currentPage >= 0 && numberOfPages == 2) {
            return List.of(1, 2);
        }
        // only 3 pages active
        if (currentPage >= 0 && numberOfPages == 3) {
            return List.of(1, 2, 3);
        }
        // only 4 pages active
        if (currentPage >= 0 && numberOfPages == 4) {
            return List.of(1, 2, 3, 4);
        }
        // only 5 pages active
        if (currentPage == 0) {
            return List.of(1, 2, 3, -1, numberOfPages);
        }
        if (currentPage == 2) {
            return List.of(1, 2, 3, 4, 5, numberOfPages);
        }
        if (currentPage == 3) {
            return List.of(1, 2, 3, 4, 5, numberOfPages);
        }
        if (currentPage == 4) {
            return List.of(1, -1, 3, 4, 5, numberOfPages);
        }
        // 6 and more pages active...
        if (currentPage == 0) { // first page with more than 5 pages active
            return List.of(1, 2, 3, -1, numberOfPages);
        }
        if (currentPage == 1) { // second page with more than 5 pages active
            return List.of(1, 2, 3, 4, -1, numberOfPages);
        }
        if (currentPage == 2 && numberOfPages == 5) { // third page with more than 5 pages active
            return List.of(1, currentPage, currentPage + 1, currentPage + 2, currentPage + 3, -1, numberOfPages);
        }
        if (currentPage == 3 && numberOfPages > 5 ) {
            return List.of(1, currentPage - 1, currentPage, currentPage + 1,currentPage+2, currentPage+3,-1, numberOfPages);
        }
        if (currentPage >= 4 && currentPage < numberOfPages-4) {
            return List.of(1, -1, currentPage - 1, currentPage, currentPage + 1, currentPage + 2, currentPage + 3, -1, numberOfPages);
        }
        if (currentPage >= 4 && currentPage < numberOfPages - 3) {
            return List.of(1, -1, currentPage - 1, currentPage, currentPage + 1, currentPage + 2,currentPage+3, numberOfPages);
        }
        if (currentPage >= 4 && currentPage == numberOfPages - 3) {
            return List.of(1, -1, currentPage - 1, currentPage, currentPage + 1, currentPage + 2, numberOfPages);
        }
        if (currentPage > 4 && currentPage < numberOfPages - 2) {
            return List.of(1, -1, currentPage - 1, currentPage, currentPage + 1, currentPage + 2, numberOfPages);
        }
        if (currentPage > 4 && currentPage == numberOfPages - 2) {
            return List.of(1, -1, currentPage - 1, currentPage, currentPage + 1, numberOfPages);
        }
        if (currentPage > 4 && currentPage < numberOfPages - 1) {
            return List.of(1, -1, currentPage - 1, currentPage, currentPage + 1, numberOfPages);
        }
        if (numberOfPages == currentPage +1) { // when last page reached
            return List.of(1, -1, currentPage - 1, currentPage, numberOfPages);
        }
        return null;
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public Page<User> findNewestUsers(Pageable pageable) {
        return userRepository.findByOrderByCreatedAtDesc(pageable);
    }
}
