package example.audiohive.app.profile;

import example.audiohive.app.sound.SoundService;
import example.audiohive.app.user.User;
import example.audiohive.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {

    private final UserService userService;
    private final SoundService soundService;

    @Autowired
    public ProfileController(UserService userService, SoundService soundService) {
        this.userService = userService;
        this.soundService = soundService;
    }

    @GetMapping("/profile/{userName}")
    public String profile(Model model, @PathVariable String userName) {
        User user = userService.findByName(userName).orElseThrow();
        model.addAttribute("user", user);

        model.addAttribute("newestSounds", soundService.findNewestSoundsByUser(user));

        return "profile";
    }
}
