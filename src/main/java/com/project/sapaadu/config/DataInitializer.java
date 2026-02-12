package com.project.sapaadu.config;

import com.project.sapaadu.entity.Role;
import com.project.sapaadu.repository.RoleRepository;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        createRoleIfNotExists("USER");
        createRoleIfNotExists("OWNER");
        createRoleIfNotExists("ADMIN");
    }

    public void createRoleIfNotExists(String roleName) {
        roleRepository.findByName(roleName).orElseGet(() ->roleRepository.save(
                Role.builder().name(roleName).build()
        ));
    }
}
