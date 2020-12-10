package example.audiohive.app.feed;

import example.audiohive.app.follower.FollowerService;
import example.audiohive.app.sound.Sound;
import example.audiohive.app.sound.SoundRepository;
import example.audiohive.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Sound> sounds = followerService.findUsersFollowedBy(user).stream()
                .flatMap(u -> soundRepository.findAllByUserOrderByCreatedAtDesc(u, Pageable.unpaged()).stream())
                .sorted(Comparator.comparing(Sound::getCreatedAt).reversed())
                .collect(Collectors.toUnmodifiableList());

        int fromIndex = Math.max(0, Math.min(sounds.size() - 1, (int) pageable.getOffset()));
        int toIndex = Math.max(0, Math.min(sounds.size(), (int) pageable.getOffset() + pageable.getPageSize()));

        List<Sound> page = sounds.subList(fromIndex, toIndex);

        return new PageImpl<>(page, pageable, sounds.size());
    }

}
