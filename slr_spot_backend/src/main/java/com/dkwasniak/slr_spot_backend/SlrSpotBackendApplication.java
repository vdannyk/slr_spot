package com.dkwasniak.slr_spot_backend;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.review.ReviewRepository;
import com.dkwasniak.slr_spot_backend.role.ReviewRole;
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
import org.springframework.data.domain.PageRequest;
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
			var u1 = new User("Krzysiek", "Krzys", "krzys@gmail.com", "1234");
			var u2 = new User("Maciej", "Maciek", "macias@gmail.com", "1234");
			var u3 = new User("Waldemar", "Wad", "waldus@gmail.com", "1234");
			var u4 = new User("Milosz", "Milo", "milo@gmail.com", "1234");
			var u5 = new User("Krzysztof", "Krzysz", "krzysz@gmail.com", "1234");
			var u6 = new User("Maja", "Majec", "majec@gmail.com", "1234");
			userService.saveUser(u1); userService.saveUser(u2); userService.saveUser(u3); userService.saveUser(u4);
			userService.saveUser(u5); userService.saveUser(u6);
			var userRole = new Role("ROLE_USER");
			var adminRole = new Role("ROLE_ADMIN");
			var proj1 = new Review("testowy projekt", "testowy projekt", "testowy projekt", false, 2, danny.getEmail());
			proj1.setIsPublic(true);
			var proj2 = new Review("testowy projekt2", "testowy projekt2", "testowy projekt2", false, 2, danny.getEmail());
			proj2.setIsPublic(true);
			var proj3 = new Review("testowy projekt3", "testowy projekt3", "testowy projekt3", true, 2, danny.getEmail());
			var proj4 = new Review("testowy projekt3", "testowy projekt3", "testowy projekt3", false, 1, tobi.getEmail());
			var proj5 = new Review("testowy projekt4", "testowy projekt4", "testowy projekt4", true, 1, tobi.getEmail());
			danny.addRole(adminRole);
			tobi.addRole(userRole);
			danny.addReview(proj1, ReviewRole.OWNER);
			danny.addReview(proj2, ReviewRole.OWNER);
			danny.addReview(proj3, ReviewRole.OWNER);
			tobi.addReview(proj4, ReviewRole.OWNER);
			tobi.addReview(proj5, ReviewRole.OWNER);
			userService.saveUser(danny);
			var proj6 = new Review("addReviewToUser");
			userService.addReviewToUser(danny, proj6);
			userService.addReviewToUser(danny, new Review("a"));
			userService.addReviewToUser(danny, new Review("bc"));
			userService.addReviewToUser(danny, new Review("da"));
			userService.addReviewToUser(danny, new Review("ef"));
			userService.addReviewToUser(danny, new Review("gh"));
			userService.addReviewToUser(danny, new Review("sialal"));
			userService.addReviewToUser(danny, new Review("wuwuniu"));
			userService.addReviewToUser(danny, new Review("bajo"));
			userService.activateUser("danny@gmail.com");
			userService.saveUser(tobi);
//			reviewRepository.save(proj6);
			// PAGINATION TEST
//			var test = reviewRepository.findByUsers_User_Id(danny.getId(), PageRequest.of(1, 5));


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
