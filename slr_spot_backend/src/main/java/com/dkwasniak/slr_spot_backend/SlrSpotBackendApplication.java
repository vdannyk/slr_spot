package com.dkwasniak.slr_spot_backend;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewRepository;
import com.dkwasniak.slr_spot_backend.role.Role;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties
public class SlrSpotBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlrSpotBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService, ReviewRepository reviewRepository) {
		return args -> {
			var danny = new User("Daniel", "Danielewicz", "danny@gmail.com", "1234");
			var tobi = new User("Tobiasz", "Tobik", "tobi@gmail.com", "1234");
			var userRole = new Role("ROLE_USER");
			var adminRole = new Role("ROLE_ADMIN");
			var proj1 = new Review("testowy projekt");
			var proj2 = new Review("testowy projekt1");
			var proj3 = new Review("testowy projekt2");
			var proj4 = new Review("testowy projekt3");
			var proj5 = new Review("testowy projekt4");
			danny.addRole(adminRole);
			tobi.addRole(userRole);
			danny.addReview(proj1);
			danny.addReview(proj2);
			danny.addReview(proj3);
			tobi.addReview(proj4);
			tobi.addReview(proj5);
			userService.saveUser(danny);
			var proj6 = new Review("addReviewToUser");
			userService.addReviewToUser(danny, proj6);
			userService.activateUser("danny@gmail.com");
			userService.saveUser(tobi);
//			reviewRepository.save(proj6);


//			userFacade.createUser(danny);
//			userService.addRoleToUser("danny@gmail.com", "ROLE_ADMIN");

//			userFacade.createUser(tobi);
//			userService.activateUser("tobi@gmail.com");

//			userService.addRoleToUser("tobi@gmail.com", "ROLE_USER");

//			userService.addReviewToUser(danny.getEmail(), 1);
//			userService.addReviewToUser(danny.getEmail(), 2);
//			userService.addReviewToUser(danny.getEmail(), 3);
//			userService.addReviewToUser(tobi.getEmail(), 4);
//			userService.addReviewToUser(tobi.getEmail(), 5);
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ObjectMapper defaultMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}
}
