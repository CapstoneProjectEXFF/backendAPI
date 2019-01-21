package com.capstone.exff.services;

import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface UserServices {
    UserEntity login(String phoneNumber, String password);

}
