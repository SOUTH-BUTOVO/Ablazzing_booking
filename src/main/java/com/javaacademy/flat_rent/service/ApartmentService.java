package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.ApartmentRqDto;
import com.javaacademy.flat_rent.dto.ApartmentRsDto;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.mapper.ApartmentMapper;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApartmentService {
    private final ApartmentMapper apartmentMapper;
    private final ApartmentRepository apartmentRepository;

    @Transactional
    public ApartmentRsDto save(ApartmentRqDto dto) {
        Apartment apartment = apartmentMapper.toEntity(dto);
        Apartment saved = apartmentRepository.save(apartment);
        System.out.println(saved);
        ApartmentRsDto rsDto = apartmentMapper.toDto(saved);
        System.out.println(rsDto);
        return rsDto;
    }
}
