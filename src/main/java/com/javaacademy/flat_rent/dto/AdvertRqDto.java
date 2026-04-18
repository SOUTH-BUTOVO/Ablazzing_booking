package com.javaacademy.flat_rent.dto;

import java.math.BigDecimal;

public record AdvertRqDto(Long id,
                          BigDecimal price,
                          Boolean isActive,
                          Long apartment,
                          String description) {}
