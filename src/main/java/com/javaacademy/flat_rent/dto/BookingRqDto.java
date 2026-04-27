package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record BookingRqDto(Long id,
                           @JsonProperty(value = "date_start")
                           LocalDate startDate,
                           @JsonProperty(value = "date_finish")
                           LocalDate endDate,
                           @JsonProperty(value = "client_id")
                           Long clientId,
                           @JsonProperty(value = "advert_id")
                           Long advertId) {}
