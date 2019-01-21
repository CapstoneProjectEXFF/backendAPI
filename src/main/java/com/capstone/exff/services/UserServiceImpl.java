package com.capstone.exff.services;

import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserServices{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity login(String phoneNumber, String password) {
        return userRepository.findFirstByPhoneNumberAndPassword(phoneNumber, password);
    }
}
