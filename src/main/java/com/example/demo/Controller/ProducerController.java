package com.example.demo.Controller;

import com.example.demo.model.Producer;
import com.example.demo.services.ProducerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/producer")
public class ProducerController {

    @Autowired
    private ProducerRepository producerRepository;
    private static String DIRECTORY_PATH = System.getProperty("user.dir") + "//images//";

    public ProducerController(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }



    @GetMapping("/getProducers")
    public ResponseEntity<List<Producer>> getAllProducers() {
        return new ResponseEntity<>(producerRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/getProducer/{id}")
    public ResponseEntity<Producer> getProducerById(@PathVariable Long id) {
        Producer producer = producerRepository.findById(id).orElse(null);
        if (producer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(producer, HttpStatus.OK);
    }

    @PostMapping("/addProducer")
    public ResponseEntity<Producer> addProducer(@RequestBody Producer producer) {
        Producer newProducer = producerRepository.save(producer);
        return new ResponseEntity<>(newProducer, HttpStatus.CREATED);
    }



    @PutMapping(value = "/updateProducer/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producer> updateProducer(
            @RequestPart("producer") String producerJson,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @PathVariable Long id) {
        try {
            Producer existingProducer = producerRepository.findById(id).orElse(null);
            if (existingProducer == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Producer updatedProducer = objectMapper.readValue(producerJson, Producer.class);

            existingProducer.setName(updatedProducer.getName());
            existingProducer.setDescription(updatedProducer.getDescription());
            existingProducer.setPhone(updatedProducer.getPhone());

            // אם הועלתה תמונה, נשמור אותה ונעדכן את הנתיב
            if (file != null && !file.isEmpty()) {
                String relativePath = "/src/photo/producers/" + file.getOriginalFilename();
                Path pathFile = Paths.get(DIRECTORY_PATH, file.getOriginalFilename());
                Files.write(pathFile, file.getBytes());
                existingProducer.setImageUrl(relativePath);
            }

            Producer savedProducer = producerRepository.save(existingProducer);
            return new ResponseEntity<>(savedProducer, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteProducer/{id}")
    public ResponseEntity<Void> deleteProducer(@PathVariable Long id) {
        if (producerRepository.existsById(id)) {
            producerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestPart("producer") String producerJson,
            @RequestPart(value = "image", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Producer producer = objectMapper.readValue(producerJson, Producer.class);

            // אם התמונה לא נשלחה, דלג על לוגיקת שמירת התמונה
            if (file != null && !file.isEmpty()) {
                String relativePath = "/src/photo/producers/" + file.getOriginalFilename();
                Path pathFile = Paths.get(DIRECTORY_PATH, file.getOriginalFilename());
                Files.write(pathFile, file.getBytes());
                producer.setImageUrl(relativePath); // עדכון הנתיב היחסי רק אם יש תמונה
            }

            Producer newProducer = producerRepository.save(producer);
            System.out.println("Uploaded Photographer ID: " + newProducer.getId());

            return new ResponseEntity<>(newProducer, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

