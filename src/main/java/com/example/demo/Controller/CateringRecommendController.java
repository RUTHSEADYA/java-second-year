package com.example.demo.Controller;

import com.example.demo.model.*;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/catering/recommendations")
public class CateringRecommendController {

        @Autowired
        private CateringRecommendReposirory cateringRecommendReposirory;

        @Autowired
        private CateringRepository cateringRepository;

        public CateringRecommendController(CateringRecommendReposirory cateringRecommendReposirory, CateringRepository cateringRepository) {
            this.cateringRecommendReposirory = cateringRecommendReposirory;
            this.cateringRepository = cateringRepository;
        }



        @GetMapping("/getRecommendations/{cateringId}")
        public ResponseEntity<List<CateringRecommend>> getRecommendations(@PathVariable Long cateringId) {
            Catering catering = cateringRepository.findById(cateringId).orElse(null);
            if (catering == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<CateringRecommend> recommendations = cateringRecommendReposirory.findByCatering(catering);
            return new ResponseEntity<>(recommendations, HttpStatus.OK);
        }


        // Get a specific recommendation by ID
        @GetMapping("/getRecommendById/{id}")
        public ResponseEntity<CateringRecommend> getRecommendation(@PathVariable Long id) {
            CateringRecommend recommendation = cateringRecommendReposirory.findById(id).orElse(null);
            if (recommendation == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(recommendation, HttpStatus.OK);
        }

        // Add a new recommendation to a specific flower
        @PostMapping("/addRecommendation/{cateringId}")
        public ResponseEntity<CateringRecommend> addRecommendation(@PathVariable Long cateringId, @RequestBody CateringRecommend recommendation) {
            Catering catering = cateringRepository.findById(cateringId).orElse(null);

            if (catering == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            recommendation.setCatering(catering); // קישור ההמלצה לפרח
            CateringRecommend newRecommendation = cateringRecommendReposirory.save(recommendation); // שמירת ההמלצה
            return new ResponseEntity<>(newRecommendation, HttpStatus.CREATED);
        }


        @PutMapping("/updateRecommendation/{id}")
        public ResponseEntity<Void> updateRecommendation(@PathVariable Long id, @RequestBody CateringRecommend recommendationDetails) {
            CateringRecommend recommendation = cateringRecommendReposirory.findById(id).orElse(null);

            if (recommendation == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            recommendation.setReviewerName(recommendationDetails.getReviewerName());
            recommendation.setComment(recommendationDetails.getComment());
            recommendation.setRating(recommendationDetails.getRating());
            recommendation.setDate(recommendationDetails.getDate());

            cateringRecommendReposirory.save(recommendation);
            return new ResponseEntity<>(HttpStatus.OK);
        }



        // Delete a recommendation by ID
        @DeleteMapping("/deleteRecommendation/{id}")
        public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
            if (cateringRecommendReposirory.existsById(id)) {
                cateringRecommendReposirory.deleteById(id);
                return ResponseEntity.noContent().build(); // מחזיר 204 אם המחיקה הצליחה
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }



