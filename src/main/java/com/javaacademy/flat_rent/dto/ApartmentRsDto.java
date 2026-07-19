package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaacademy.flat_rent.entity.RoomCount;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ апартаменты")
public record ApartmentRsDto(Long id,
                             String city,
                             String street,
                             String house,
                             String corpus,
                             @JsonProperty(value = "apartment_type")
                             RoomCount roomCount) {}
