package example.audiohive.app.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TermsOfServiceController {

    @GetMapping("/termsOfService")
    public String termsOfService(Model model) {
        model.addAttribute("name", "example_name");
        model.addAttribute("city", "example_city");
       return "termsOfService";
    }

    @GetMapping("/about")
    public String aboutUs(Model model) {
        model.addAttribute("name", "example_name");
        model.addAttribute("city", "example_city");
        return "about";
    }
    @GetMapping("/contacts")
    public String contacts(Model model) {
        model.addAttribute("name", "example_name");
        model.addAttribute("city", "example_city");
        return "contacts";
    }
}
