package com.example.demo.services;

import com.example.demo.model.Singers;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SingersRepository extends JpaRepository<Singers, Long> {

}
