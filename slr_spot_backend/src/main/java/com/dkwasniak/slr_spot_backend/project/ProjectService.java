package com.dkwasniak.slr_spot_backend.project;

import com.dkwasniak.slr_spot_backend.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getReviews() {
        return projectRepository.findAll();
    }

    public List<Project> getReviewsByUser(String username) {
        return projectRepository.findByUser(userService.getUser(username));
    }

    public Optional<Project> getReviewByTitle(String title) {
        return projectRepository.findByTitle(title);
    }
}
