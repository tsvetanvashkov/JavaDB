package com.example.springdataautomappingobjects;

import com.example.springdataautomappingobjects.model.dto.GameAddDto;
import com.example.springdataautomappingobjects.model.dto.UserLoginDto;
import com.example.springdataautomappingobjects.model.dto.UserRegisterDto;
import com.example.springdataautomappingobjects.service.GameService;
import com.example.springdataautomappingobjects.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.math.BigDecimal;


@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final BufferedReader bufferedReader;
    private final UserService userService;
    private final GameService gameService;


    public CommandLineRunnerImpl(BufferedReader bufferedReader, UserService userService, GameService gameService) {
        this.bufferedReader = bufferedReader;
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void run(String... args) throws Exception {
        while(true){
            System.out.println("Enter your commands:");
            String[] commands = bufferedReader.readLine().split("\\|");

            switch (commands[0]){
                case "RegisterUser" -> userService
                        .registerUser(new UserRegisterDto(commands[1], commands[2],
                                commands[3], commands[4]));
                case "LoginUser" -> userService
                        .loginUser(new UserLoginDto(commands[1], commands[2]));
                case "Logout" -> userService
                        .logout();
                case "AddGame" -> gameService
                        .addGame(new GameAddDto(commands[1], new BigDecimal(commands[2]),
                                Double.parseDouble(commands[3]), commands[4],
                                commands[5], commands[6], commands[7]));
                case "EditGame" -> gameService
                        .editGame(Long.parseLong(commands[1]),
                                new BigDecimal(commands[2]), Double.parseDouble(commands[3]));

            }

        }
    }
}
