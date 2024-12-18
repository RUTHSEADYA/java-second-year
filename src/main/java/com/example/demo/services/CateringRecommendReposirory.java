package com.example.demo.services;

import com.example.demo.model.Catering;
import com.example.demo.model.CateringRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CateringRecommendReposirory extends JpaRepository<CateringRecommend, Long> {
    List<CateringRecommend> findByCatering(Catering catering);
}
