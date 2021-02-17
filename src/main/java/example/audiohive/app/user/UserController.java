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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String register(Model model) {

        RegistrationDTO registrationDTO = new RegistrationDTO(false, "", LocalDate.now(), "", "");
        model.addAttribute("registration", registrationDTO);

        return "signup";
    }

    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute("registration") RegistrationDTO registrationDTO,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (!registrationDTO.getPassword1().equals(registrationDTO.getPassword2())) {
            bindingResult.addError(new FieldError("registration", "password2", "Does not match"));
        }

        if (ChronoUnit.YEARS.between(registrationDTO.getDate(), LocalDate.now()) < 18) {
            bindingResult.addError(new FieldError("registration", "date", "You are not old enough"));
        }    

        if (userService.userNameTaken(registrationDTO.getName())) {
            bindingResult.addError(new FieldError("registration", "name", "User name already taken"));
        }
        if (registrationDTO.getToService() == null) {
            bindingResult.addError(new FieldError("registration", "toService", "Please agree to Terms of Service to Sign up"));
        }

        String allowedCharacters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789_";
//        String allowedCharacters = "[a-zA-Z0-9_]";

        boolean isAllowed = true;
        for (int i = 0; i < registrationDTO.getName().length(); i++) {
            boolean isFound = false;
            char checkedCharacter = registrationDTO.getName().charAt(i);
            for (int j = 0; j < allowedCharacters.length(); j++) {
                if (checkedCharacter == allowedCharacters.charAt(j)) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                isAllowed = false;
            }
        }
        if (!isAllowed) {
            bindingResult.addError(new FieldError("registration", "name", "The name can only contain Latin lower and upper case letters, numbers 0-9 and _"));
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
