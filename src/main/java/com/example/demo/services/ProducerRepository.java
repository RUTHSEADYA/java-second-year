package com.example.demo.services;

import com.example.demo.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProducerRepository extends JpaRepository<Producer, Long> {

}
