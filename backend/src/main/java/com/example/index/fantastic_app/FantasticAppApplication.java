package com.example.index.fantastic_app;

import com.example.index.fantastic_app.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FantasticAppApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(FantasticAppApplication.class, args);
	}

	@Override
	public void run(String... args) {
		long count = userRepository.count();
		System.out.println("User 테이블 레코드 수: " + count);
	}
}
