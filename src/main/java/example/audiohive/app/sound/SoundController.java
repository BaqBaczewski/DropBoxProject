package example.audiohive.app.sound;

import example.audiohive.app.badge.Badge;
import example.audiohive.app.badge.BadgeService;
import example.audiohive.app.playback.PlaybackService;
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
        Pageable pageable = PageRequest.of(page, 2);
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

    @GetMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public String upload(Model model) {
        model.addAttribute("sound", new UploadSoundDTO("", null));
        return "upload";
    }

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public String upload(@Valid @ModelAttribute("sound") UploadSoundDTO sound, BindingResult bindingResult,
                         @ModelAttribute("sessionUser") User sessionUser, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "upload";
        }

        try {
            soundService.create(sound.getTitle(), sound.getFile().getInputStream(), sound.getFile().getSize(),
                    sessionUser, Instant.now());

            redirectAttributes.addAttribute("uploaded", true);
            return "redirect:/newSounds";
        } catch (IOException e) {
            bindingResult.addError(new ObjectError("file", "Can't read file"));
            return "upload";
        }
    }

    @GetMapping("/sound/{soundId}")
    public String soundDetails(Model model, @PathVariable("soundId") Sound sound) {
        model.addAttribute("sound", sound);

        long playbackCount = playbackService.getPlaybackCount(sound);
        model.addAttribute("playbackCount", playbackCount);

        List<Badge> badges = badgeService.getSoundBadges(sound, Instant.now());
        model.addAttribute("badges", badges);

        return "soundDetails";
    }

    @PostMapping("/deleteSound/{soundId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #sound.user.name eq authentication.name")
    public String deleteSound(@PathVariable("soundId") Sound sound, RedirectAttributes redirectAttributes) {
        soundService.delete(sound);

        redirectAttributes.addAttribute("soundDeleted", true);
        return "redirect:/";
    }
}
