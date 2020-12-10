package example.audiohive.app.sound;

import example.audiohive.app.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SoundRepository extends JpaRepository<Sound, String>, JpaSpecificationExecutor<Sound> {
    Page<Sound> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Sound> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
