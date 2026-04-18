package com.javaacademy.flat_rent.dto;

import java.math.BigDecimal;
import java.util.Set;

public record AdvertRsDto(Long id,
                          BigDecimal price,
                          Boolean isActive,
                          Long apartment,
                          String description,
                          Set<Long> bookings) {}
