package gov.naco.soch.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.twilio.Twilio;

@SpringBootApplication
@EnableJpaRepositories("gov.naco.soch.repository")
@EntityScan("gov.naco.soch.entity")
@ComponentScan(basePackages = { "gov.naco.soch" })
@PropertySource("classpath:application-${spring.profiles.active}.yml")
public class NotificationServiceApplication extends SpringBootServletInitializer {

	@Value("${twilio.sid}")
	private String sid;
	@Value("${twilio.token}")
	private String token;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NotificationServiceApplication.class);
	}

	/**
	 * Main method, used to run the application.
	 *
	 * @param args
	 *            the command line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);

	}

	@Bean
	public void twilioInit() {
		Twilio.init(sid, token);

	}

}
