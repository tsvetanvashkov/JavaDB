package com.example.springdataautomappingobjects.service.impl;

import com.example.springdataautomappingobjects.model.dto.GameAddDto;
import com.example.springdataautomappingobjects.model.entity.Game;
import com.example.springdataautomappingobjects.repository.GameRepository;
import com.example.springdataautomappingobjects.repository.UserRepository;
import com.example.springdataautomappingobjects.service.GameService;
import com.example.springdataautomappingobjects.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public GameServiceImpl(GameRepository gameRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void addGame(GameAddDto gameAddDto) {
        Set<ConstraintViolation<GameAddDto>> violations = validationUtil.getViolations(gameAddDto);
        if (!violations.isEmpty()){
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return;
        }

        Game game = modelMapper.map(gameAddDto, Game.class);
        game.setReleaseDate(LocalDate.parse(gameAddDto.getReleaseDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        gameRepository.save(game);

        System.out.println("Added game " + gameAddDto.getTitle());

    }

    @Override
    public void editGame(Long gameId, BigDecimal price, Double size) {
        Game game = gameRepository
                .findById(gameId)
                .orElse(null);
        if (game == null){
            System.out.println("Id is not correct");
            return;
        }
        game.setPrice(price);
        game.setSize(size);

        gameRepository.save(game);

        System.out.println("Edited game " + game.getTitle());

    }
}
