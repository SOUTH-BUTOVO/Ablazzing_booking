package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Ответ бронирование")
public record BookingRsDto(Long id,
                           @JsonProperty(value = "date_start")
                           LocalDate startDate,
                           @JsonProperty(value = "date_finish")
                           LocalDate endDate,
                           ClientRsDto client,
                           AdvertRsDto advert,
                           @JsonProperty(value = "result_price")
                           BigDecimal totalPrice) {}
