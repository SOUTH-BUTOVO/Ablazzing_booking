package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record AdvertRsDto(Long id,
                          BigDecimal price,
                          @JsonProperty(value = "is_active")
                          Boolean isActive,
                          ApartmentRsDto apartment,
                          String description) {}
