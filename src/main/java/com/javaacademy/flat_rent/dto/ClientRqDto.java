package com.javaacademy.flat_rent.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание клиента")
public record ClientRqDto(Long id,
                          String name,
                          String email) {}
