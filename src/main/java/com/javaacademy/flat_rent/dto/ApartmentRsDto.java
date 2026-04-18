package com.javaacademy.flat_rent.dto;

import com.javaacademy.flat_rent.entity.RoomCount;

import java.util.Set;

public record ApartmentRsDto(Long id,
                             String city,
                             String street,
                             String house,
                             String corpus,
                             RoomCount roomCount,
                             Set<Long> adverts) {
}
