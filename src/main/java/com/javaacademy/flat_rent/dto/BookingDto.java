package com.javaacademy.flat_rent.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingDto(Long id,
                         LocalDate startDate,
                         LocalDate endDate,
                         ClientRsDto client,
                         AdvertRqDto advert,
                         BigDecimal totalPrice) {
}
