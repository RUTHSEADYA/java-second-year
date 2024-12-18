package com.example.demo.Controller;

import com.example.demo.model.Flowers;
import com.example.demo.model.FlowersRecommend;
import com.example.demo.services.FlowersRecommendRepository;
import com.example.demo.services.FlowersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/flowers/recommendations")
public class FlowersRecommendController {

    @Autowired
    private FlowersRecommendRepository flowersRecommendRepository;

    @Autowired
    private FlowersRepository flowersRepository;

    public FlowersRecommendController(FlowersRecommendRepository flowersRecommendRepository, FlowersRepository flowersRepository) {
        this.flowersRecommendRepository = flowersRecommendRepository;
        this.flowersRepository = flowersRepository;
    }



    @GetMapping("/getRecommendations/{flowerId}")
    public ResponseEntity<List<FlowersRecommend>> getRecommendations(@PathVariable Long flowerId) {
        Flowers flower = flowersRepository.findById(flowerId).orElse(null);
        if (flower == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<FlowersRecommend> recommendations = flowersRecommendRepository.findByFlower(flower);
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }


    // Get a specific recommendation by ID
    @GetMapping("/getRecommendById/{id}")
    public ResponseEntity<FlowersRecommend> getRecommendation(@PathVariable Long id) {
        FlowersRecommend recommendation = flowersRecommendRepository.findById(id).orElse(null);
        if (recommendation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(recommendation, HttpStatus.OK);
    }

    // Add a new recommendation to a specific flower
    @PostMapping("/addRecommendation/{flowerId}")
    public ResponseEntity<FlowersRecommend> addRecommendation(@PathVariable Long flowerId, @RequestBody FlowersRecommend recommendation) {
        Flowers flower = flowersRepository.findById(flowerId).orElse(null);

        if (flower == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recommendation.setFlower(flower); // קישור ההמלצה לפרח
        FlowersRecommend newRecommendation = flowersRecommendRepository.save(recommendation); // שמירת ההמלצה
        return new ResponseEntity<>(newRecommendation, HttpStatus.CREATED);
    }


    @PutMapping("/updateRecommendation/{id}")
    public ResponseEntity<Void> updateRecommendation(@PathVariable Long id, @RequestBody FlowersRecommend recommendationDetails) {
        FlowersRecommend recommendation = flowersRecommendRepository.findById(id).orElse(null);

        if (recommendation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recommendation.setReviewerName(recommendationDetails.getReviewerName());
        recommendation.setComment(recommendationDetails.getComment());
        recommendation.setRating(recommendationDetails.getRating());
        recommendation.setDate(recommendationDetails.getDate());

        flowersRecommendRepository.save(recommendation);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    // Delete a recommendation by ID
    @DeleteMapping("/deleteRecommendation/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        if (flowersRecommendRepository.existsById(id)) {
            flowersRecommendRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // מחזיר 204 אם המחיקה הצליחה
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

