package example.audiohive.app.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "example.audiohive.default-admin")
public class DefaultAdminProperties {
    private final String name;
    private final String password;

    @ConstructorBinding
    public DefaultAdminProperties(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}