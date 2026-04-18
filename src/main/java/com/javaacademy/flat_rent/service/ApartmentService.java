package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.ApartmentRqDto;
import com.javaacademy.flat_rent.dto.ApartmentRsDto;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.mapper.ApartmentMapstruct;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApartmentService {
    private final ApartmentMapstruct apartmentMapstruct;
    private final ApartmentRepository apartmentRepository;

    public ApartmentRsDto save(ApartmentRqDto dto) {
        Apartment apartment = apartmentMapstruct.toEntity(dto);
        Apartment saved = apartmentRepository.save(apartment);
        System.out.println(saved);
        ApartmentRsDto rsDto = apartmentMapstruct.toDto(saved);
        System.out.println(rsDto);
        return rsDto;
    }
}
