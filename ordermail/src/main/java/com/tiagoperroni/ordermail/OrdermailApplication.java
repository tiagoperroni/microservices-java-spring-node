package com.tiagoperroni.ordermail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class OrdermailApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdermailApplication.class, args);
	}

}
