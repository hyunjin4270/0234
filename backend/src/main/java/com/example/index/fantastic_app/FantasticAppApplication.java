package com.example.index.fantastic_app;

import com.example.index.fantastic_app.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FantasticAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(FantasticAppApplication.class, args);
	}
}
