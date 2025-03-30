package com.api.basicorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.api.basicorder")
public class BasicOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicOrderApplication.class, args);
	}

}
