package example.audiohive.app.follower;

import example.audiohive.app.user.User;
import example.audiohive.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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

    @GetMapping("/followers/{userName}")
    public String followersList(Model model, @PathVariable String userName, @RequestParam(defaultValue = "0") int page){

       Pageable pageable = PageRequest.of(page, 10);
       User user = userService.findByName(userName).orElseThrow();

       Collection<User> followers = followerService.findUsersFollowing(user);
       model.addAttribute("followersList", followerService.findUsersFollowing(user));


        return "followers";

    }

    @GetMapping("/following/{userName}")
    public String followingList(Model model, @PathVariable String userName, @RequestParam(defaultValue = "0") int page){

        Pageable pageable = PageRequest.of(page, 10);
        User user = userService.findByName(userName).orElseThrow();


        Collection<User> following =followerService.findUsersFollowedBy(user);
        model.addAttribute("followingList",followerService.findUsersFollowedBy(user));
        return "following";

    }
}
