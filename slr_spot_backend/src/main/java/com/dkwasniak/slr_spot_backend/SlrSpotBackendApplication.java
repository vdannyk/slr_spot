package com.dkwasniak.slr_spot_backend;

import com.dkwasniak.slr_spot_backend.project.Project;
import com.dkwasniak.slr_spot_backend.project.ProjectService;
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
	CommandLineRunner run(UserService userService, UserFacade userFacade, ProjectService projectService) {
		return args -> {
			userService.saveRole(new Role("ROLE_USER"));
			userService.saveRole(new Role("ROLE_ADMIN"));

			var danny = new User("Daniel", "Danielewicz", "danny@gmail.com", "1234", new ArrayList<>());
			userFacade.saveUser(danny);
			userService.activateUser("danny@gmail.com");
			var tobi = new User("Tobiasz", "Tobik", "tobi@gmail.com", "1234", new ArrayList<>());
			userFacade.saveUser(tobi);
			userService.activateUser("tobi@gmail.com");

			userFacade.addRoleToUser("danny@gmail.com", "ROLE_ADMIN");
			userFacade.addRoleToUser("tobi@gmail.com", "ROLE_USER");

			var proj1 = new Project("testowy projekt", danny);
			var proj2 = new Project("testowy projekt1", danny);
			var proj3 = new Project("testowy projekt2", danny);
			var proj4 = new Project("testowy projekt3", tobi);
			var proj5 = new Project("testowy projekt4", tobi);
			projectService.saveProject(proj1);
			projectService.saveProject(proj2);
			projectService.saveProject(proj3);
			projectService.saveProject(proj4);
			projectService.saveProject(proj5);
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
