package com.dkwasniak.slr_spot_backend;

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
	CommandLineRunner run(UserService userService, UserFacade userFacade) {
		return args -> {
			userService.saveRole(new Role("ROLE_USER"));
			userService.saveRole(new Role("ROLE_ADMIN"));

			userFacade.saveUser(new User("Daniel", "Danielewicz", "danny@gmail.com", "1234", new ArrayList<>()));
			userService.activateUser("danny@gmail.com");
			userFacade.saveUser(new User("Tobiasz", "Tobik", "tobi@gmail.com", "1234", new ArrayList<>()));

			userFacade.addRoleToUser("danny@gmail.com", "ROLE_ADMIN");
			userFacade.addRoleToUser("tobi@gmail.com", "ROLE_USER");
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
