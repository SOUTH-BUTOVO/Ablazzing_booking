package com.javaacademy.flat_rent.mapper;

import com.javaacademy.flat_rent.dto.ClientRqDto;
import com.javaacademy.flat_rent.dto.ClientRsDto;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "bookings", ignore = true)
    public abstract Client toEntity(ClientRqDto dto);

    ClientRsDto toDto(Client entity);

    default Long mapToClientId(Booking booking) {
        return booking.getId();
    }
}
