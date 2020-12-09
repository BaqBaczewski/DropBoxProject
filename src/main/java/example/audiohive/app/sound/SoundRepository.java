package example.audiohive.app.sound;

import example.audiohive.app.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SoundRepository extends JpaRepository<Sound, String> {
    Page<Sound> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
