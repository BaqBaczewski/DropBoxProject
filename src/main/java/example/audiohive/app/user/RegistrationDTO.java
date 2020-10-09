package example.audiohive.app.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegistrationDTO {
    @NotBlank
    @Size(min = 3, max = 50, message = "Minimum length is 3 characters.")
    private final String name;

    @NotBlank
    @Size(min = 5, message = "Minimum length is 5 characters.")
    private final String password1;
    private final String password2;

    public RegistrationDTO(String name, String password1, String password2) {
        this.name = name;
        this.password1 = password1;
        this.password2 = password2;
    }

    public String getName() {
        return name;
    }

    public String getPassword1() {
        return password1;
    }

    public String getPassword2() {
        return password2;
    }
}
