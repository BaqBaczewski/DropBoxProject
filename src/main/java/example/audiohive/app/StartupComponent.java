package example.audiohive.app;

import example.audiohive.app.configuration.DefaultAdminProperties;
import example.audiohive.app.sound.SoundService;
import example.audiohive.app.user.User;
import example.audiohive.app.user.UserService;
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


    @Autowired
    public StartupComponent(
            UserService userService,
            DefaultAdminProperties defaultAdminProperties,
            SoundService soundService) {
        this.userService = userService;
        this.defaultAdminProperties = defaultAdminProperties;
        this.soundService = soundService;
    }

    private void createExampleSound(String title, String resourceName, User user) {
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

            soundService.create(title, inputStream, streamLength, user, Instant.now());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Could not create example sound " + title, e);
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

            User exampleUser = userService.createUser("example_sounds", "example123", User.Role.USER);

            createExampleSound("Komiku - Pop City", "pop_city.mp3", exampleUser);
            createExampleSound("Monplaisir - Pizza", "pizza.mp3", exampleUser);
            createExampleSound("Tytia Mina Teremina - Rimsky Champagne Aeroplane", "aeroplane.mp3", exampleUser);
            createExampleSound("XTaKeRuX - Pursuing Darkness", "darkness.mp3", exampleUser);

        }
    }
}
