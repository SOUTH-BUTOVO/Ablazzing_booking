package com.javaacademy.flat_rent.repository;

import com.javaacademy.flat_rent.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
}
