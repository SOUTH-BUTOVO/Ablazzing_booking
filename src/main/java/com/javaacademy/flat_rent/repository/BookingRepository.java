package com.javaacademy.flat_rent.repository;

import com.javaacademy.flat_rent.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            select count(b) > 0
            from Booking b
            join b.advert a
            where a.apartment.id = :apartmentId
                and b.startDate < :endDate
                and b.endDate > :startDate""")
    boolean existsOverlappingBookings(
            @Param("apartmentId") Long apartmentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
            select b
            from Booking b
            join b.client c
            where lower(c.email) = lower(:email)""")
    @EntityGraph(attributePaths = {"advert", "apartment", "client"})
    Page<Booking> findAllByClientEmail(@Param("email") String email, Pageable pageable);

    @Modifying
    @Query(value = "delete from Booking b where b.client.id = :clientId")
    void deleteAllByClientId(@Param("clientId") Long clientId);
}
