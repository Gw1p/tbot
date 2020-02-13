package com.ufc.tbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class TbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TbotApplication.class, args);
	}

}
