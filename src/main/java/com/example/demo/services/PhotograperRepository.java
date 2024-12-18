package com.example.demo.services;

import com.example.demo.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PhotograperRepository extends JpaRepository<Photographer,Long> {

}
