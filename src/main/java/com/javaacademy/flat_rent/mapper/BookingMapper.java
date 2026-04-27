package com.javaacademy.flat_rent.mapper;

import com.javaacademy.flat_rent.dto.BookingRqDto;
import com.javaacademy.flat_rent.dto.BookingRsDto;
import com.javaacademy.flat_rent.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "advert", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    Booking toEntity(BookingRqDto dto);

    BookingRsDto toDto(Booking booking);
}
