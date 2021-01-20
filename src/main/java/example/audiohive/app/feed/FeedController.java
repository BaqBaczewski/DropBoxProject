package example.audiohive.app.feed;

import example.audiohive.app.user.*;
import example.audiohive.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FeedController {

    private final FeedService feedService;
    private final UserService userService;

    @Autowired
    public FeedController(FeedService feedService, UserService userService) {
        this.feedService = feedService;
        this.userService = userService;
    }

    @GetMapping("/feed")
    public String Feed(Model model, @ModelAttribute("sessionUser") User sessionUser, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        User user = userService.findByName(sessionUser.getName()).orElseThrow();
        model.addAttribute("sounds", feedService.getFeed(user, pageable));
        return "feed";
    }
}