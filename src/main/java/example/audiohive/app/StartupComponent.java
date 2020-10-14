package example.audiohive.app;

import example.audiohive.app.configuration.DefaultAdminProperties;
import example.audiohive.app.user.User;
import example.audiohive.app.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class StartupComponent {

    private static final Logger logger = LoggerFactory.getLogger(StartupComponent.class);

    private final UserRepository userRepository;
    private final DefaultAdminProperties defaultAdminProperties;


    @Autowired
    public StartupComponent(
            UserRepository userRepository,
            DefaultAdminProperties defaultAdminProperties
    ) {
        this.userRepository = userRepository;
        this.defaultAdminProperties = defaultAdminProperties;
    }

    @EventListener
    @Transactional
    public void handleApplicationReady(ApplicationReadyEvent event) {
        if (userRepository.existsByNameIgnoreCase(defaultAdminProperties.getName())) {
            logger.info("Default admin account exists already.");
        }
        else {
            userRepository.save(
                    new User(defaultAdminProperties.getName(), defaultAdminProperties.getPassword(), User.Role.ADMIN)
            );
            logger.info("Created default admin account: " + defaultAdminProperties.getName());
        }
    }
}
