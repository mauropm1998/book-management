package com.management.book;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import com.management.book.models.Role;
import com.management.book.repositories.RoleRepository;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class BookApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
		
	}
	
	@Bean
	public CommandLineRunner runner (RoleRepository repo) {
		return args -> {
			if(repo.findByName("USER").isEmpty()) {
				repo.save(Role.builder().name("USER").build());
			}
		};		
	}

}
