package com.example.demo.services;

import com.example.demo.model.Catering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CateringRepository extends JpaRepository<Catering, Long> {
}
