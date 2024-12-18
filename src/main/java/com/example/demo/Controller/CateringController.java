package com.example.demo.Controller;

import com.example.demo.model.Catering;
import com.example.demo.model.Flowers;
import com.example.demo.model.Singers;
import com.example.demo.services.CateringRepository;
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
    @RequestMapping("api/catering")
    public class CateringController {
        @Autowired
        private CateringRepository cateringRepository;



        //ניתוב של הפרויקט הנוכחי
        private static String DIRECTORY_PATH = System.getProperty("user.dir") + "//images//";



        public CateringController(CateringRepository cateringRepository) {
            this.cateringRepository = cateringRepository;
        }




        @GetMapping("/getCatering")
        public ResponseEntity<List<Catering>> getCaterings(){
            return new ResponseEntity<>(cateringRepository.findAll(), HttpStatus.OK);
        }

        @GetMapping("/getCatering/{id}")
        public ResponseEntity <Catering> getCatering(@PathVariable Long id) {
            Catering c = cateringRepository.findById(id).orElse(null);
            if (c == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(c, HttpStatus.OK);
        }

        @PostMapping("/addCatering")
        public ResponseEntity<Catering> addFlower(@RequestBody Catering catering){
            Catering newCatering=cateringRepository.save(catering);
            return new ResponseEntity<>(newCatering, HttpStatus.CREATED);}


        @PutMapping(value = "/updateCatering/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<Catering> updateCatering(
                @RequestPart("catering") String cateringJson,
                @RequestPart(value = "image", required = false) MultipartFile file,
                @PathVariable Long id) {
            try {
                Catering existingCatering = cateringRepository.findById(id).orElse(null);
                if (existingCatering == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                ObjectMapper objectMapper = new ObjectMapper();
                Catering updatedCatering = objectMapper.readValue(cateringJson, Catering.class);

                existingCatering.setName(updatedCatering.getName());
                existingCatering.setDescription(updatedCatering.getDescription());
                existingCatering.setArea(updatedCatering.getArea());
                existingCatering.setType(updatedCatering.getType());
                existingCatering.setPhone(updatedCatering.getPhone());

                // אם הועלתה תמונה, נשמור אותה ונעדכן את הנתיב
                if (file != null && !file.isEmpty()) {
                    String relativePath = "/src/photo/catering/" + file.getOriginalFilename();
                    Path pathFile = Paths.get(DIRECTORY_PATH, file.getOriginalFilename());
                    Files.write(pathFile, file.getBytes());
                    existingCatering.setImageUrl(relativePath);
                }

                Catering savedCatering = cateringRepository.save(existingCatering);
                return new ResponseEntity<>(savedCatering, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @DeleteMapping("/deleteCatering/{id}")
        public ResponseEntity<Void> deleteCatering(@PathVariable Long id){
            cateringRepository.deleteById(id);
            return ResponseEntity.noContent().build();  // מחזיר 204
        }



        @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<?> upload(
                @RequestPart("catering") String cateringJson,
                @RequestPart(value = "image", required = false) MultipartFile file) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Catering catering = objectMapper.readValue(cateringJson, Catering.class);

                // אם התמונה לא נשלחה, דלג על לוגיקת שמירת התמונה
                if (file != null && !file.isEmpty()) {
                    String relativePath = "/src/photo/catering/" + file.getOriginalFilename();
                    Path pathFile = Paths.get(DIRECTORY_PATH, file.getOriginalFilename());
                    Files.write(pathFile, file.getBytes());
                    catering.setImageUrl(relativePath); // עדכון הנתיב היחסי רק אם יש תמונה
                }

                Catering newCatering= cateringRepository.save(catering);

                return new ResponseEntity<>(newCatering, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }




    }












