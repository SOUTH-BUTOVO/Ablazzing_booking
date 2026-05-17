package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaacademy.flat_rent.entity.Client;

import java.time.LocalDate;

public record BookingRqDto(Long id,
                           @JsonProperty(value = "date_start")
                           LocalDate startDate,
                           @JsonProperty(value = "date_finish")
                           LocalDate endDate,
                           ClientRqDto client,
                           @JsonProperty(value = "advert_id")
                           Long advertId) {}
