package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaacademy.flat_rent.entity.RoomCount;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание апартаментов")
public record ApartmentRqDto(Long id,
                             String city,
                             String street,
                             String house,
                             String corpus,
                             @JsonProperty(value = "apartment_type")
// Для enum Swagger обычно сам покажет допустимые значения. Более того: это не одно значение, а список значений.
// example должен быть одним примером, для enum можно вообще ничего не писать.
                             @Schema(example = "ROOM, ONE, TWO, THREE, FOUR_PLUS")
                             RoomCount roomCount) {}
