package example.audiohive.app;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan("example.audiohive.app.configuration")
public class ImaginariumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImaginariumApplication.class, args);
	}

	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

}
