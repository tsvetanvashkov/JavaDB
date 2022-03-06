package com.example.springdataautomappingobjects.service;

import com.example.springdataautomappingobjects.model.dto.UserLoginDto;
import com.example.springdataautomappingobjects.model.dto.UserRegisterDto;

public interface UserService {

    void registerUser(UserRegisterDto userRegisterDto);

    void loginUser(UserLoginDto userLoginDto);

    void logout();
}
