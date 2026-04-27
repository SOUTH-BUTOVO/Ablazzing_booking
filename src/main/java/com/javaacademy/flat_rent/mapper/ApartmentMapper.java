package com.javaacademy.flat_rent.mapper;

import com.javaacademy.flat_rent.dto.ApartmentRqDto;
import com.javaacademy.flat_rent.dto.ApartmentRsDto;
import com.javaacademy.flat_rent.entity.Apartment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AdvertMapper.class)
public interface ApartmentMapper {

    @Mapping(target = "adverts", ignore = true)
    @Mapping(target = "corpus", defaultValue = "нет")
    Apartment toEntity(ApartmentRqDto dto);

    ApartmentRsDto toDto(Apartment apartment);
}
