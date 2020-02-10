package com.ufc.tbot;

import com.ufc.tbot.controller.AppController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TbotApplication {

	public static void main(String[] args) {
		AppController appController = new AppController();
		SpringApplication.run(TbotApplication.class, args);
	}

}
