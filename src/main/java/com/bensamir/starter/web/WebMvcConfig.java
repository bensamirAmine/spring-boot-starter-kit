package com.bensamir.starter.web;

import com.bensamir.starter.properties.StarterKitProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class WebMvcConfig implements WebMvcConfigurer {

    private final StarterKitProperties properties;

    public WebMvcConfig(StarterKitProperties properties) {
        this.properties = properties;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Configure Jackson with common settings
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .build();

        converters.add(0, new MappingJackson2HttpMessageConverter(objectMapper));
    }
}