package com.example.jsonex.service;

import com.example.jsonex.model.dto.UserSoldDto;
import com.example.jsonex.model.entity.User;

import java.io.IOException;
import java.util.List;

public interface UserService {
    void seedUsers() throws IOException;

    User findRandomUser();

    List<UserSoldDto> findAllUsersWithMoreThanOneSoldProducts();
}
