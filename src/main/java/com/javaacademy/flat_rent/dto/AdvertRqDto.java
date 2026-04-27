package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record AdvertRqDto(Long id,
                          BigDecimal price,
                          @JsonProperty(value = "is_active")
                          Boolean isActive,
                          @JsonProperty(value = "apartment_id")
                          Long apartmentId,
                          String description) {}
