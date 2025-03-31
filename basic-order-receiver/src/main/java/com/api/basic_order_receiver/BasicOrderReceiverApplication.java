package com.api.basic_order_receiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BasicOrderReceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicOrderReceiverApplication.class, args);
	}

}
