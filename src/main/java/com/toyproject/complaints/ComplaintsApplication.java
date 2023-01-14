package com.toyproject.complaints;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ComplaintsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComplaintsApplication.class, args);
	}

}
