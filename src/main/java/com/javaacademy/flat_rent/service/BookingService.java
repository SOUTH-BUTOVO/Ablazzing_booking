package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.BookingRqDto;
import com.javaacademy.flat_rent.dto.BookingRsDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.entity.Client;
import com.javaacademy.flat_rent.mapper.BookingMapper;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import com.javaacademy.flat_rent.repository.BookingRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingMapper bookingMapper;
    private final ClientService clientService;
    private final AdvertRepository advertRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public BookingRsDto save(BookingRqDto dto) {
        Advert advert = advertRepository.findById(dto.advertId()).orElseThrow(() ->
                new EntityNotFoundException("Advert %s, not found".formatted(dto.advertId())));
        checkAvailableBooking(advert.getApartment().getId(), dto.startDate(), dto.endDate());

        Client client = clientService.findExistingOrCreateClient(dto.client());
        Booking booking = bookingMapper.toEntity(dto);

        advert.addBooking(booking);
        client.addBooking(booking);
        booking.assignAdvert(advert);

        try {
            booking.setTotalPrice(calcBookingTotalPrice(dto.startDate(), dto.endDate(), advert.getPrice()));
            Booking saved = bookingRepository.save(booking);
            return bookingMapper.toDto(saved);
        } catch (DataIntegrityViolationException e) {
            throw new EntityExistsException("Date is occupied");
        }
    }

    @Transactional(readOnly = true)
    public Page<BookingRsDto> getAllBookingsByEmail(String email, Pageable pageable) {
        return bookingRepository.findAllByClientEmail(email, pageable).map(bookingMapper::toDto);
    }

    private void checkAvailableBooking(Long apartmentId, LocalDate startDate, LocalDate endDate) {
        if (bookingRepository.existsOverlappingBookings(apartmentId, startDate, endDate)) {
            throw new EntityExistsException("Date is occupied");
        }
    }

    private BigDecimal calcBookingTotalPrice(LocalDate start, LocalDate end, BigDecimal price) {
        long days = ChronoUnit.DAYS.between(start, end);
        if (days <= 1) {
            return price;
        }
        return BigDecimal.valueOf(days).multiply(price);
    }
}
