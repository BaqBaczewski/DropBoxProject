package example.audiohive.app.playback;

import example.audiohive.app.sound.Sound;
import example.audiohive.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;

@Controller
public class PlaybackController {

    private final PlaybackService playbackService;

    @Autowired
    public PlaybackController(PlaybackService playbackService) {
        this.playbackService = playbackService;
    }

    @PostMapping("/trackPlayback/{soundId}")
    public ResponseEntity<String> trackPlay(@PathVariable("soundId") Sound sound, @ModelAttribute("sessionUser") User sessionUser) {
        Playback p = playbackService.savePlayback(sound, sessionUser, Instant.now());
        return ResponseEntity.ok(p.getId());
    }
}
