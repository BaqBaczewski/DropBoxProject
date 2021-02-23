package example.audiohive.app.image;

import example.audiohive.app.badge.Badge;
import example.audiohive.app.badge.BadgeService;
import example.audiohive.app.playback.PlaybackService;
import example.audiohive.app.sound.Sound;
import example.audiohive.app.sound.SoundService;
import example.audiohive.app.upload.UploadDescriptionDTO;
import example.audiohive.app.upload.UploadTitleDTO;
import example.audiohive.app.user.User;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Controller
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/newImages")
    public String newImages(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Image> imagePage = imageService.findNewImages(pageable);
        model.addAttribute("images", imagePage);

        List<Integer> pagination = ImageService.createPagination(imagePage.getNumber(), imagePage.getTotalPages());
        model.addAttribute("pagination", pagination);
        return "newImages";
    }

    @GetMapping("/imageData/{imageId}")
    public ResponseEntity<InputStreamResource> imageData(@PathVariable String imageId) throws SQLException {
        Optional<Image> optionalImage = imageService.findById(imageId);

        if (optionalImage.isPresent()) {
            Blob imageBlob = optionalImage.get().getImageData();

            return ResponseEntity.ok()
                    .contentLength(imageBlob.length())
                    .contentType(new MediaType("image", "jpg"))
                    .body(new InputStreamResource(imageBlob.getBinaryStream()));
        }
        return ResponseEntity.notFound().build();
    }

//
    @GetMapping("/image/{imageId}")
    public String imageDetails(Model model, @PathVariable("imageId") Image image) {
        model.addAttribute("image", image);

        UploadDescriptionDTO uploadDescriptionDTO = new UploadDescriptionDTO();
        uploadDescriptionDTO.setNewDescription(image.getDescription());
        model.addAttribute("newDescription", uploadDescriptionDTO);

        UploadTitleDTO uploadTitleDTO = new UploadTitleDTO();
        uploadTitleDTO.setNewTitle(image.getTitle());
        model.addAttribute("newTitle", uploadTitleDTO);

        return "imageDetails";
    }

    @PostMapping("/deleteImage/{imageId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #image.user.name eq authentication.name")
    public String deleteImage(@PathVariable("imageId") Image image, RedirectAttributes redirectAttributes) {
        imageService.delete(image);

        redirectAttributes.addAttribute("imageDeleted", true);
        return "redirect:/";
    }

    @PostMapping("/changeDescription/{imageId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #image.user.name eq authentication.name")
    public String changeDescription(@PathVariable("imageId") Image image, UploadDescriptionDTO newDescription, RedirectAttributes redirectAttributes) {
        imageService.changeDescription(image.getId(), newDescription.getNewDescription());

        redirectAttributes.addAttribute("changeDescription", true);
        return "redirect:/image/"+image.getId();
    }

    @PostMapping("/changeTitle/{imageId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #image.user.name eq authentication.name")
    public String changeTitle(@PathVariable("imageId") Image image, UploadTitleDTO newTitle, RedirectAttributes redirectAttributes) {
        imageService.changeTitle(image.getId(), newTitle.getNewTitle());

        redirectAttributes.addAttribute("changeTitle", true);
        return "redirect:/image/"+image.getId();
    }
}
