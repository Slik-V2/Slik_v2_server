package com.spring.slik_v2_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SlikV2ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlikV2ServerApplication.class, args);
	}

}
