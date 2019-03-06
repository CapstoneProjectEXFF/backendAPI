package com.capstone.exff.services;

import com.capstone.exff.constants.ExffRole;
import com.capstone.exff.constants.ExffStatus;
import com.capstone.exff.entities.RoleEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.repositories.RoleRepository;
import com.capstone.exff.repositories.UserRepository;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserServices {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private List<RoleEntity> roleEntities;
    private final RoleEntity userRole;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleEntities = roleRepository.findAll();
//        Optional<RoleEntity> role = roleEntities.stream()
//                .filter(roleEntity -> roleEntity.getName().equals(ExffRole.ROLE_USER))
//                .findFirst();
//        this.userRole = role.orElse(null);
        this.userRole = roleRepository.findTop1ByName(ExffRole.ROLE_USER);
    }

    @Override
    public ResponseEntity login(String phoneNumber, String password) {
        UserEntity userEntity = userRepository.findFirstByPhoneNumber(phoneNumber);
        if (userEntity != null && passwordEncoder.matches(password, userEntity.getPassword())) {
            Map<String, Object> data = new HashMap<>();
            data.put(
                    TokenAuthenticationService.HEADER_STRING,
                    TokenAuthenticationService.createToken(userEntity));
            data.put("User", userEntity);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ExffMessage("Cannot login"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity register(String phoneNumber, String password, String fullname) {
        return register(phoneNumber, password, fullname, this.userRole);
    }

    @Override
    public ResponseEntity register(String phoneNumber, String password, String fullname, RoleEntity roleEntity) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPhoneNumber(phoneNumber);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setFullName(fullname);
        userEntity.setStatus(ExffStatus.USER_ENABLE);
        userEntity.setRoleByRoleId(roleEntity);
        try {
            userEntity = userRepository.save(userEntity);
        } catch (Exception e) {
            return new ResponseEntity<>(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity updateUserInfo(int id, String phoneNumber, String fullName, String avatar, String status) {
        UserEntity userEntity = new UserEntity();
        userEntity = userRepository.findFirstByPhoneNumber(phoneNumber);

        if (userEntity != null) {
            userEntity.setAvatar(avatar);
            userEntity.setFullName(fullName);
            userEntity.setStatus(status);
            userRepository.save(userEntity);
            Map<String, Object> data = new HashMap<>();
            data.put("User", userEntity);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ExffMessage("Cannot Update"), HttpStatus.BAD_REQUEST);

        }

    }

    @Override
    public ResponseEntity getAllUser() {
        List users;
        try {
            users = userRepository.findAll();
        } catch (Exception e) {
            return new ResponseEntity<>(new ExffMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    public List<UserEntity> findUsersByName(String name) {
        return userRepository.findUserEntitiesByFullName(name);
    }

    @Override
    public UserEntity findUserByPhone(String phone) {
        return userRepository.findUserEntitiesByPhoneNumber(phone);
    }


}
