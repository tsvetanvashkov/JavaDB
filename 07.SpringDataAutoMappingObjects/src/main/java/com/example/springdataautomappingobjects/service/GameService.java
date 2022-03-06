package com.example.springdataautomappingobjects.service;

import com.example.springdataautomappingobjects.model.dto.GameAddDto;

import java.math.BigDecimal;

public interface GameService {

    void addGame(GameAddDto gameAddDto);

    void editGame(Long gameId, BigDecimal price, Double size);
}
