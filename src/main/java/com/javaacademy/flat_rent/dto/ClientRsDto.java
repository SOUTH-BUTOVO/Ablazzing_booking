package com.javaacademy.flat_rent.dto;

import java.util.Set;

public record ClientRsDto(Long id,
                          String name,
                          String email,
                          Set<Long> bookings) {}
