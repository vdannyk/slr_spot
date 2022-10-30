package com.dkwasniak.slr_spot_backend.project;

import com.dkwasniak.slr_spot_backend.project.dto.ProjectDto;
import com.dkwasniak.slr_spot_backend.role.RoleToUserRequest;
import com.dkwasniak.slr_spot_backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "api/reviews")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectFacade projectFacade;
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok().body(projectService.getReviews());
    }

    @GetMapping("/yours")
    public ResponseEntity<List<Project>> getUserProjects() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok().body(projectService.getReviewsByUser(username));
    }

    @PostMapping("/save")
    public ResponseEntity<List<Project>> saveProject(@RequestBody ProjectDto projectDto) {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        projectFacade.createProject(projectDto, user);
        return ResponseEntity.ok().build();
    }
}
