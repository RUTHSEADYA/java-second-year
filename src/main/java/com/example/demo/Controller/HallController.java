

package com.example.demo.Controller;

import com.example.demo.model.Hall;
import com.example.demo.services.HallRepository;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping({"api/hall"})
public class HallController {
    @Autowired
    private final HallRepository hallRepository;

    //ניתוב של הפרויקט הנוכחי
    private static String DIRECTORY_PATH = System.getProperty("user.dir") + "//images//";



    @Autowired
    public HallController(HallRepository hallRepository){
        this.hallRepository = hallRepository;
    }

    @GetMapping({"/getHalls"})
    public ResponseEntity<List<Hall>> getAllHalls() {
        return new ResponseEntity(this.hallRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/getHall/{id}")
    public ResponseEntity<Hall> getHallById(@PathVariable Long id) {
        Hall h = hallRepository.findById(id).orElse(null);
        if (h == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(h, HttpStatus.OK);
    }



    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestPart("hall") String hallJson,
            @RequestPart(value = "image", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Hall hall = objectMapper.readValue(hallJson, Hall.class);

            // אם התמונה לא נשלחה, דלג על לוגיקת שמירת התמונה
            if (file != null && !file.isEmpty()) {
                String relativePath = "/src/photo/halls/" + file.getOriginalFilename();
                Path pathFile = Paths.get(DIRECTORY_PATH, file.getOriginalFilename());
                Files.write(pathFile, file.getBytes());
                hall.setImageUrl(relativePath); // עדכון הנתיב היחסי רק אם יש תמונה
            }

            Hall newHall = hallRepository.save(hall);

            return new ResponseEntity<>(newHall, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping({"/addHall"})
    public ResponseEntity<Hall> addHall(@RequestBody Hall hall) {
        Hall newHall = (Hall) this.hallRepository.save(hall);
        return new ResponseEntity(newHall, HttpStatus.CREATED);
    }


@PutMapping(value = "/updateHall/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<Hall> updateHall(
        @RequestPart("hall") String hallJson,
        @RequestPart(value = "image", required = false) MultipartFile file,
        @PathVariable Long id) {
    try {
        Hall existingHall = hallRepository.findById(id).orElse(null);
        if (existingHall == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Hall updatedHall = objectMapper.readValue(hallJson, Hall.class);

        existingHall.setName(updatedHall.getName());
        existingHall.setDescription(updatedHall.getDescription());
        existingHall.setPosition(updatedHall.getPosition());
        existingHall.setPhone(updatedHall.getPhone());

        // אם הועלתה תמונה, נשמור אותה ונעדכן את הנתיב
        if (file != null && !file.isEmpty()) {
            String relativePath = "/src/photo/halls/" + file.getOriginalFilename();
            Path pathFile = Paths.get(DIRECTORY_PATH, file.getOriginalFilename());
            Files.write(pathFile, file.getBytes());
            existingHall.setImageUrl(relativePath);
        }

        Hall savedHall = hallRepository.save(existingHall);
        return new ResponseEntity<>(savedHall, HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
    @DeleteMapping({"/deleteHall/{id}"})
    public ResponseEntity<Hall> deleteHall(@PathVariable Long id) {
        this.hallRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }





}