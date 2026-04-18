package com.javaacademy.flat_rent.mapper;

import com.javaacademy.flat_rent.dto.AdvertRqDto;
import com.javaacademy.flat_rent.dto.AdvertRsDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
import com.javaacademy.flat_rent.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ApartmentMapstruct.class)
public abstract class AdvertMapstruct {
    @Autowired
    protected BookingRepository bookingRepository;
    @Autowired
    protected ApartmentRepository apartmentRepository;

    @Mapping(target = "price", defaultValue = "0")
    @Mapping(target = "isActive", defaultValue = "true")
    @Mapping(target = "apartment", source = "apartment", qualifiedByName = "getApartment")
    @Mapping(target = "bookings", ignore = true)
    public abstract Advert toEntity(AdvertRqDto dto);

    @Mapping(target = "bookings", source = "bookings", qualifiedByName = "getBookings")
    public abstract AdvertRsDto toDto(Advert advert);

    @Named("getApartment")
    protected Apartment getApartment(Long id) {
        if (id == null) return null;
        return apartmentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Apartment %s, not found".formatted(id)));
    }

    @Named("getBookings")
    protected Set<Long> getBookings(Set<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) return Set.of();
        return bookings.stream().map(Booking::getId).collect(Collectors.toSet());
    }

    public Long mapApartmentToId(Apartment apartment) {
        return apartment.getId();
    }
}
