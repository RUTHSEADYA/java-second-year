package com.example.demo.services;

import com.example.demo.model.Hall;
import com.example.demo.model.HallRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HallRecommendRepository extends JpaRepository<HallRecommend,Long> {
    List<HallRecommend> findByHall(Hall hall);

}
