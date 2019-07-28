package com.learningvoyage.hasherservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class HasherApplication {

	public static void main(String[] args) {
		SpringApplication.run(HasherApplication.class, args);
	}
	
	

}
