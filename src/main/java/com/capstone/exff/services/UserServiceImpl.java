package com.capstone.exff.services;

import com.capstone.exff.entities.RoleEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.repositories.RoleRepository;
import com.capstone.exff.repositories.UserRepository;
import com.capstone.exff.utilities.ExffError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserServices{

    private final RoleEntity ROLE_USER;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private List<RoleEntity> roleEntities;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleEntities = roleRepository.findAll();
        Optional<RoleEntity> role = roleEntities.stream()
                .filter(roleEntity -> roleEntity.getName().equals("user"))
                .findFirst();
        ROLE_USER = role.orElse(null);
    }

    @Override
    public ResponseEntity login(String phoneNumber, String password) {
        UserEntity userEntity = userRepository.findFirstByPhoneNumberAndPassword(phoneNumber, password);

        if (userEntity != null) {
            Map<String,String> token = new HashMap<>();
            token.put("token",TokenAuthenticationService.createToken(userEntity.getPhoneNumber()));

            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ExffError("Cannot login"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity register(String phoneNumber, String password, String fullname, String status) {
        return register(phoneNumber,password,fullname, status, this.ROLE_USER);
    }

    @Override
    public ResponseEntity register(String phoneNumber, String password, String fullname, String status, RoleEntity roleEntity) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPhoneNumber(phoneNumber);
        userEntity.setPassword(password);
        userEntity.setFullName(fullname);
        userEntity.setStatus(status);
        userEntity.setRoleByRoleId(roleEntity);
        try {
            userEntity = userRepository.save(userEntity);
        } catch (Exception e) {
            return new ResponseEntity<>(new ExffError(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }


}
