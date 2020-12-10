package example.audiohive.app.home;

import example.audiohive.app.sound.SoundService;
import example.audiohive.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final SoundService soundService;
    private final UserService userService;

    @Autowired
    public HomeController(SoundService soundService, UserService userService) {
        this.soundService = soundService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("newUsers", userService.findNewestUsers(PageRequest.of(0, 5)));
        model.addAttribute("newSounds", soundService.findNewSounds(PageRequest.of(0, 5)));
        return "home";
    }
}
