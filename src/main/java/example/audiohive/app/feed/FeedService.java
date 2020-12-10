package example.audiohive.app.feed;

import example.audiohive.app.follower.Follower;
import example.audiohive.app.follower.FollowerService;
import example.audiohive.app.follower.Follower_;
import example.audiohive.app.sound.Sound;
import example.audiohive.app.sound.SoundRepository;
import example.audiohive.app.sound.Sound_;
import example.audiohive.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

@Service
public class FeedService {

    private final SoundRepository soundRepository;
    private final FollowerService followerService;

    @Autowired
    public FeedService(SoundRepository soundRepository, FollowerService followerService) {
        this.soundRepository = soundRepository;
        this.followerService = followerService;
    }

    public Page<Sound> getFeed(User user, Pageable pageable) {

        Specification<Sound> specification = (root, query, criteriaBuilder) -> {
            Subquery<Follower> subQuery = query.subquery(Follower.class);
            Root<Follower> sqRoot = subQuery.from(Follower.class);

            subQuery.select(sqRoot).where(
                    criteriaBuilder.equal(sqRoot.get(Follower_.followedUser), subQuery.correlate(root).get(Sound_.user)),
                    criteriaBuilder.equal(sqRoot.get(Follower_.followingUser), user)
            );

            query.orderBy(criteriaBuilder.desc(root.get(Sound_.createdAt)));
            return criteriaBuilder.exists(subQuery);
        };

        return soundRepository.findAll(specification, pageable);
    }

}
