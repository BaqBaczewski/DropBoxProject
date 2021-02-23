package example.audiohive.app;

import example.audiohive.app.configuration.DefaultAdminProperties;
import example.audiohive.app.sound.SoundService;
import example.audiohive.app.image.ImageService;
import example.audiohive.app.user.User;
import example.audiohive.app.user.UserService;
import example.audiohive.app.videos.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

@Component
public class StartupComponent {

    private static final Logger logger = LoggerFactory.getLogger(StartupComponent.class);

    private final UserService userService;
    private final DefaultAdminProperties defaultAdminProperties;
    private final SoundService soundService;
    private final VideoService videoService;
    private final ImageService imageService;



    @Autowired
    public StartupComponent(
            UserService userService,
            DefaultAdminProperties defaultAdminProperties,    
            SoundService soundService, ImageService imageService,VideoService videoService) {
        this.userService = userService;
        this.defaultAdminProperties = defaultAdminProperties;
        this.soundService = soundService;
        this.imageService = imageService;
              this.videoService = videoService;

    }

    private void createExampleSound(String title, String resourceName, User user, String description) {
        try {
            String resourcePath = "/example_sounds/" + resourceName;
            URI uri = getClass().getResource(resourcePath).toURI();
            InputStream inputStream;
            long streamLength;
            if (uri.isOpaque()) {
                // running in jar
                streamLength = 0;

                // read once to determine size
                InputStream skipStream = getClass().getResourceAsStream(resourcePath);
                while (skipStream.read() != -1) {
                    streamLength++;
                }

                inputStream = getClass().getResourceAsStream(resourcePath);
            } else {
                // running from file system
                File resourceFile = new File(uri);
                inputStream = new FileInputStream(resourceFile);
                streamLength = resourceFile.length();
            }

            soundService.create(title, inputStream, streamLength, user, Instant.now(), description);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Could not create example sound " + title, e);
        }

    }

    private void createExampleImages(String title, String resourceName, User user, String description) {
        try {
            String resourcePath = "/example_images/" + resourceName;
            URI uri = getClass().getResource(resourcePath).toURI();
            InputStream inputStream;
            long streamLength;
            if (uri.isOpaque()) {
                streamLength = 0;

                InputStream skipStream = getClass().getResourceAsStream(resourcePath);
                while (skipStream.read() != -1) {
                    streamLength++;
                }

                inputStream = getClass().getResourceAsStream(resourcePath);
            } else {
                File resourceFile = new File(uri);
                inputStream = new FileInputStream(resourceFile);
                streamLength = resourceFile.length();
            }

            imageService.create(title, inputStream, streamLength, user, Instant.now(), description);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Could not create example image " + title, e);
        }
    }

    private void createExampleVideo(String title, String resourceName, User user, String description) {
        try {
            String resourcePath = "/example_videos/" + resourceName;
            URI uri = getClass().getResource(resourcePath).toURI();
            InputStream inputStream;
            long streamLength;
            if (uri.isOpaque()) {

                // running in jar
                streamLength = 0;

                // read once to determine size
                InputStream skipStream = getClass().getResourceAsStream(resourcePath);
                while (skipStream.read() != -1) {
                    streamLength++;
                }

                inputStream = getClass().getResourceAsStream(resourcePath);
            } else {
                // running from file system
                File resourceFile = new File(uri);
                inputStream = new FileInputStream(resourceFile);
                streamLength = resourceFile.length();
            }

            videoService.create(title, inputStream, streamLength, user, Instant.now(), description);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Could not create example video " + title, e);
        }
    }

    @EventListener
    @Transactional
    public void handleApplicationReady(ApplicationReadyEvent event) {
        if (defaultAdminProperties.getName() == null || defaultAdminProperties.getPassword() == null) {
            logger.info("No default admin account requested.");
        }
        else if (userService.userNameTaken(defaultAdminProperties.getName())) {
            logger.info("Default admin account exists already.");
        }
        else {
            userService.createUser(
                    defaultAdminProperties.getName(), defaultAdminProperties.getPassword(), User.Role.ADMIN
            );
            logger.info("Created default admin account: " + defaultAdminProperties.getName());
        }

        if (soundService.findNewSounds(PageRequest.of(0, 10)).isEmpty()) {

            User exampleUser = userService.createUser("MrP.TheMusicMaster", "example123", User.Role.USER);

            createExampleSound("Komiku - Pop City", "pop_city.mp3", exampleUser, "My favourite song");
            createExampleSound("Monplaisir - Pizza", "pizza.mp3", exampleUser, "Makes me hungry");
            createExampleSound("Tytia Mina Teremina - Rimsky Champagne Aeroplane", "aeroplane.mp3", exampleUser, "Just putting something here");
            createExampleSound("XTaKeRuX - Pursuing Darkness", "darkness.mp3", exampleUser, "when I am sad, I play this one");
            createExampleSound("Kevin Macleod - Sneaky Snitch", "SneakySnitch.mp3", exampleUser, "He's looking from shadows");

        }

        if (videoService.findVideo(PageRequest.of(0, 5)).isEmpty()) {

            User videoUser = userService.createUser("MaryMovieEnthusiast", "video123", User.Role.USER);

            createExampleVideo("Emotions", "Emotions.mp4", videoUser, "wide range of emotions, it's beautiful");
            createExampleVideo("Sing", "Ready to sing.mp4", videoUser, "had to show you this guys");
            createExampleVideo("Love", "Love.mp4", videoUser, "Fall in love, forget about whole world");
            createExampleVideo("Fire", "Fire.mp4", videoUser, "I miss this so much");
            createExampleVideo("Party Hard", "Party Hard.mp4", videoUser, "Come and party with me!");
            createExampleVideo("Baywatch", "Baywatch.mp4", videoUser, ";) just having fun");

        }

        if (imageService.findNewImages(PageRequest.of(0, 10)).isEmpty()) {

            User exampleUser = userService.createUser("BobThePhotographer", "bob123", User.Role.USER);

            createExampleImages("Break from work", "example1.jpg", exampleUser, "Lonely and tired airport worker");
            createExampleImages("Accidental art", "example2.jpg", exampleUser, "Urban art at its best");
            createExampleImages("Cloudy San Francisco", "example3.jpg", exampleUser, "Cloudy and hazy day");
            createExampleImages("Snow train", "example4.jpg", exampleUser, "A lonely locomotive on snowy tracks");
            createExampleImages("Dark tunnel", "example5.jpg", exampleUser, "Dark urban landscape");

        }
    }

}
