package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.AdvertRqDto;
import com.javaacademy.flat_rent.dto.AdvertRsDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.mapper.AdvertMapper;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import lombok.RequiredArgsConstructor;
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
        System.out.println(saved);
        AdvertRsDto rsDto = advertMapper.toDto(saved);
        System.out.println(rsDto);
        return rsDto;
    }
}
