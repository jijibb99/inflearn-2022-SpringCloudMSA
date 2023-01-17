package com.example.ecoomerc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EcoomercApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcoomercApplication.class, args);
	}

}
