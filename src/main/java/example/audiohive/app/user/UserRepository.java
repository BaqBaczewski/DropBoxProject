package example.audiohive.app.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByNameIgnoreCase(String name);
    Optional<User> findByName(String name);

    boolean existsByNameIgnoreCase(String name);

    Page<User> findByOrderByCreatedAtDesc(Pageable pageable);
}
