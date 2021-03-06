package example.audiohive.app.profile;

import example.audiohive.app.badge.Badge;
import example.audiohive.app.badge.BadgeService;
import example.audiohive.app.follower.Follower;
import example.audiohive.app.follower.FollowerId;

import example.audiohive.app.follower.FollowerService;
import example.audiohive.app.image.Image;
import example.audiohive.app.image.ImageService;
import example.audiohive.app.sound.Sound;
import example.audiohive.app.sound.SoundService;
import example.audiohive.app.user.User;
import example.audiohive.app.user.UserService;

import example.audiohive.app.videos.Video;
import example.audiohive.app.videos.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Controller
public class ProfileController {

    private final UserService userService;
    private final SoundService soundService;
    private final ImageService imageService;
    private final VideoService videoService;
    private final BadgeService badgeService;
    private final FollowerService followerService;

    @Autowired
    public ProfileController(UserService userService, SoundService soundService, ImageService imageService, VideoService videoService, BadgeService badgeService, FollowerService followerService) {
        this.userService = userService;
        this.soundService = soundService;
        this.imageService = imageService;
        this.videoService = videoService;
        this.badgeService = badgeService;
        this.followerService = followerService;
    }

    @GetMapping("/profile/{userName}")
    public String profile(Model model, @PathVariable String userName, @ModelAttribute("sessionUser") User sessionUser) {
        User user = userService.findByName(userName).orElseThrow();
        model.addAttribute("user", user);

        Page<Sound> newestSounds = soundService.findNewestSoundsByUser(user);
        model.addAttribute("newestSounds", newestSounds);

        Page<Image> newestImages = imageService.findNewestImagesByUser(user);
        model.addAttribute("newestImages", newestImages);

        Page<Video> newestVideos = videoService.findNewestVideoByUser(user);
        model.addAttribute("newestVideos", newestVideos);

        List<Badge> badges = badgeService.getUserBadges(user, Instant.now());
        model.addAttribute("badges", badges);


        boolean isFollowed = followerService.isFollowing(sessionUser, user);
        model.addAttribute("isFollowed", isFollowed);

        Collection<User> followers = followerService.findUsersFollowing(user);
        model.addAttribute("followers", followers.size());

        Collection<User> following =followerService.findUsersFollowedBy(user);
        model.addAttribute("following",following.size());

        return "profile";
    }


}
