package com.capstone.exff.seeders;

import com.capstone.exff.constants.ExffRole;
import com.capstone.exff.constants.ExffStatus;
import com.capstone.exff.entities.RoleEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.repositories.RoleRepository;
import com.capstone.exff.repositories.UserRepository;
import com.capstone.exff.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DatabaseSeeder {
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UserServices userServices;

    @Autowired
    public DatabaseSeeder(RoleRepository roleRepository, UserRepository userRepository, UserServices userServices) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userServices = userServices;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        try {
            seedRole();
            seedUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seedRole() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        if (roleEntities == null || roleEntities.isEmpty()) {
            RoleEntity role = new RoleEntity();
            role.setName(ExffRole.ROLE_USER);
            roleRepository.save(role);
            role.setName(ExffRole.ROLE_ADMIN);
            roleRepository.save(role);
        }
    }

    private void seedUser() {
        String adminAccount = "admin00100";
        UserEntity userEntity = userRepository.findTop1ByPhoneNumber("admin00100");
        if (userEntity == null) {
            RoleEntity roleEntity = roleRepository.findTop1ByName(ExffRole.ROLE_ADMIN);
            String adminPassword = "password00100";
            userServices.register(adminAccount,adminPassword,"admin", ExffStatus.USER_ENABLE, roleEntity);
        }
    }
}
