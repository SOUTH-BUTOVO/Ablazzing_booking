package com.javaacademy.flat_rent.repository;

import com.javaacademy.flat_rent.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
