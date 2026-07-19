package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Запрос на создание объявления")
public record AdvertRqDto(Long id,
                          BigDecimal price,
                          @JsonProperty(value = "is_active")
                          @Schema(defaultValue = "true")
                          Boolean isActive,
                          @JsonProperty(value = "apartment_id")
                          Long apartmentId,
                          String description) {}
