package example.audiohive.app.home;

import example.audiohive.app.sound.SoundService;
import example.audiohive.app.user.UserService;
import example.audiohive.app.videos.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final SoundService soundService;
    private final UserService userService;
    private final VideoService videoService;

    @Autowired
    public HomeController(SoundService soundService, VideoService videoService, UserService userService) {
        this.soundService = soundService;
        this.userService = userService;
        this.videoService = videoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("newUsers", userService.findNewestUsers(PageRequest.of(0, 5)));
        model.addAttribute("newSounds", soundService.findNewSounds(PageRequest.of(0, 5)));
        model.addAttribute("videos", videoService.findVideo(PageRequest.of(0, 5)));
        return "home";
    }
}
