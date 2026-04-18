package com.javaacademy.flat_rent.mapper;

import com.javaacademy.flat_rent.dto.ApartmentRqDto;
import com.javaacademy.flat_rent.dto.ApartmentRsDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AdvertMapstruct.class)
public abstract class ApartmentMapstruct {

    @Mapping(target = "adverts", ignore = true)
    @Mapping(target = "corpus", defaultValue = "нет")
    public abstract Apartment toEntity(ApartmentRqDto dto);

    public abstract ApartmentRsDto toDto(Apartment apartment);

    protected Long mapAdvertToId(Advert advert) {
        return advert.getId();
    }
}
