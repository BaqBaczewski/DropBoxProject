package example.audiohive.app.user;

import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class RegistrationDTO {

    @AssertTrue(message = "Please agree to Terms of Service to Sign up")
    private final Boolean toService;

    @NotBlank
    @Size(min = 3, max = 50, message = "Minimum length is 3 characters.")
    private final String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate date;

    @NotBlank
    @Size(min = 5, message = "Minimum length is 5 characters.")
    private final String password1;
    private final String password2;

    public RegistrationDTO(Boolean toService, String name, LocalDate date, String password1, String password2) {
        this.toService = toService;
        this.name = name;
        this.date = date;
        this.password1 = password1;
        this.password2 = password2;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {return date;}

    public String getPassword1() {
        return password1;
    }

    public String getPassword2() {
        return password2;
    }

    public Boolean getToService() {
        return toService;
    }

}
