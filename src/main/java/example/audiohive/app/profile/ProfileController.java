package example.audiohive.app.profile;

import example.audiohive.app.badge.Badge;
import example.audiohive.app.badge.BadgeService;
import example.audiohive.app.sound.Sound;
import example.audiohive.app.sound.SoundService;
import example.audiohive.app.user.User;
import example.audiohive.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.List;

@Controller
public class ProfileController {

    private final UserService userService;
    private final SoundService soundService;
    private final BadgeService badgeService;

    @Autowired
    public ProfileController(UserService userService, SoundService soundService, BadgeService badgeService) {
        this.userService = userService;
        this.soundService = soundService;
        this.badgeService = badgeService;
    }

    @GetMapping("/profile/{userName}")
    public String profile(Model model, @PathVariable String userName) {
        User user = userService.findByName(userName).orElseThrow();
        model.addAttribute("user", user);

        Page<Sound> newestSounds = soundService.findNewestSoundsByUser(user);
        model.addAttribute("newestSounds", newestSounds);

        List<Badge> badges = badgeService.getUserBadges(user, Instant.now());
        model.addAttribute("badges", badges);

        return "profile";
    }
}
