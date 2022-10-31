package com.dkwasniak.slr_spot_backend;

import com.dkwasniak.slr_spot_backend.project.Review;
import com.dkwasniak.slr_spot_backend.project.ReviewService;
import com.dkwasniak.slr_spot_backend.role.Role;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserFacade;
import com.dkwasniak.slr_spot_backend.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableConfigurationProperties
public class SlrSpotBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlrSpotBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService, UserFacade userFacade, ReviewService reviewService) {
		return args -> {
			userService.saveRole(new Role("ROLE_USER"));
			userService.saveRole(new Role("ROLE_ADMIN"));

			var danny = new User("Daniel", "Danielewicz", "danny@gmail.com", "1234", new ArrayList<>());
			userFacade.createUser(danny);
			userService.activateUser("danny@gmail.com");
			var tobi = new User("Tobiasz", "Tobik", "tobi@gmail.com", "1234", new ArrayList<>());
			userFacade.createUser(tobi);
//			userService.activateUser("tobi@gmail.com");

			userFacade.addRoleToUser("danny@gmail.com", "ROLE_ADMIN");
			userFacade.addRoleToUser("tobi@gmail.com", "ROLE_USER");

			var proj1 = new Review("testowy projekt", danny);
			var proj2 = new Review("testowy projekt1", danny);
			var proj3 = new Review("testowy projekt2", danny);
			var proj4 = new Review("testowy projekt3", tobi);
			var proj5 = new Review("testowy projekt4", tobi);
			reviewService.saveProject(proj1);
			reviewService.saveProject(proj2);
			reviewService.saveProject(proj3);
			reviewService.saveProject(proj4);
			reviewService.saveProject(proj5);
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
