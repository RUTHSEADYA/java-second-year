package com.example.demo.Controller;

import com.example.demo.model.PhotoRecommend;
import com.example.demo.model.Photographer;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/photographerRecommend")
public class PhotographerRecommendController {
    @Autowired
    private PhotograperRepository photograperRepository;
    @Autowired
    private PhotograperRecommendRepository photograperRecommendRepository;

    public PhotographerRecommendController(PhotograperRepository photograperRepository, PhotograperRecommendRepository photograperRecommendRepository) {
        this.photograperRepository = photograperRepository;
        this.photograperRecommendRepository = photograperRecommendRepository;
    }
    @GetMapping("/getRecommendations/{photographerId}")
    public ResponseEntity<List<PhotoRecommend>>getAllPhotographerRecommend(@PathVariable Long photographerId) {
        Photographer photographer=photograperRepository.findById(photographerId).orElse(null);
        if(photographer==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<PhotoRecommend>recommedations=photograperRecommendRepository.findByPhotographer(photographer);
        return new ResponseEntity<>(recommedations,HttpStatus.OK);

    }




    @GetMapping("/getRcommendById/{id}")
    public ResponseEntity<PhotoRecommend>getRecommendById(@PathVariable Long id) {
        PhotoRecommend photoRecommend = photograperRecommendRepository.findById(id).orElse(null);
        if (photoRecommend == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(photoRecommend, HttpStatus.OK);

    }
    @PostMapping("/addRecommendadion/{photographerId}")
    public ResponseEntity<PhotoRecommend> addRecommendation(@PathVariable Long photographerId , @RequestBody PhotoRecommend recommendation) {
        Photographer photographer = photograperRepository.findById(photographerId).orElse(null);

        if (photographer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recommendation.setPhotographer(photographer); // קישור ההמלצה לפרח
        PhotoRecommend newRecommendation = photograperRecommendRepository.save(recommendation); // שמירת ההמלצה
        return new ResponseEntity<>(newRecommendation, HttpStatus.CREATED);
    }


    @PutMapping("/updateRecommendation/{id}")
    public ResponseEntity<Void> updateRecommendation(@PathVariable Long id, @RequestBody PhotoRecommend recommendationDetails) {
        PhotoRecommend recommendation = photograperRecommendRepository.findById(id).orElse(null);

        if (recommendation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recommendation.setReviewerName(recommendationDetails.getReviewerName());
        recommendation.setComment(recommendationDetails.getComment());
        recommendation.setRating(recommendationDetails.getRating());
        recommendation.setDate(recommendationDetails.getDate());

        photograperRecommendRepository.save(recommendation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteRecommend/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        if (photograperRecommendRepository.existsById(id)) {
            photograperRecommendRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // מחזיר 204 אם המחיקה הצליחה
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
