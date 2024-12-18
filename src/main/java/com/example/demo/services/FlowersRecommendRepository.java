package com.example.demo.services;

import com.example.demo.model.Flowers;
import com.example.demo.model.FlowersRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlowersRecommendRepository extends JpaRepository<FlowersRecommend,Long> {
    List<FlowersRecommend> findByFlower(Flowers flower);
}
