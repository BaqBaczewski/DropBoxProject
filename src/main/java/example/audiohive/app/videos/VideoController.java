package example.audiohive.app.videos;

import example.audiohive.app.badge.Badge;
import example.audiohive.app.badge.BadgeService;
import example.audiohive.app.playback.PlaybackService;
import example.audiohive.app.videos.VideoService;
import example.audiohive.app.videos.UploadVideoDTO;
import example.audiohive.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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


public class VideoController {
    private final VideoService videoService;

    private final PlaybackService playbackService;
    private final BadgeService badgeService;

    @Autowired
    public VideoController(VideoService videoService, PlaybackService playbackService, BadgeService badgeService) {
        this.videoService = videoService;
        this.playbackService = playbackService;
        this.badgeService = badgeService;
    }

    @GetMapping("/videos")
    public String video(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Video> videoPage = videoService.findVideo(pageable);
        model.addAttribute("video", videoPage);

        List<Integer> pagination = VideoService.createPagination(videoPage.getNumber(), videoPage.getTotalPages());
        model.addAttribute("pagination", pagination);
        return "videos";
    }


    @GetMapping("/videoData/{videoId}")
    public ResponseEntity<InputStreamResource> videoData(@PathVariable String videoId) throws SQLException {
        Optional<Video> optionalVideo = videoService.findById(videoId);

        if (optionalVideo.isPresent()) {
            Blob videoBlob = optionalVideo.get().getVideoData();

            return ResponseEntity.ok()
                    .contentLength(videoBlob.length())
                    .contentType(new MediaType("video", "mp4"))
                    .body(new InputStreamResource(videoBlob.getBinaryStream()));
        }
        return ResponseEntity.notFound().build();
    }

//    @GetMapping("/upload")
//    @PreAuthorize("isAuthenticated()")
//    public String upload(Model model) {
//        model.addAttribute("video", new UploadVideoDTO("", null, ""));
//        return "upload";
//    }

//    @PostMapping("/upload")
//    @PreAuthorize("isAuthenticated()")
//    public String upload(@Valid @ModelAttribute ("sound") UploadVideoDTO video, BindingResult bindingResult, @ModelAttribute("sessionUser") User sessionUser,
//                         RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()){
//            return "upload";
//        }
//        if (!video.getFile().getContentType().equals("video/mp4")){
//            bindingResult.addError(new FieldError("video", "file","Error!!! Incorrect file type."));
//        }
//        if (bindingResult.hasErrors()){
//            return "upload";
//        }
//
//        try {
//            videoService.create(video.getTitle(), video.getFile().getInputStream(), video.getFile().getSize(),
//                    sessionUser, Instant.now(), video.getDescription());
//
//            redirectAttributes.addAttribute("uploaded", true);
//            return "redirect:/Video";
//        } catch (IOException e) {
//            bindingResult.addError(new ObjectError("file", "Can't read file"));
//            return "upload";
//        }
//    }

    @GetMapping("/video/{videoId}")
    public String videoDetails(Model model, @PathVariable("videoId") Video video) {
        model.addAttribute("video", video);

        return "videoDetails";
    }

    @PostMapping("/deleteVideo/{videoId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #video.user.name eq authentication.name")
    public String deleteVideo(@PathVariable("videoId") Video video, RedirectAttributes redirectAttributes) {
        videoService.delete(video);

        redirectAttributes.addAttribute("videoDeleted", true);
        return "redirect:/";
    }

}