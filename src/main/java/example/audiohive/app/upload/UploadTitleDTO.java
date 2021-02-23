package example.audiohive.app.upload;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class UploadTitleDTO {



    @NotBlank
    @Size(min = 5, message = "A title must consist of at least 5 characters.")
    private String newTitle;

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

}
