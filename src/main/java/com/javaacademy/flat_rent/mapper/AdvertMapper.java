package com.javaacademy.flat_rent.mapper;

import com.javaacademy.flat_rent.dto.AdvertRqDto;
import com.javaacademy.flat_rent.dto.AdvertRsDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = ApartmentMapper.class)
public abstract class AdvertMapper {
    @Autowired
    protected ApartmentRepository apartmentRepository;

    @Mapping(target = "price", defaultValue = "0")
    @Mapping(target = "isActive", defaultValue = "true")
    @Mapping(target = "apartment", source = "apartmentId", qualifiedByName = "getApartment")
    @Mapping(target = "bookings", ignore = true)
    public abstract Advert toEntity(AdvertRqDto dto);

    public abstract AdvertRsDto toDto(Advert advert);

    @Named("getApartment")
    protected Apartment getApartment(Long id) {
        if (id == null) return null;
        return apartmentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Apartment %s, not found".formatted(id)));
    }
}
