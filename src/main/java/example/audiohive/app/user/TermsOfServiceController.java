package example.audiohive.app.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TermsOfServiceController {

    @GetMapping("/termsOfService")
    public String termsOfService(Model model) {
        model.addAttribute("name", "Satan");
        model.addAttribute("city", "Depths of Hell");
       return "termsOfService";
    }
}
