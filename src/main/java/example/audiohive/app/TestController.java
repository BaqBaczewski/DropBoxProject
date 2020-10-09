package example.audiohive.app;

import example.audiohive.app.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class TestController {

    @GetMapping("/test")
    @PreAuthorize("isAuthenticated()")
    public String test(@ModelAttribute("sessionUser") User sessionUser) {

        return "test";
    }

}
