package example.audiohive.app.videos;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class UploadVideoDTO {
    @NotBlank
    @Size(min = 5, message = "A title must consist of at least 5 characters.")
    private String title;

    private MultipartFile file;

    @NotBlank
    @Size(max = 10000, message = "The maximum length of a description is 10000 characters")
    private String description;

    public UploadVideoDTO(String title, MultipartFile file, String description) {
        this.title = title;
        this.file = file;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
