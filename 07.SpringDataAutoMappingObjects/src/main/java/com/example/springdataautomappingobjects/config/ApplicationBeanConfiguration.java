package com.example.springdataautomappingobjects.config;

import com.example.springdataautomappingobjects.model.dto.GameAddDto;
import com.example.springdataautomappingobjects.model.entity.Game;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public BufferedReader bufferedReader(){
        return new BufferedReader(new InputStreamReader(System.in));
    }

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        Converter<String, LocalDate> localDateConverter = new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
                return mappingContext.getSource() == null
                        ? LocalDate.now()
                        : LocalDate.parse(mappingContext.getSource(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
        };
        modelMapper.addConverter(localDateConverter);

        modelMapper
                .typeMap(GameAddDto.class, Game.class)
                .addMappings(mapper ->
                        mapper.map(GameAddDto::getThumbnailURL,
                                Game::setImageThumbnail));

        return modelMapper;
    }
}
