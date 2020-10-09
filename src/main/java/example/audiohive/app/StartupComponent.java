package example.audiohive.app;

import example.audiohive.app.user.User;
import example.audiohive.app.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Random;

@Component
public class StartupComponent {

    private static final Logger logger = LoggerFactory.getLogger(StartupComponent.class);

    private final UserRepository userRepository;
    private final String defaultAdminName;
    private final String defaultAdminPassword;

    @Autowired
    public StartupComponent(
            UserRepository userRepository,
            @Value("${example.audiohive.default_admin.name:}") String defaultAdminName,
            @Value("${example.audiohive.default_admin.password:}") String defaultAdminPassword
    ) {
        this.userRepository = userRepository;
        this.defaultAdminName = defaultAdminName;
        this.defaultAdminPassword = defaultAdminPassword;
    }

    @EventListener
    @Transactional
    public void handleApplicationReady(ApplicationReadyEvent event) {
        if (defaultAdminName != null && !defaultAdminName.isEmpty() &&
                defaultAdminPassword != null && !defaultAdminPassword.isEmpty()) {
            if (userRepository.existsByNameIgnoreCase(defaultAdminName)) {
                logger.info("Default admin account exists already.");
            }
            else {
                userRepository.save(new User(defaultAdminName, defaultAdminPassword, User.Role.ADMIN));
                logger.info("Created default admin account: " + defaultAdminName);
            }
        }
    }
}
