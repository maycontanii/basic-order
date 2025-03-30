package com.api.basic_order_producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BasicOrderProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicOrderProducerApplication.class, args);
	}

}
