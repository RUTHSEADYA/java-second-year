package com.example.demo.Controller;

import com.example.demo.model.Photographer;
import com.example.demo.services.PhotograperRepository;
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
@RequestMapping("api/photographer")
public class PhotographerController {
    @Autowired
    private PhotograperRepository photographerRepository;

    private static String DIRECTORY_PATH = System.getProperty("user.dir") + "//images//";

    public PhotographerController(PhotograperRepository photograperRepository) {
        this.photographerRepository = photograperRepository;
    }
    @GetMapping("/getPhotographers")
    public ResponseEntity<List<Photographer>> getAllPhotographers() {
        return new ResponseEntity<>(photographerRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/getPhotographer/{id}")
    public ResponseEntity<Photographer> getPhotographerById(@PathVariable Long id) {
        Photographer photographer = photographerRepository.findById(id).orElse(null);
        if (photographer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(photographer, HttpStatus.OK);
    }



    @PutMapping(value = "/updatePhotographer/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Photographer> updatePhotographer(
            @RequestPart("photographer") String photographerJson,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @PathVariable Long id) {
        try {
            Photographer existingPhotographer = photographerRepository.findById(id).orElse(null);
            if (existingPhotographer == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Photographer updatePhotographer = objectMapper.readValue(photographerJson, Photographer.class);

            existingPhotographer.setName(updatePhotographer.getName());
            existingPhotographer.setDescription(updatePhotographer.getDescription());
            existingPhotographer.setPhone(updatePhotographer.getPhone());

            // אם הועלתה תמונה, נשמור אותה ונעדכן את הנתיב
            if (file != null && !file.isEmpty()) {
                String relativePath = "/src/photo/photographers/" + file.getOriginalFilename();
                Path pathFile = Paths.get(DIRECTORY_PATH, file.getOriginalFilename());
                Files.write(pathFile, file.getBytes());
                existingPhotographer.setImageUrl(relativePath);
            }

            Photographer savedPhotographer = photographerRepository.save(existingPhotographer);
            return new ResponseEntity<>(savedPhotographer, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestPart("photographer") String photographerJson,
            @RequestPart(value = "image", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Photographer photographer = objectMapper.readValue(photographerJson, Photographer.class);

            // אם התמונה לא נשלחה, דלג על לוגיקת שמירת התמונה
            if (file != null && !file.isEmpty()) {
                String relativePath = "/src/photo/photographers/" + file.getOriginalFilename();
                Path pathFile = Paths.get(DIRECTORY_PATH, file.getOriginalFilename());
                Files.write(pathFile, file.getBytes());
                photographer.setImageUrl(relativePath); // עדכון הנתיב היחסי רק אם יש תמונה
            }

            Photographer newPhotographer = photographerRepository.save(photographer);
            System.out.println("Uploaded Photographer ID: " + newPhotographer.getId());

            return new ResponseEntity<>(newPhotographer, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping({"/deletePhotographer/{id}"})
    public ResponseEntity<Photographer> deletePhotographer(@PathVariable Long id) {
        this.photographerRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }



}
