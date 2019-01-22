package com.capstone.exff.services;

import com.capstone.exff.entities.RoleEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.repositories.RoleRepository;
import com.capstone.exff.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserServices{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private List<RoleEntity> roleEntities;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleEntities = roleRepository.findAll();
    }

    @Override
    public UserEntity login(String phoneNumber, String password) {
        return userRepository.findFirstByPhoneNumberAndPassword(phoneNumber, password);
    }

    @Override
    public UserEntity save(String phoneNumber, String password, String fullname, String status) {
        Optional<RoleEntity> role = roleEntities.stream()
                .filter(roleEntity -> roleEntity.getName().equals("user"))
                .findFirst();
        return save(phoneNumber,password,fullname, status, role.get());
    }

    @Override
    public UserEntity save(String phoneNumber, String password, String fullname, String status, RoleEntity roleEntity) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPhoneNumber(phoneNumber);
        userEntity.setPassword(password);
        userEntity.setFullName(fullname);
        userEntity.setStatus(status);
        userEntity.setRoleByRoleId(roleEntity);
        return userRepository.save(userEntity);
    }


}
