package example.audiohive.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String register(Model model) {
        model.addAttribute("registration", new RegistrationDTO("", "", ""));
        return "signup";
    }

    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute("registration") RegistrationDTO registrationDTO,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (!registrationDTO.getPassword1().equals(registrationDTO.getPassword2())) {
            bindingResult.addError(new FieldError("registration", "password2", "Does not match"));
        }

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        userService.createUser(
                registrationDTO.getName(), registrationDTO.getPassword1(), User.Role.USER
        );

        redirectAttributes.addAttribute("registered", true);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam(defaultValue = "aaa") String error) {
        model.addAttribute("error", !error.isEmpty());
        return "login";
    }
}
