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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

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
        File resourceFile;
        try {
            resourceFile = new File(getClass().getResource("/example_sounds/" + resourceName).toURI());
            soundService.create(title, new FileInputStream(resourceFile), resourceFile.length(), user);
        } catch (URISyntaxException | FileNotFoundException e) {
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
