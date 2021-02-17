package example.audiohive.app.videos;

import example.audiohive.app.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VideoRepository extends JpaRepository<Video, String>, JpaSpecificationExecutor<Video> {
    Page<Video> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Video> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
