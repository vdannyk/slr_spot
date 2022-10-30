package com.dkwasniak.slr_spot_backend.project;

import com.dkwasniak.slr_spot_backend.project.dto.ProjectDto;
import com.dkwasniak.slr_spot_backend.user.User;
import com.dkwasniak.slr_spot_backend.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ProjectFacade {

    private final ProjectService projectService;
    private final UserService userService;

    public void createProject(ProjectDto projectDto, String username) {
        User user = userService.getUser(username);
        Project project = projectService.saveProject(new Project(projectDto.getTitle(), user));
    }
}
