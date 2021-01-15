package example.audiohive.app.follower;

import example.audiohive.app.user.User;
import example.audiohive.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FollowerController {
    private final FollowerService followerService;
    private final UserService userService;

    @Autowired
    public FollowerController(FollowerService followerService, UserService userService) {
        this.followerService = followerService;
        this.userService = userService;
    }

    @PostMapping("/follow/{userName}")
    public String follow(@ModelAttribute("sessionUser") User sessionUser, @PathVariable String userName) {
        User user = userService.findByName(userName).orElseThrow();
        boolean isFollowed = followerService.isFollowing(sessionUser, user);
        followerService.setFollowing(sessionUser, user, !isFollowed);
        return "redirect:/profile/" + userName;
    }

}
