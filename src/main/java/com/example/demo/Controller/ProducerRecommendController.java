package com.example.demo.Controller;

import com.example.demo.model.Producer;
import com.example.demo.model.ProducerRecommend;

import com.example.demo.services.ProducerRecommendRepository;
import com.example.demo.services.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/producerRecommend")
public class ProducerRecommendController {
    @Autowired
    private ProducerRepository producerRepository;
    @Autowired
    private ProducerRecommendRepository producerRecommendRepository;

    public ProducerRecommendController(ProducerRepository producerRepository, ProducerRecommendRepository producerRecommendRepository) {
        this.producerRepository = producerRepository;
        this.producerRecommendRepository = producerRecommendRepository;
    }
    @GetMapping("/getRecommendations/{producerId}")
    public ResponseEntity<List<ProducerRecommend>>getAllProducerRecommend(@PathVariable Long producerId) {
        Producer producer=producerRepository.findById(producerId).orElse(null);
        if(producer==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<ProducerRecommend>recommedations=producerRecommendRepository.findByProducer(producer);
        return new ResponseEntity<>(recommedations,HttpStatus.OK);

    }




    @GetMapping("/getRcommendById/{id}")
    public ResponseEntity<ProducerRecommend>getRecommendById(@PathVariable Long id) {
        ProducerRecommend producerRecommend = producerRecommendRepository.findById(id).orElse(null);
        if (producerRecommend == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(producerRecommend, HttpStatus.OK);

    }
    @PostMapping("/addRecommendadion/{producerId}")
    public ResponseEntity<ProducerRecommend> addRecommendation(@PathVariable Long producerId, @RequestBody ProducerRecommend recommendation) {
        Producer producer = producerRepository.findById(producerId).orElse(null);

        if (producer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recommendation.setProducer(producer); // קישור ההמלצה לפרח
        ProducerRecommend newRecommendation = producerRecommendRepository.save(recommendation); // שמירת ההמלצה
        return new ResponseEntity<>(newRecommendation, HttpStatus.CREATED);
    }


    @PutMapping("/updateRecommendation/{id}")
    public ResponseEntity<Void> updateRecommendation(@PathVariable Long id, @RequestBody ProducerRecommend recommendationDetails) {
        ProducerRecommend recommendation = producerRecommendRepository.findById(id).orElse(null);

        if (recommendation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recommendation.setReviewerName(recommendationDetails.getReviewerName());
        recommendation.setComment(recommendationDetails.getComment());
        recommendation.setRating(recommendationDetails.getRating());
        recommendation.setDate(recommendationDetails.getDate());

        producerRecommendRepository.save(recommendation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

@DeleteMapping("/deleteRecommend/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
if (producerRecommendRepository.existsById(id)) {
producerRecommendRepository.deleteById(id);
    return ResponseEntity.noContent().build(); // מחזיר 204 אם המחיקה הצליחה
}
else
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}

}
