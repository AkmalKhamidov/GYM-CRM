package com.epamlearning.services.impl;

import com.epamlearning.entities.Role;
import com.epamlearning.entities.enums.RoleName;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.repositories.RoleRepository;
import com.epamlearning.services.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByRoleName(RoleName name) {
        if(name == null) {
            log.warn("RoleName is null.");
            throw new NotFoundException("RoleName is null.");
        }
        Optional<Role> optionalRole = roleRepository.findByName(name);
        if (optionalRole.isEmpty()) {
            log.warn("Role with roleName: {} not found.", name);
            throw new NotFoundException("Role with roleName " + name + " not found.");
        }
        return optionalRole.get();
    }
}
