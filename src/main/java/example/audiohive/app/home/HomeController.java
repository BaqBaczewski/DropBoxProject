package example.audiohive.app.home;

import example.audiohive.app.image.ImageService;
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
    private final ImageService imageService;

    @Autowired
    public HomeController(SoundService soundService, UserService userService, ImageService imageService) {
        this.soundService = soundService;
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("newUsers", userService.findNewestUsers(PageRequest.of(0, 5)));
        model.addAttribute("newSounds", soundService.findNewSounds(PageRequest.of(0, 5)));
        model.addAttribute( "newImages", imageService.findNewImages(PageRequest.of(0, 5)));
        return "home";
    }

}