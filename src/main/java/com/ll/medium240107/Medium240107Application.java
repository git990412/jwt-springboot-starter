package com.ll.medium240107;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Medium240107Application {

	public static void main(String[] args) {
		SpringApplication.run(Medium240107Application.class, args);
	}

}
