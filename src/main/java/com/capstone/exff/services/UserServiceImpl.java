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

import java.util.*;

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
            if (userEntity.getStatus().equals(ExffStatus.USER_DISABLE)) {
                return new ResponseEntity<>(new ExffMessage("Cannot login"), HttpStatus.BAD_REQUEST);
            }
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
    public ResponseEntity loginAdmin(String phoneNumber, String password) {
        UserEntity userEntity = userRepository.findFirstByPhoneNumber(phoneNumber);
        if (userEntity != null &&
                passwordEncoder.matches(password, userEntity.getPassword())
        ) {
            if (userEntity.getRoleByRoleId().getName().equals(ExffRole.ROLE_ADMIN)) {
                Map<String, Object> data = new HashMap<>();
                data.put(
                        TokenAuthenticationService.HEADER_STRING,
                        TokenAuthenticationService.createToken(userEntity));
                data.put("User", userEntity);
                return new ResponseEntity<>(data, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(new ExffMessage("Cannot login"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ExffMessage("Cannot login"), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity changePassword(String phoneNumber, String oldPassword, String newPassword) {
        UserEntity userEntity = userRepository.findFirstByPhoneNumber(phoneNumber);
        if (userEntity != null && passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
            try {
                userEntity.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(userEntity);
            } catch (Exception e) {
                return new ResponseEntity<>(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
            }
            Map<String, Object> data = new HashMap<>();
            data.put(
                    TokenAuthenticationService.HEADER_STRING,
                    TokenAuthenticationService.createToken(userEntity));
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else if (!passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
            return new ResponseEntity<>(new ExffMessage("Wrong Password"), HttpStatus.BAD_REQUEST);
        }

        return null;
    }


    @Override
    public ResponseEntity register(String phoneNumber, String password, String fullname, String address) {
        return register(phoneNumber, password, fullname, address, this.userRole);
    }

    @Override
    public ResponseEntity register(String phoneNumber, String password, String fullname, String address, RoleEntity roleEntity) {
        UserEntity userEntity = new UserEntity();
        try {
            userEntity.setPhoneNumber(phoneNumber);
            userEntity.setPassword(passwordEncoder.encode(password));
            userEntity.setFullName(fullname);
            userEntity.setAddress(address);
            userEntity.setStatus(ExffStatus.USER_ENABLE);
            userEntity.setRoleByRoleId(roleEntity);
            userEntity = userRepository.save(userEntity);
        } catch (Exception e) {
            return new ResponseEntity<>(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity updateUserInfo(String phoneNumber, String fullName, String address, String avatar) {
        UserEntity userEntity = new UserEntity();
//        RoleEntity roleEntity = roleRepository.findTop1ByName(ExffRole.ROLE_USER);
        userEntity = userRepository.findFirstByPhoneNumber(phoneNumber);

        if (userEntity != null) {
            userEntity.setAvatar(avatar);
            userEntity.setFullName(fullName);
            userEntity.setAddress(address);
//            userEntity.setRoleByRoleId(roleEntity);
            userRepository.save(userEntity);
            Map<String, Object> data = new HashMap<>();
            data.put("User", userEntity);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ExffMessage("Cannot Update"), HttpStatus.BAD_REQUEST);

        }

    }


    @Override
    public ResponseEntity getAllEnableUser() {
        List users;
        try {
            RoleEntity roleEntity = roleRepository.findTop1ByName(ExffRole.ROLE_USER);
            users = userRepository.findAllByRoleByRoleIdAndStatus(roleEntity, ExffStatus.USER_ENABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(new ExffMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @Override
    public ResponseEntity getAllDisableUser() {
        List users;
        try {
            RoleEntity roleEntity = roleRepository.findTop1ByName(ExffRole.ROLE_USER);
            users = userRepository.findAllByRoleByRoleIdAndStatus(roleEntity, ExffStatus.USER_DISABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(new ExffMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity banUser(int id) {
        try {
            UserEntity userEntity = getUserById(id);
            if (userEntity != null) {

                if (!userEntity.getRoleByRoleId().getName().equals(ExffRole.ROLE_ADMIN)) {
                    userEntity.setStatus(ExffStatus.USER_DISABLE);
                    userRepository.save(userEntity);
                    Map<String, Object> data = new HashMap<>();
                    data.put("User", userEntity);
                    return new ResponseEntity<>(data, HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(new ExffMessage("Cannot ban"), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(new ExffMessage("Cannot ban"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ExffMessage("Cannot ban"), HttpStatus.BAD_REQUEST);
        }


    }

    @Override
    public UserEntity getUserById(int id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<UserEntity> findUsersByName(String name) {
        return userRepository.findUserEntitiesByFullName(name);
    }

    @Override
    public UserEntity findUserByPhone(String phone) {
        return userRepository.findUserEntitiesByPhoneNumber(phone);
    }

    @Override
    public List<UserEntity> findUsersbyPhoneNumberList(ArrayList<String> phoneNumberList) {
        return userRepository.findByPhoneNumberInAndStatus(phoneNumberList, ExffStatus.USER_ENABLE);
    }


}
