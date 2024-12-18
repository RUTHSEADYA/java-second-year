package com.example.demo.services;

import com.example.demo.model.Producer;
import com.example.demo.model.ProducerRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProducerRecommendRepository extends JpaRepository<ProducerRecommend, Long> {
    List<ProducerRecommend> findByProducer(Producer producer);
}
