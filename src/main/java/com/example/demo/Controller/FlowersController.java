package com.example.demo.Controller;

import com.example.demo.model.*;
import com.example.demo.services.FlowersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/flowers")
public class FlowersController {
@Autowired
private FlowersRepository flowersRepository;



    //ניתוב של הפרויקט הנוכחי
    private static String DIRECTORY_PATH = System.getProperty("user.dir") + "//images//";



    public FlowersController(FlowersRepository flowersRepository) {
        this.flowersRepository = flowersRepository;
    }




    @GetMapping("/getFlowers")
    public ResponseEntity<List<Flowers>> getFlowers(){
        return new ResponseEntity<>(flowersRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/getFlower/{id}")
    public ResponseEntity <Flowers> getFlower(@PathVariable Long id) {
        Flowers f = flowersRepository.findById(id).orElse(null);
        if (f == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(f, HttpStatus.OK);
    }

    @PostMapping("/addFlower")
    public ResponseEntity<Flowers> addFlower(@RequestBody Flowers flowers){
        Flowers newFlower=flowersRepository.save(flowers);
        return new ResponseEntity<>(newFlower, HttpStatus.CREATED);}


    @PutMapping(value = "/updateFlowers/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Flowers> updateFlowers(
            @RequestPart("flower") String flowersJson,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @PathVariable Long id) {
        try {
            Flowers existingFlower = flowersRepository.findById(id).orElse(null);
            if (existingFlower == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Flowers updatedFlower = objectMapper.readValue(flowersJson, Flowers.class);

            existingFlower.setName(updatedFlower.getName());
            existingFlower.setDescription(updatedFlower.getDescription());
            existingFlower.setPhone(updatedFlower.getPhone());

            // אם הועלתה תמונה, נשמור אותה ונעדכן את הנתיב
            if (file != null && !file.isEmpty()) {
                String relativePath = "/src/photo/flowers/" + file.getOriginalFilename();
                Path pathFile = Paths.get(DIRECTORY_PATH, file.getOriginalFilename());
                Files.write(pathFile, file.getBytes());
                existingFlower.setImageUrl(relativePath);
            }

            Flowers savedFlower = flowersRepository.save(existingFlower);
            return new ResponseEntity<>(savedFlower, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteFlower/{id}")
    public ResponseEntity<Void> deleteFlower(@PathVariable Long id){
        flowersRepository.deleteById(id);
        return ResponseEntity.noContent().build();  // מחזיר 204
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestPart("flower") String flowerJson,
            @RequestPart(value = "image", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Flowers flowers = objectMapper.readValue(flowerJson, Flowers.class);

            // אם התמונה לא נשלחה, דלג על לוגיקת שמירת התמונה
            if (file != null && !file.isEmpty()) {
                String relativePath = "/src/photo/flowers/" + file.getOriginalFilename();
                Path pathFile = Paths.get(DIRECTORY_PATH, file.getOriginalFilename());
                Files.write(pathFile, file.getBytes());
                flowers.setImageUrl(relativePath); // עדכון הנתיב היחסי רק אם יש תמונה
            }

            Flowers newFlower = flowersRepository.save(flowers);
            System.out.println("Uploaded Photographer ID: " + newFlower.getId());

            return new ResponseEntity<>(newFlower, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}










