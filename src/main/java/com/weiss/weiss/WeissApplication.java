package com.weiss.weiss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@EnableEurekaClient
@SpringBootApplication
public class WeissApplication {
	@Bean
	public BCryptPasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@LoadBalanced
	@Bean
	WebClient.Builder loadBalancer(){
		return WebClient.builder();
	}
	@Primary
	@Bean
	WebClient.Builder webClient(){
		return WebClient.builder();
	}
	public static void main(String[] args) {
		SpringApplication.run(WeissApplication.class, args);
	}
}
