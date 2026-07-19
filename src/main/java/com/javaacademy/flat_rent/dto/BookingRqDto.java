package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaacademy.flat_rent.entity.Client;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Запрос на создание бронирования")
public record BookingRqDto(Long id,
                           @JsonProperty(value = "date_start")
                           LocalDate startDate,
                           @JsonProperty(value = "date_finish")
                           LocalDate endDate,
                           ClientRqDto client,
                           @JsonProperty(value = "advert_id")
                           Long advertId) {}
