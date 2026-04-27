package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingRsDto(Long id,
                           @JsonProperty(value = "date_start")
                           LocalDate startDate,
                           @JsonProperty(value = "date_finish")
                           LocalDate endDate,
                           ClientRsDto client,
                           AdvertRsDto advert,
                           @JsonProperty(value = "result_price")
                           BigDecimal totalPrice) {}
