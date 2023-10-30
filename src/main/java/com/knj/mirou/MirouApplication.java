package com.knj.mirou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MirouApplication {

	public static void main(String[] args) {
		SpringApplication.run(MirouApplication.class, args);
	}

}
