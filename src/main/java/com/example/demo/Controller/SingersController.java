package com.example.demo.Controller;

import com.example.demo.model.Singers;
import com.example.demo.services.SingersRepository;
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
@RequestMapping("api/singers")
public class SingersController {
    @Autowired
    private SingersRepository singersRepository;

    //ניתוב של הפרויקט הנוכחי
    private static String DIRECTORY_PATH = System.getProperty("user.dir") + "//images//";




    @GetMapping("/getSingers")
    public ResponseEntity<List<Singers>> getAllSingers() {
        return new ResponseEntity<>(singersRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/getSinger/{id}")
    public ResponseEntity<Singers> getSingerById(@PathVariable Long id) {
        Singers singer = singersRepository.findById(id).orElse(null);
        if (singer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(singer, HttpStatus.OK);
    }

    @PostMapping("/addSinger")
    public ResponseEntity<Singers> addSinger(@RequestBody Singers singer) {
        Singers newSinger = singersRepository.save(singer);
        return new ResponseEntity<>(newSinger, HttpStatus.CREATED);
    }



    @PutMapping(value = "/updateSinger/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Singers> updateSinger(
            @RequestPart("singer") String singerJson,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @PathVariable Long id) {
        try {
            Singers existingSinger = singersRepository.findById(id).orElse(null);
            if (existingSinger == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Singers updatedSinger = objectMapper.readValue(singerJson, Singers.class);

            existingSinger.setName(updatedSinger.getName());
            existingSinger.setDescription(updatedSinger.getDescription());
            existingSinger.setPhone(updatedSinger.getPhone());

            // אם הועלתה תמונה, נשמור אותה ונעדכן את הנתיב
            if (file != null && !file.isEmpty()) {
                String relativePath = "/src/photo/singers/" + file.getOriginalFilename();
                Path pathFile = Paths.get(DIRECTORY_PATH, file.getOriginalFilename());
                Files.write(pathFile, file.getBytes());
                existingSinger.setImageUrl(relativePath);
            }

            Singers savedSinger = singersRepository.save(existingSinger);
            return new ResponseEntity<>(savedSinger, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteSinger/{id}")
    public ResponseEntity<Void> deleteSinger(@PathVariable Long id) {
        if (singersRepository.existsById(id)) {
            singersRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestPart("singer") String singerJson,
            @RequestPart(value = "image", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Singers singers = objectMapper.readValue(singerJson, Singers.class);

            // אם התמונה לא נשלחה, דלג על לוגיקת שמירת התמונה
            if (file != null && !file.isEmpty()) {
                String relativePath = "/src/photo/singers/" + file.getOriginalFilename();
                Path pathFile = Paths.get(DIRECTORY_PATH, file.getOriginalFilename());
                Files.write(pathFile, file.getBytes());
                singers.setImageUrl(relativePath); // עדכון הנתיב היחסי רק אם יש תמונה
            }

            Singers newSinger= singersRepository.save(singers);
            System.out.println("Uploaded Photographer ID: " + newSinger.getId());

            return new ResponseEntity<>(newSinger, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

