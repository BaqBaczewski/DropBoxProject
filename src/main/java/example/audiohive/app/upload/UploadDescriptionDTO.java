package example.audiohive.app.upload;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class UploadDescriptionDTO {

    @NotBlank
    @Size(max = 10000, message = "The maximum length of a description is 10000 characters")
    private String newDescription;

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }

}
