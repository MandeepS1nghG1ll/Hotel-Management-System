package com.mandeep.reservation_service;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.mandeep.reservation_service.repo")
@EntityScan //(basePackages = "com.mandeep.reservation_service.entity")
@EnableDiscoveryClient
@EnableFeignClients
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}

}


