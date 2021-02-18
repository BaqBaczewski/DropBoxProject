package example.audiohive.app.upload;


import example.audiohive.app.image.ImageService;
import example.audiohive.app.image.UploadImageDTO;
import example.audiohive.app.sound.SoundService;
import example.audiohive.app.sound.UploadSoundDTO;
import example.audiohive.app.user.User;
import example.audiohive.app.videos.UploadVideoDTO;
import example.audiohive.app.upload.UploadFileDTO;
import example.audiohive.app.videos.VideoService;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.Instant;

@Controller
public class UploadController {

    private final SoundService soundService;
    private final ImageService imageService;
    private final VideoService videoService;

    @Autowired
    public UploadController(SoundService soundService, ImageService imageService, VideoService videoService) {
        this.soundService = soundService;
        this.imageService = imageService;
        this.videoService = videoService;
    }

    @GetMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public String upload(Model model) {
        model.addAttribute("file", new UploadFileDTO("", null, ""));
        return "upload";
    }

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public String upload(@Valid @ModelAttribute ("file") UploadFileDTO file, BindingResult bindingResult, @ModelAttribute("sessionUser") User sessionUser,
                               RedirectAttributes redirectAttributes) {

        if (file.getFile().getContentType().equals("audio/mpeg")) {
            try {
                soundService.create(file.getTitle(), file.getFile().getInputStream(), file.getFile().getSize(),
                        sessionUser, Instant.now(), file.getDescription());

                redirectAttributes.addAttribute("uploaded", true);
                return "redirect:/newSounds";
            } catch (IOException e) {
                bindingResult.addError(new ObjectError("file", "Can't read file"));
                return "upload";
            }
        }
        if (file.getFile().getContentType().equals("image/jpeg") ) {
            try {
                imageService.create(file.getTitle(), file.getFile().getInputStream(), file.getFile().getSize(),
                        sessionUser, Instant.now(), file.getDescription());

                redirectAttributes.addAttribute("uploaded", true);
                return "redirect:/newImages";
            } catch (IOException e) {
                bindingResult.addError(new ObjectError("file", "Can't read file"));
                return "upload";
            }
        }
        if (file.getFile().getContentType().equals("video/mp4")) {
            try {
                videoService.create(file.getTitle(), file.getFile().getInputStream(), file.getFile().getSize(),
                        sessionUser, Instant.now(), file.getDescription());

                redirectAttributes.addAttribute("uploaded", true);
                return "redirect:/videos";
            } catch (IOException e) {
                bindingResult.addError(new ObjectError("file", "Can't read file"));
                return "upload";
            }
        }
        bindingResult.hasErrors();
        return "upload";
    }

}
