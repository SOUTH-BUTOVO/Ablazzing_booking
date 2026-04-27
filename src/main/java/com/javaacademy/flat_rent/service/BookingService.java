package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.BookingRqDto;
import com.javaacademy.flat_rent.dto.BookingRsDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.entity.Client;
import com.javaacademy.flat_rent.mapper.BookingMapper;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import com.javaacademy.flat_rent.repository.BookingRepository;
import com.javaacademy.flat_rent.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingMapper bookingMapper;
    private final AdvertRepository advertRepository;
    private final ClientRepository clientRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public BookingRsDto save(BookingRqDto dto) {
        Advert advert = advertRepository.findById(dto.advertId()).orElseThrow(() ->
                new EntityNotFoundException("Advert %s, not found".formatted(dto.advertId())));
        Client client = clientRepository.findById(dto.clientId()).orElseThrow(() ->
                new EntityNotFoundException("Client %s, not found".formatted(dto.clientId())));

        Booking booking = bookingMapper.toEntity(dto);
        advert.addBooking(booking);
        client.addBooking(booking);
        Booking saved = bookingRepository.save(booking);
        System.out.println(saved);
        return bookingMapper.toDto(saved);
    }
}
