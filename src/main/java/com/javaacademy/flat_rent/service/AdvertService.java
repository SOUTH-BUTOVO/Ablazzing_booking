package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.AdvertRqDto;
import com.javaacademy.flat_rent.dto.AdvertRsDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.mapper.AdvertMapstruct;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvertService {
    private final AdvertMapstruct advertMapstruct;
    private final AdvertRepository advertRepository;

    public AdvertRsDto save(AdvertRqDto dto) {
        Advert advert = advertMapstruct.toEntity(dto);
        Advert saved = advertRepository.save(advert);
        System.out.println(saved);
        AdvertRsDto rsDto = advertMapstruct.toDto(saved);
        System.out.println(rsDto);
        return rsDto;
    }
}
