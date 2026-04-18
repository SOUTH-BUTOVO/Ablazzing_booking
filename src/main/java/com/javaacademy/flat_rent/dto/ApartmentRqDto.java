package com.javaacademy.flat_rent.dto;

import com.javaacademy.flat_rent.entity.RoomCount;

public record ApartmentRqDto(Long id,
                             String city,
                             String street,
                             String house,
                             String corpus,
                             RoomCount roomCount) {
}
