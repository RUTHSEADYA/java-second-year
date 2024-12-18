package com.example.demo.services;

import com.example.demo.model.PhotoRecommend;
import com.example.demo.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotograperRecommendRepository extends JpaRepository<PhotoRecommend,Long> {
    List<PhotoRecommend> findByPhotographer(Photographer photographer);
}
