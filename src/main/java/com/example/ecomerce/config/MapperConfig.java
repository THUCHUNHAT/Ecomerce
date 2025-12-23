package com.example.ecomerce.config;
import com.example.ecomerce.dto.OderResponseDto;
import com.example.ecomerce.entity.Order;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(Order.class, OderResponseDto.class).addMappings(m -> {
            m.map(src -> src.getUser().getPhone(), OderResponseDto::setPhoneNumber);
            m.map(src -> src.getUser().getUsername(), OderResponseDto::setUserName);
        });
        return mapper;
    }

}
