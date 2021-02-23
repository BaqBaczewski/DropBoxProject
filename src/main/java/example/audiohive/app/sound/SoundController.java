package example.audiohive.app.sound;

import example.audiohive.app.badge.Badge;
import example.audiohive.app.badge.BadgeService;
import example.audiohive.app.image.Image;
import example.audiohive.app.playback.PlaybackService;
import example.audiohive.app.upload.UploadDescriptionDTO;
import example.audiohive.app.upload.UploadTitleDTO;
import example.audiohive.app.user.User;
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
public class SoundController {

    private final SoundService soundService;
    private final PlaybackService playbackService;
    private final BadgeService badgeService;

    @Autowired
    public SoundController(SoundService soundService, PlaybackService playbackService, BadgeService badgeService) {
        this.soundService = soundService;
        this.playbackService = playbackService;
        this.badgeService = badgeService;
    }

    @GetMapping("/newSounds")
    public String newSounds(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Sound> soundPage = soundService.findNewSounds(pageable);
        model.addAttribute("sounds", soundPage);

        List<Integer> pagination = SoundService.createPagination(soundPage.getNumber(), soundPage.getTotalPages());
        model.addAttribute("pagination", pagination);
        return "newSounds";
    }


    @GetMapping("/audioData/{soundId}")
    public ResponseEntity<InputStreamResource> audioData(@PathVariable String soundId) throws SQLException {
        Optional<Sound> optionalSound = soundService.findById(soundId);

        if (optionalSound.isPresent()) {
            Blob audioBlob = optionalSound.get().getAudioData();

            return ResponseEntity.ok()
                    .contentLength(audioBlob.length())
                    .contentType(new MediaType("audio", "mp3"))
                    .body(new InputStreamResource(audioBlob.getBinaryStream()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/sound/{soundId}")
    public String soundDetails(Model model, @PathVariable("soundId") Sound sound) {
        model.addAttribute("sound", sound);

        long playbackCount = playbackService.getPlaybackCount(sound);
        model.addAttribute("playbackCount", playbackCount);

        List<Badge> badges = badgeService.getSoundBadges(sound, Instant.now());
        model.addAttribute("badges", badges);

        UploadDescriptionDTO uploadDescriptionDTO = new UploadDescriptionDTO();
        uploadDescriptionDTO.setNewDescription(sound.getDescription());
        model.addAttribute("newDescription", uploadDescriptionDTO);

        UploadTitleDTO uploadTitleDTO = new UploadTitleDTO();
        uploadTitleDTO.setNewTitle(sound.getTitle());
        model.addAttribute("newTitle", uploadTitleDTO);

        return "soundDetails";
    }

    @PostMapping("/deleteSound/{soundId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #sound.user.name eq authentication.name")
    public String deleteSound(@PathVariable("soundId") Sound sound, RedirectAttributes redirectAttributes) {
        soundService.delete(sound);

        redirectAttributes.addAttribute("soundDeleted", true);
        return "redirect:/";
    }

    @PostMapping("/changeDescriptionSound/{soundId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #sound.user.name eq authentication.name")
    public String changeDescriptionSound(@PathVariable("soundId") Sound sound, UploadDescriptionDTO newDescription, RedirectAttributes redirectAttributes) {
        soundService.changeDescriptionSound(sound.getId(), newDescription.getNewDescription());

        redirectAttributes.addAttribute("changeDescriptionSound", true);
        return "redirect:/sound/"+sound.getId();
    }

    @PostMapping("/changeTitleSound/{soundId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #sound.user.name eq authentication.name")
    public String changeTitleSound(@PathVariable("soundId") Sound sound, UploadTitleDTO newTitle, RedirectAttributes redirectAttributes) {
        soundService.changeTitleSound(sound.getId(), newTitle.getNewTitle());

        redirectAttributes.addAttribute("changeTitleSound", true);
        return "redirect:/sound/"+sound.getId();
    }
}
