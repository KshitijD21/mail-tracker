package com.example.mail_tracker;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MailTrackerApplication {

	public static void main(String[] args) {

		if (System.getenv("RENDER") == null) {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
			System.setProperty("MONGODB_URI", dotenv.get("MONGODB_URI"));
		}

		SpringApplication.run(MailTrackerApplication.class, args);
	}
}
