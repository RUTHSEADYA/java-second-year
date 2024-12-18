package com.example.demo.services;

import com.example.demo.model.Singers;
import com.example.demo.model.SingersRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SingersRecommendRepository extends JpaRepository<SingersRecommend, Long> {
    List<SingersRecommend> findBySingers(Singers singers);
}