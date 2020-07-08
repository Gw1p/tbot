package com.gw1p.tbot;

import com.gw1p.tbot.controller.AppController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class TbotApplication {

	public static void main(String[] args) {
        SpringApplication.run(TbotApplication.class, args);
        AppController appController = new AppController();
        System.exit(0);
	}

}
