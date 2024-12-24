package com.example.demo.Controller;

import com.example.demo.model.*;
import com.example.demo.services.SingersRecommendRepository;
import com.example.demo.services.SingersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/singersRecommendController")
public class SingersRecommendController {

    @Autowired
    private SingersRepository singersRepository;
    @Autowired
    private SingersRecommendRepository singersRecommendRepository;

    public SingersRecommendController(SingersRepository singersRepository, SingersRecommendRepository singersRecommendRepository) {
        this.singersRepository = singersRepository;
        this.singersRecommendRepository = singersRecommendRepository;

    }

    @GetMapping("/getRecommendations/{singerId}")
    public ResponseEntity<List<SingersRecommend>>getAllProducerRecommend(@PathVariable Long singerId) {
        Singers singers=singersRepository.findById(singerId).orElse(null);
        if(singers==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<SingersRecommend>recommedations=singersRecommendRepository.findBySingers(singers);
        return new ResponseEntity<>(recommedations,HttpStatus.OK);

    }
    @GetMapping("/getRecommendById/{id}")
    public ResponseEntity<SingersRecommend> getRecommendById(@PathVariable Long id){
        SingersRecommend singersRecommend=singersRecommendRepository.findById(id).orElse(null);
        if(singersRecommend==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
return new ResponseEntity<>(singersRecommend, HttpStatus.OK);    }


    @PostMapping("/addSingerRecommend/{singerId}")
    public ResponseEntity<SingersRecommend> addSingerRecommend(@PathVariable Long singerId,@RequestBody SingersRecommend recommendations){
        Singers singers=singersRepository.findById(singerId).orElse(null);
        if(singers==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        recommendations.setSingers(singers);
        SingersRecommend newRcommendation=singersRecommendRepository.save(recommendations);
        return new ResponseEntity<>(newRcommendation, HttpStatus.OK);
    }



    @PutMapping("/updateRecommendation/{id}")
    public ResponseEntity<Void> updateRecommendation(@PathVariable Long id, @RequestBody SingersRecommend recommendationDetails) {
        SingersRecommend recommendation = singersRecommendRepository.findById(id).orElse(null);

        if (recommendation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recommendation.setReviewerName(recommendationDetails.getReviewerName());
        recommendation.setComment(recommendationDetails.getComment());
        recommendation.setRating(recommendationDetails.getRating());
        recommendation.setDate(recommendationDetails.getDate());

        singersRecommendRepository.save(recommendation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteRecommend/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        if (singersRecommendRepository.existsById(id)) {
            singersRecommendRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // מחזיר 204 אם המחיקה הצליחה
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}


