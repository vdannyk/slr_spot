package com.dkwasniak.slr_spot_backend.role;

import com.dkwasniak.slr_spot_backend.role.exception.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
    }

    public void saveRole(Role role) {
        log.info("Saving new role: {}", role.getName());
        roleRepository.save(role);
    }
}
