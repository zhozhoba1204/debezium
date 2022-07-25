package com.blinov.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka

@ComponentScan(basePackages = {
		"com.blinov.kafka"
})
@PropertySource({
		"classpath:kafka.properties"
})
public class KafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaApplication.class, args);
	}
}

