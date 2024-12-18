package com.example.demo.Controller;


import com.example.demo.model.Hall;
import com.example.demo.model.HallRecommend;
import com.example.demo.services.HallRecommendRepository;
import com.example.demo.services.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/hall/recommendations")
public class HallRecommendController {

    @Autowired
    private HallRecommendRepository hallRecommendRepository;

    @Autowired
    private HallRepository hallRepository;

    public HallRecommendController(HallRecommendRepository hallRecommendRepository, HallRepository hallRepository) {
        this.hallRecommendRepository = hallRecommendRepository;
        this.hallRepository = hallRepository;
    }

    @GetMapping("/getRecommendations/{hallId}")
    public ResponseEntity<List<HallRecommend>> getRecommendations(@PathVariable Long hallId) {
        Hall hall = hallRepository.findById(hallId).orElse(null);
        if (hall == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<HallRecommend> recommendations = hallRecommendRepository.findByHall(hall);
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }

    @GetMapping("/getRecommend/{id}")
    public ResponseEntity<HallRecommend> getRecommend(@PathVariable Long id) {
        HallRecommend recommendation = hallRecommendRepository.findById(id).orElse(null);
        if (recommendation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(recommendation, HttpStatus.OK);
    }

    @PostMapping("/addRecommendation/{hallId}")
    public ResponseEntity<HallRecommend> addRecommendation(@PathVariable Long hallId, @RequestBody HallRecommend recommendation) {
        Hall hall = hallRepository.findById(hallId).orElse(null);

        if (hall == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recommendation.setHall(hall); // קישור ההמלצה לאולם
        HallRecommend newRecommendation = hallRecommendRepository.save(recommendation); // שמירת ההמלצה
        return new ResponseEntity<>(newRecommendation, HttpStatus.CREATED);
    }

    // Update an existing recommendation by ID
    @PutMapping("/updateRecommendation/{id}")
    public ResponseEntity<Void> updateRecommendation(@PathVariable Long id, @RequestBody HallRecommend recommendationDetails) {
        HallRecommend recommendation = hallRecommendRepository.findById(id).orElse(null);

        if (recommendation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recommendation.setReviewerName(recommendationDetails.getReviewerName());
        recommendation.setComment(recommendationDetails.getComment());
        recommendation.setRating(recommendationDetails.getRating());
        recommendation.setDate(recommendationDetails.getDate());

        hallRecommendRepository.save(recommendation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteRecommendation/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        if (hallRecommendRepository.existsById(id)) {
            hallRecommendRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // מחזיר 204 אם המחיקה הצליחה
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

