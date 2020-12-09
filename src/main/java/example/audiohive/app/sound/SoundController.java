package example.audiohive.app.sound;

import example.audiohive.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

@Controller
public class SoundController {

    private final SoundService soundService;

    @Autowired
    public SoundController(SoundService soundService) {
        this.soundService = soundService;
    }

    @GetMapping("/newSounds")
    public String newSounds(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        model.addAttribute("sounds", soundService.findNewSounds(pageable));
        return "newSounds";
    }

    @GetMapping("/audioData/{soundId}")
    public ResponseEntity<InputStreamResource> audioData(@PathVariable String soundId) throws SQLException {
        Optional<Sound> optionalSound = soundService.findById(soundId);

        if (optionalSound.isPresent()) {
            Blob audioBlob = optionalSound.get().getAudioData();

            return ResponseEntity.ok()
                    .contentLength(audioBlob.length())
                    .contentType(new MediaType("audio", "mpeg"))
                    .body(new InputStreamResource(audioBlob.getBinaryStream()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public String upload(Model model) {
        model.addAttribute("sound", new UploadSoundDTO("", null));
        return "upload";
    }

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public String upload(@Valid @ModelAttribute UploadSoundDTO sound, @ModelAttribute("sessionUser") User sessionUser,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "upload";
        }

        try {
            soundService.create(sound.getTitle(), sound.getFile().getInputStream(), sound.getFile().getSize(), sessionUser);

            redirectAttributes.addAttribute("uploaded", true);
            return "redirect:/newSounds";
        } catch (IOException e) {
            bindingResult.addError(new ObjectError("file", "Can't read file"));
            return "upload";
        }
    }

    @GetMapping("/sound/{soundId}")
    public String soundDetails(Model model, @PathVariable String soundId) {
        Sound s = soundService.findById(soundId).orElseThrow();
        model.addAttribute("sound", s);

        return "soundDetails";
    }

}
