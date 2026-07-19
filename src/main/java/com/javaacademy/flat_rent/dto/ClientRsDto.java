package com.javaacademy.flat_rent.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ клиент")
public record ClientRsDto(Long id,
                          String name,
                          String email) {}
