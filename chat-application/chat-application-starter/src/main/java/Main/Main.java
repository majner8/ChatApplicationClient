package Main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import Logger.Log4j2;




@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan(basePackages = {"Main","Bean","Controler"})
@EntityScan(basePackages={"AuthorizationEntity"})
public class Main {
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		Log4j2.log.info("Starting application");

		
	}


}
