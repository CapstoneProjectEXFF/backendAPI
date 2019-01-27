package com.capstone.exff.seeders;

import com.capstone.exff.entities.RoleEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.repositories.RoleRepository;
import com.capstone.exff.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class DatabaseSeeder {
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private RoleEntity roleAdmin;

    @Autowired
    public DatabaseSeeder(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event){
//        try {
//        seedRole();
//        seedUser();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
    }

    private void seedRole() {
        RoleEntity role = new RoleEntity();
        role.setName("USER");
        roleRepository.save(role);
        role.setName("CHARITY");
        roleRepository.save(role);
        role.setName("ADMIN");
        roleRepository.save(role);
        roleAdmin = role;
    }

    private void seedUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setPhoneNumber("admin");
        userEntity.setPassword("password");
        userEntity.setRoleByRoleId(roleAdmin);
        userRepository.save(userEntity);
    }
}
