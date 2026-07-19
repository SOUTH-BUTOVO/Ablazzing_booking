package com.javaacademy.flat_rent.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info()// models.info.Info;
                .title("API аренды недвижимости")
                .description("Публичное api аренды недвижимости")
                .contact(new Contact() // models.info.Contact;
                        .name("Admin")
                        .email("admin@mail.com")
                        .url("www.admin-rent.com"))
                .version("1.0.1"));
    }
}
