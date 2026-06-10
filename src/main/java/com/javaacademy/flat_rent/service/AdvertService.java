package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.AdvertRqDto;
import com.javaacademy.flat_rent.dto.AdvertRsDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.mapper.AdvertMapper;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdvertService {
    private final AdvertMapper advertMapper;
    private final AdvertRepository advertRepository;

    @Transactional
    public AdvertRsDto save(AdvertRqDto dto) {
        Advert advert = advertMapper.toEntity(dto);
        Advert saved = advertRepository.save(advert);
        return advertMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public Page<AdvertRsDto> findAllByCity(String city, Pageable pageable) {
        return advertRepository.findAllByApartmentCity(city, pageable)
                .map(advertMapper::toDto);
    }
}
