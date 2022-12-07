package com.dkwasniak.slr_spot_backend.reviewRole;

import com.dkwasniak.slr_spot_backend.reviewRole.exception.ReviewRoleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewRoleService {

    private final ReviewRoleRepository roleRepository;

    public ReviewRole getRoleByName(String name) {
        return roleRepository.findByName(name).orElseThrow(
                () -> new ReviewRoleNotFoundException(name)
        );
    }
}
