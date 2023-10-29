package Main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import chat_application_commonPart.Logger.Log4j2;






@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"Main","Bean","chat_application_authorization","chat_application_commonPart"
		,"chat_application_database"})
@EntityScan(basePackages={"chat_application_database"})
@EnableJpaRepositories(basePackages = {"chat_application_database"})
@EnableAutoConfiguration
@EnableAspectJAutoProxy
public class Main {
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		Log4j2.log.info("Starting application");

		
	}


}
