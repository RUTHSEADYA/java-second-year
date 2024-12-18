package com.example.demo.services;

import com.example.demo.model.Flowers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlowersRepository extends JpaRepository<Flowers,Long> {
    // חיפוש משולב
}
