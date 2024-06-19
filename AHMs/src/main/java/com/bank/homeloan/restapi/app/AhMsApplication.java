package com.bank.homeloan.restapi.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AhMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AhMsApplication.class, args);
	}

}
