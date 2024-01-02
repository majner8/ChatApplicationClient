package Main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import chatAPP_CommontPart.Log4j2.Log4j2;
//import chatAPP_database.Chat.Messages.MessageEntity;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"chatAPP_database"})
@EntityScan(basePackages={"chatAPP_database"})
@EnableJpaRepositories(basePackages = {"chatAPP_database"})
@EnableAutoConfiguration
@EnableAspectJAutoProxy
public class Main {
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		Log4j2.log.info("Starting application");

		
	}


}