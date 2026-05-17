package com.javaacademy.flat_rent.repository;

import com.javaacademy.flat_rent.entity.Advert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertRepository extends JpaRepository<Advert, Long> {

    Page<Advert> findAllByApartmentCity(String city, Pageable pageable);
}
