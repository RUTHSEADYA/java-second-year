package com.example.demo.services;

import com.example.demo.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HallRepository extends JpaRepository<Hall, Long> {

    }